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

import java.util.HashMap;


public class RegisterNamePhoneActivity extends AppCompatActivity {

    private Button RegisterOver;
    private EditText  InputName,InputPhone;
    private ProgressDialog loadingBar;
    private String password,account,downloadImageUrl,name,phone;
    private TextView HavaAccount;
    private DatabaseReference UserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_name_phone);


        Toast.makeText(RegisterNamePhoneActivity.this,"加油!只差最後一部瞜!!",Toast.LENGTH_SHORT).show();
        UserRef = FirebaseDatabase.getInstance().getReference();
        RegisterOver= (Button) findViewById(R.id.register_over_btn);
        InputPhone = (EditText) findViewById(R.id.register_phone_number_input);
        InputName = (EditText) findViewById(R.id.register_name_input);
        HavaAccount = (TextView) findViewById(R.id.hava_account);
        loadingBar = new ProgressDialog(this);

        account=  getIntent().getStringExtra("account");
        downloadImageUrl=getIntent().getStringExtra("image");
        password= getIntent().getStringExtra("password");

        HavaAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterNamePhoneActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        RegisterOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    // do something you want
                    RegisterOver();
                } else {
                    Toast.makeText(RegisterNamePhoneActivity.this, "請開啟網路", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void RegisterOver() {
        ValidatephoneNumber();
    }
    private void ValidatephoneNumber() {
         name= InputName.getText().toString();
        phone = InputPhone.getText().toString();
        if (TextUtils.isEmpty( name)) {
            Toast.makeText(this, "請輸入姓名...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(  phone)) {
            Toast.makeText(this, "請輸入手機號碼...", Toast.LENGTH_SHORT).show();
        }
        else {
            SaveUserInfoToDarabase();
        }
    }

    private void SaveUserInfoToDarabase() {
        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("uid", "Users" + account);
                        userMap.put("image", downloadImageUrl);
                        userMap.put("account", account);
                        userMap.put("password", password);
                        userMap.put("name", name);
                        userMap.put("phone", phone);
                        userMap.put("money", 0);

                        UserRef.child("Users").child("Users" + account).updateChildren(userMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            loadingBar.dismiss();
                                            Toast.makeText(RegisterNamePhoneActivity.this, "會員註冊成功..", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegisterNamePhoneActivity.this, LoginActivity.class);
                                            startActivity(intent);

                                        } else {
                                            loadingBar.dismiss();
                                            Toast.makeText(RegisterNamePhoneActivity.this, "請開啟網路...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
