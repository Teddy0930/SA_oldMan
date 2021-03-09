package com.example.index;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ManagerActivity extends AppCompatActivity {

    private Button CreateAccountButton;
    private EditText InputName, InputPhoneNumber, InputPassword, InputAccount;
    private CircleImageView InputUserImage;
    private ProgressDialog loadingBar;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private StorageReference UserImagesRef;
    private String downloadImageUrl;
    private String name, phone, account, password, image;
    private DatabaseReference UserRef;
    private String parentDbName = "Users";
    private TextView  AdminLink, NotAdminlink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);


        UserImagesRef = FirebaseStorage.getInstance().getReference().child("User  Image");
        UserRef = FirebaseDatabase.getInstance().getReference();

        CreateAccountButton = (Button) findViewById(R.id.register_btn);
        InputUserImage = (CircleImageView) findViewById(R.id.register_userimage);
        InputName = (EditText) findViewById(R.id.register_username_input);
        InputAccount = (EditText) findViewById(R.id.register_account_input);
        InputPassword = (EditText) findViewById(R.id.register_password_input);
        InputPhoneNumber = (EditText) findViewById(R.id.register_phone_number_input);
        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminlink = (TextView) findViewById(R.id.not_admin_panel_link);
        loadingBar = new ProgressDialog(this);



        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccountButton.setText("廠商註冊");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminlink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });

        NotAdminlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccountButton.setText("註冊");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminlink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });


        InputUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(ImageUri)
                        .setAspectRatio(1, 1)
                        .start(ManagerActivity.this);
               // OpenGallery();
            }
        });
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    // do something you want
                    CreateAccountButton();
                } else {
                    Toast.makeText(ManagerActivity.this, "請開啟網路", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

   /* private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }*/

    private void CreateAccountButton() {

        ValidatephoneNumber();
    }

    private void ValidatephoneNumber() {
        name = InputName.getText().toString();
        phone = InputPhoneNumber.getText().toString();
        password = InputPassword.getText().toString();
        account = InputAccount.getText().toString();

        if (TextUtils.isEmpty(account)) {
            Toast.makeText(this, "請輸入帳號...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "請輸入密碼...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "請輸入姓名...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "請輸入手機號碼...", Toast.LENGTH_SHORT).show();
        } else {
            StoreUserInformation();
        }

    }

    private void StoreUserInformation() {
        loadingBar.setTitle("帳號建立中");
        loadingBar.setMessage("請稍號, 正在為您確認...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        if (ImageUri != null) {


            final StorageReference filePath = UserImagesRef.child(ImageUri.getLastPathSegment() + account + ".jpg");
            final UploadTask uploadTask = filePath.putFile(ImageUri);


            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(ManagerActivity.this, "Error:  " + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                            if (!task.isSuccessful()) {

                                throw task.getException();

                            }

                            downloadImageUrl = filePath.getDownloadUrl().toString();
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                downloadImageUrl = task.getResult().toString();

                                SaveUserInfoToDarabase();


                            }
                        }
                    });
                }
            });
        } else {
            Toast.makeText(ManagerActivity.this, "請選擇圖片...", Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }
    }

    private void SaveUserInfoToDarabase() {


        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child(parentDbName).child(parentDbName + account).exists())) {

                    if (parentDbName.equals("Users")) {
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("uid", parentDbName + account);
                        userMap.put("image", downloadImageUrl);
                        userMap.put("account", account);
                        userMap.put("password", password);
                        userMap.put("name", name);
                        userMap.put("phone", phone);
                        userMap.put("money", 0);

                        UserRef.child(parentDbName).child(parentDbName + account).updateChildren(userMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            loadingBar.dismiss();
                                            Toast.makeText(ManagerActivity.this, "會員註冊成功..", Toast.LENGTH_SHORT).show();

                                        } else {
                                            loadingBar.dismiss();
                                            Toast.makeText(ManagerActivity.this, "請開啟網路...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("aid", parentDbName + account);
                        userMap.put("image", downloadImageUrl);
                        userMap.put("account", account);
                        userMap.put("password", password);
                        userMap.put("name", name);
                        userMap.put("phone", phone);
                        userMap.put("money", 10000);

                        UserRef.child(parentDbName).child(parentDbName + account).updateChildren(userMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            loadingBar.dismiss();
                                            Toast.makeText(ManagerActivity.this, "廠商註冊成功..", Toast.LENGTH_SHORT).show();

                                        } else {
                                            loadingBar.dismiss();
                                            Toast.makeText(ManagerActivity.this, "請開啟網路...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }

                } else {
                    loadingBar.dismiss();
                    Toast.makeText(ManagerActivity.this, "帳號: " + account + "  ,已經存在了" + "請使用其他帳號...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        /*if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            InputUserImage.setImageURI(ImageUri);
        }*/
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            ImageUri = result.getUri();

            InputUserImage.setImageURI(ImageUri);
        } else {
            Toast.makeText(this, "請再嘗試一次", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ManagerActivity.this,ManagerActivity.class));
            finish();
        }
    }

}

