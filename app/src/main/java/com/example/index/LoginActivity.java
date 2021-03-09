package com.example.index;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.example.index.Model.Admins;
import com.rey.material.widget.CheckBox;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.index.Model.Users;
import com.example.index.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText InputAccount, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView AdminLink, NotAdminlink, NotRegister;

    private String parentDbName = "Users";
    private CheckBox chkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        LoginButton = (Button) findViewById(R.id.login_btn);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        InputAccount = (EditText) findViewById(R.id.login_account_input);
        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminlink = (TextView) findViewById(R.id.not_admin_panel_link);
        NotRegister = (TextView) findViewById(R.id.register_link);
        loadingBar = new ProgressDialog(this);


        //chkBoxRememberMe = findViewById(R.id.remember_me_chkb);
        Paper.init(this);

        NotRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterAccountActivity.class);
                startActivity(intent);
            }
        });
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    // do something you want
                    LoginUser();

                } else {
                    Toast.makeText(LoginActivity.this, "請開啟網路", Toast.LENGTH_SHORT).show();
                }

            }
        });
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("廠商登入");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminlink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });

        NotAdminlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("登入");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminlink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });


    }


    private void LoginUser() {

        String account = InputAccount.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(account)) {
            Toast.makeText(this, "請輸入帳號...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "請輸入密碼...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("帳號登入中");
            loadingBar.setMessage("請稍號,正在確認.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(account, password);
        }
    }

    private void AllowAccessToAccount(final String account, final String password) {


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(parentDbName).child(parentDbName + account).exists()) {

                    Users usersData = dataSnapshot.child(parentDbName).child(parentDbName + account).getValue(Users.class);
                    Admins adminsData = dataSnapshot.child(parentDbName).child(parentDbName + account).getValue(Admins.class);
                    if (usersData.getAccount().equals(account)) {
                        if (usersData.getPassword().equals(password)) {
                            if (usersData.getPassword().equals("admin0962000923")||usersData.getAccount().equals("admin")) {
                                Toast.makeText(LoginActivity.this, "歡迎管理員...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, ManagerActivity.class);
                                startActivity(intent);
                            }
                            else  if (parentDbName.equals("Admins")) {
                                Toast.makeText(LoginActivity.this, "歡迎廠商...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                Prevalent.currentOnlineAdmin = adminsData;
                                startActivity(intent);
                            } else if (parentDbName.equals("Users")) {

                                Toast.makeText(LoginActivity.this, "登入成功...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

                                Prevalent.currentOnlineUser = usersData;

                                startActivity(intent);
                            }
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "密碼不正確.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "帳號:  " + account + "不存在", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
