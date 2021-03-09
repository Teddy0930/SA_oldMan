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


public class RegisterAccountActivity extends AppCompatActivity {

    private Button Next;
    private EditText  InputAccount;
    private CircleImageView InputUserImage;
    private ProgressDialog loadingBar;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private StorageReference UserImagesRef;
    private String downloadImageUrl;
    private String account, image;
    private DatabaseReference UserRef;
    private String parentDbName = "Users";
    private TextView HavaAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);



        UserImagesRef = FirebaseStorage.getInstance().getReference().child("User  Image");
        UserRef = FirebaseDatabase.getInstance().getReference();

        Next = (Button) findViewById(R.id.register_btn);
        InputUserImage = (CircleImageView) findViewById(R.id.register_userimage);
        InputAccount = (EditText) findViewById(R.id.register_account_input);
        HavaAccount = (TextView) findViewById(R.id.hava_account);
        loadingBar = new ProgressDialog(this);


        HavaAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterAccountActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        InputUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(ImageUri)
                        .setAspectRatio(1, 1)
                        .start(RegisterAccountActivity.this);
             //   OpenGallery();
            }
        });
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    // do something you want
                    Next();
                } else {
                    Toast.makeText(RegisterAccountActivity.this, "請開啟網路", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void Next() {
        ValidatephoneNumber();
    }

    private void ValidatephoneNumber() {
        account = InputAccount.getText().toString();

        if (TextUtils.isEmpty(account)) {
            Toast.makeText(this, "請輸入帳號...", Toast.LENGTH_SHORT).show();
        } else {
            StoreUserInformation();
        }
    }

    private void StoreUserInformation() {
        loadingBar.setTitle("下一步");
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
                    Toast.makeText(RegisterAccountActivity.this, "Error:  " + message, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(RegisterAccountActivity.this, "請選擇圖片...", Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }
    }

    private void SaveUserInfoToDarabase() {
        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child(parentDbName).child(parentDbName + account).exists())) {


                    Intent intent=new Intent(RegisterAccountActivity.this,RegisterPasswordActivity.class);
                    intent.putExtra("account",account);
                    intent.putExtra("image",downloadImageUrl);
                    startActivity(intent);
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(RegisterAccountActivity.this, "帳號: " + account + "  ,已經存在了" + "請使用其他帳號...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

   /* private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

     /*   if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            InputUserImage.setImageURI(ImageUri);
        }*/
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            ImageUri = result.getUri();

            InputUserImage.setImageURI(ImageUri);
        } else {
            Toast.makeText(this, "請再嘗試一次", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterAccountActivity.this,RegisterAccountActivity.class));
            finish();
        }
    }
}
