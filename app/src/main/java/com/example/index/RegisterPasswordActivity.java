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


public class RegisterPasswordActivity extends AppCompatActivity {

    private Button Next;
    private EditText  InputPassword;
    private ProgressDialog loadingBar;
    private String password,account,downloadImageUrl;
    private TextView HavaAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_password);



        Next = (Button) findViewById(R.id.register_btn);
        InputPassword = (EditText) findViewById(R.id.register_password_input);
        HavaAccount = (TextView) findViewById(R.id.hava_account);
        loadingBar = new ProgressDialog(this);

        account=getIntent().getStringExtra("account");
        downloadImageUrl= getIntent().getStringExtra("image");

        HavaAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        Next.setOnClickListener(


                new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    // do something you want
                    Next();
                } else {
                    Toast.makeText(RegisterPasswordActivity.this, "請開啟網路", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void Next() {
        ValidatephoneNumber();
    }
    private void ValidatephoneNumber() {
        password = InputPassword.getText().toString();

        if (TextUtils.isEmpty( password)) {
            Toast.makeText(this, "請輸入密碼...", Toast.LENGTH_SHORT).show();
        } else {
            SaveUserInfoToDarabase();
        }
    }
    private void SaveUserInfoToDarabase() {

                    Intent intent=new Intent(RegisterPasswordActivity.this,RegisterNamePhoneActivity.class);
                    intent.putExtra("account",account);
                    intent.putExtra("image",downloadImageUrl);
                    intent.putExtra("password",password);
                    startActivity(intent);

    }

}
