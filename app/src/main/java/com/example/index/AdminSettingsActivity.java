package com.example.index;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.index.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminSettingsActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private EditText fullNameEditText, userPhoneEditText, passwordEditText;
    private TextView profileChangeTextBtn, closeTextBtn, saveTextButton;

    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePictureRef;
    private String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);

        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Admin  Image"); //儲存庫

        profileImageView = (CircleImageView) findViewById(R.id.settings_profile_image);
        fullNameEditText = (EditText) findViewById(R.id.settings_full_name);
        userPhoneEditText = (EditText) findViewById(R.id.settings_phone_number);
        passwordEditText = (EditText) findViewById(R.id.settings_password);
        profileChangeTextBtn = (TextView) findViewById(R.id.profile_image_change_btn);
        closeTextBtn = (TextView) findViewById(R.id.close_settings_btn);
        saveTextButton = (TextView) findViewById(R.id.update_account_settings_btn);

        userInfoDisplay(profileImageView, fullNameEditText, userPhoneEditText, passwordEditText);

        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checker.equals("clicked")) {

                    userInfoSaved();
                } else {
                    updateOnlyUserInfo();
                }
            }
        });

        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";

                //裁切圖片
                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(AdminSettingsActivity.this);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImageView.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "請再嘗試一次", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminSettingsActivity.this, AdminSettingsActivity.class));
            finish();
        }
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Admins");

        HashMap<String, Object> adminMap = new HashMap<>();
        adminMap.put("name", fullNameEditText.getText().toString());
        adminMap.put("password", passwordEditText.getText().toString());
        adminMap.put("phone", userPhoneEditText.getText().toString());
        ref.child("Admins" + Prevalent.currentOnlineAdmin.getAccount()).updateChildren(adminMap);

        Prevalent.currentOnlineAdmin.setName(fullNameEditText.getText().toString());

        startActivity(new Intent(AdminSettingsActivity.this, AdminCategoryActivity.class));
        Toast.makeText(AdminSettingsActivity.this, "檔案已經都更改成功囉!", Toast.LENGTH_SHORT).show();
        finish();

    }

    private void userInfoSaved() {

        if (TextUtils.isEmpty(fullNameEditText.getText().toString())) {
            Toast.makeText(this, "請輸入姓名", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(passwordEditText.getText().toString())) {
            Toast.makeText(this, "請輸入密碼", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(userPhoneEditText.getText().toString())) {
            Toast.makeText(this, "請輸入手機號碼", Toast.LENGTH_SHORT).show();
        } else if (checker.equals("clicked")) {

            uploadImage();

        }

    }

    private void uploadImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("更新檔案");
        progressDialog.setMessage("請稍後,我們正在更新您的檔案");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null) {

            final StorageReference fileRef = storageProfilePictureRef
                    .child("Admins" + Prevalent.currentOnlineAdmin.getAccount() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {


                    if (!task.isSuccessful()) {

                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() { //加了<Uri>
                @Override
                public void onComplete(@NonNull Task<Uri> task) {  //加了<Uri>
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Admins");

                        HashMap<String, Object> adminMap = new HashMap<>();
                        adminMap.put("name", fullNameEditText.getText().toString());
                        adminMap.put("password", passwordEditText.getText().toString());
                        adminMap.put("phone", userPhoneEditText.getText().toString());
                        adminMap.put("image", myUrl);
                        ref.child("Admins" + Prevalent.currentOnlineAdmin.getAccount()).updateChildren(adminMap);
                        Prevalent.currentOnlineAdmin.setImage(myUrl);
                        Prevalent.currentOnlineAdmin.setName(fullNameEditText.getText().toString());

                        progressDialog.dismiss();

                        startActivity(new Intent(AdminSettingsActivity.this, AdminCategoryActivity.class));
                        Toast.makeText(AdminSettingsActivity.this, "檔案更新成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(AdminSettingsActivity.this, "錯誤", Toast.LENGTH_SHORT).show();

                    }
                }
            });


        } else {
            Toast.makeText(AdminSettingsActivity.this, "沒有選擇照片", Toast.LENGTH_SHORT).show();

        }
    }


    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEditText, final EditText userPhoneEditText, final EditText passwordEditText) {

        DatabaseReference AdminsRef = FirebaseDatabase.getInstance().getReference().child("Admins").child("Admins" + Prevalent.currentOnlineAdmin.getAccount());

        AdminsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("image").exists()) {

                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String password = dataSnapshot.child("password").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                        fullNameEditText.setText(name);
                        userPhoneEditText.setText(phone);
                        passwordEditText.setText(password);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
