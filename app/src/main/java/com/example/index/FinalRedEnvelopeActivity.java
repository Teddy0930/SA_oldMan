package com.example.index;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.index.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class FinalRedEnvelopeActivity extends AppCompatActivity {

    private ProgressDialog loadingBar;
    private EditText etMoney;
    private Button btnSend;
    private String money, receiveUser, receiverUserMoney,receiverUserName, RecordCurrentDate, RecordCurrentTime, RecordKey;
    private int Sendmoney = 0;
    private int Getmoney = 0;
    private DatabaseReference SendRef = FirebaseDatabase.getInstance().getReference().child("Users");
    private DatabaseReference RecordRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_red_envelope);

        receiveUser = getIntent().getStringExtra("uid");

        etMoney = (EditText) findViewById(R.id.et_envelope_money);
        btnSend = (Button) findViewById(R.id.btn_confirm_send);
        loadingBar = new ProgressDialog(this);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setTitle("處理中");
                loadingBar.setMessage("請稍號, 正在為您送禮...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                updateMoney();
            }
        });
    }

    private void updateMoney() {
        money = etMoney.getText().toString();
        try {
            if (Integer.parseInt(money) > 0) {
                if (Integer.parseInt(money) <= Prevalent.currentOnlineUser.getMoney()) {
                    // Sendmoney = Prevalent.currentOnlineUser.getMoney() - Integer.parseInt(money);
                    SendRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            receiverUserMoney = dataSnapshot.child(receiveUser).child("money").getValue().toString();
                            receiverUserName = dataSnapshot.child(receiveUser).child("name").getValue().toString();


                            Sendmoney = Prevalent.currentOnlineUser.getMoney() - Integer.parseInt(money);
                            Getmoney = Integer.parseInt(receiverUserMoney) + Integer.parseInt(money);
                            finalUpdayeMoney();
                            //SendRef.child("Users"+ Prevalent.currentOnlineUser.getAccount()).updateChildren(userMap);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(this, "您的剩餘紅包錢不足喔!", Toast.LENGTH_SHORT).show();
                }
            } else {
                loadingBar.dismiss();
                Toast.makeText(this, "請輸入0以上的數字", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            loadingBar.dismiss();
            Toast.makeText(this, "請輸入數字", Toast.LENGTH_SHORT).show();
        }

    }

    private void finalUpdayeMoney() {

        HashMap<String, Object> userMapReceive = new HashMap<>();
        userMapReceive.put("money", Getmoney);
        SendRef.child(receiveUser).updateChildren(userMapReceive);
        HashMap<String, Object> userMapGet = new HashMap<>();
        userMapGet.put("money", Sendmoney);
        SendRef.child("Users" + Prevalent.currentOnlineUser.getAccount()).updateChildren(userMapGet);
        Prevalent.currentOnlineUser.setMoney(Sendmoney);

        RecordRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat currentDate = new SimpleDateFormat("MMM  dd,   yyyy");
                RecordCurrentDate = currentDate.format(calendar.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss  a");
                RecordCurrentTime = currentTime.format(calendar.getTime());

                RecordKey = RecordCurrentDate + RecordCurrentTime;

                HashMap<String, Object> recordDeliveyMap = new HashMap<>();
                recordDeliveyMap.put("did", RecordKey);
                recordDeliveyMap.put("DeliveryAccount", "Users" + Prevalent.currentOnlineUser.getAccount());
                recordDeliveyMap.put("ReceiveName", receiverUserName);
                recordDeliveyMap.put("ReceiveAccount", receiveUser);
                recordDeliveyMap.put("RecordDate", RecordCurrentDate);
                recordDeliveyMap.put("RecordTime", RecordCurrentTime);
                recordDeliveyMap.put("RecordMoney", Integer.parseInt(money));
                recordDeliveyMap.put("RecordThing", "紅包");
                RecordRef.child("records").child("Delivery" + "Users" + Prevalent.currentOnlineUser.getAccount()).child(RecordKey).updateChildren(recordDeliveyMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loadingBar.dismiss();
                            Toast.makeText(FinalRedEnvelopeActivity.this, "送禮成功..", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(FinalRedEnvelopeActivity.this, HomeActivity.class);
                            startActivity(intent);

                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(FinalRedEnvelopeActivity.this, "請開啟網路...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                HashMap<String, Object> recordReceiveMap = new HashMap<>();
                recordReceiveMap.put("rid", RecordKey);
                recordReceiveMap.put("DeliveryAccount", "Users" + Prevalent.currentOnlineUser.getAccount());
                recordReceiveMap.put("DeliveryName", Prevalent.currentOnlineUser.getName());
                recordReceiveMap.put("ReceiveAccount", receiveUser);
                recordReceiveMap.put("RecordDate", RecordCurrentDate);
                recordReceiveMap.put("RecordTime", RecordCurrentTime);
                recordReceiveMap.put("RecordMoney", Integer.parseInt(money));
                recordReceiveMap.put("RecordThing", "紅包");
                RecordRef.child("records").child("Receive" + receiveUser).child(RecordKey).updateChildren(recordReceiveMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loadingBar.dismiss();
                            Toast.makeText(FinalRedEnvelopeActivity.this, "送禮成功..", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(FinalRedEnvelopeActivity.this, HomeActivity.class);
                            startActivity(intent);

                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(FinalRedEnvelopeActivity.this, "請開啟網路...", Toast.LENGTH_SHORT).show();
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
