package com.example.index;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.index.Prevalent.Prevalent;
import com.example.index.ReadQRCode.QRGeo;
import com.example.index.ReadQRCode.QRURL;
import com.example.index.ReadQRCode.QRVCard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class AdminDeliveryMoneyActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    private String GiveUser="",UserMoney="",GiveUserName="";
    private String evendRecordID="";
    private int eventRecordMoney=0;
    private int AdminMoney = 0;
    private int FinalUserMoney = 0;
    private String  RecordCurrentDate, RecordCurrentTime, RecordKey;
    private DatabaseReference AdminRef = FirebaseDatabase.getInstance().getReference().child("Admins");
    private DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
    private DatabaseReference EventsListRef = FirebaseDatabase.getInstance().getReference().child("EventsList");
    private DatabaseReference RecordRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delivery_money);


        evendRecordID=getIntent().getStringExtra("eid");
        GiveUser=getIntent().getStringExtra("eUser");
        GiveUserName=getIntent().getStringExtra("eUserName");
        eventRecordMoney=getIntent().getIntExtra("eMoney",0);

        //Init
        scannerView = (ZXingScannerView) findViewById(R.id.zxscan);
        //Request permission
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scannerView.setResultHandler( AdminDeliveryMoneyActivity.this);
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText( AdminDeliveryMoneyActivity.this, "請允許此權限", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
    }


    @Override
    protected void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
    }


    @Override
    public void handleResult(Result rawResult) {

        //Here we can receive rawRusult
      //  processRawResult(rawResult.getText());

       if(rawResult.getText().equals(GiveUser)) {

               if (eventRecordMoney <= Prevalent.currentOnlineAdmin.getMoney()) {
                   // Sendmoney = Prevalent.currentOnlineUser.getMoney() - Integer.parseInt(money);
                   UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           UserMoney = dataSnapshot.child(GiveUser).child("money").getValue().toString();
                           AdminMoney = Prevalent.currentOnlineAdmin.getMoney() - eventRecordMoney;
                           FinalUserMoney = Integer.parseInt(UserMoney) + eventRecordMoney;

                           finalUpdayeMoney();
                           //SendRef.child("Users"+ Prevalent.currentOnlineUser.getAccount()).updateChildren(userMap);


                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });
               } else {
                   Toast.makeText(this, "您的剩餘紅包錢不足喔!", Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(AdminDeliveryMoneyActivity.this, AdminCategoryActivity.class);
                   startActivity(intent);
               }
      }else {
           Toast.makeText(AdminDeliveryMoneyActivity.this,"會員身分不正確",Toast.LENGTH_SHORT).show();
           Intent intent = new Intent(AdminDeliveryMoneyActivity.this, AdminCategoryActivity.class);
           startActivity(intent);
      }

    }

    private void finalUpdayeMoney() {

        HashMap<String, Object> adminMap = new HashMap<>();
        adminMap.put("money", AdminMoney);
        AdminRef.child("Admins"+Prevalent.currentOnlineAdmin.getAccount()).updateChildren(adminMap);
        HashMap<String, Object> userMapGet = new HashMap<>();
        userMapGet.put("money", FinalUserMoney);
        UserRef.child(GiveUser).updateChildren(userMapGet);
        Prevalent.currentOnlineAdmin.setMoney(AdminMoney);

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
                recordDeliveyMap.put("DeliveryAccount","Admins"+Prevalent.currentOnlineAdmin.getAccount() );
                recordDeliveyMap.put("ReceiveAccount", GiveUser);
                recordDeliveyMap.put("ReceiveName", GiveUserName);
                recordDeliveyMap.put("RecordDate", RecordCurrentDate);
                recordDeliveyMap.put("RecordTime", RecordCurrentTime);
                recordDeliveyMap.put("RecordMoney", eventRecordMoney);
                recordDeliveyMap.put("RecordThing", "參加活動獎勵");
                RecordRef.child("records").child("Delivery" + "Admins" + Prevalent.currentOnlineAdmin.getAccount()).child(RecordKey).updateChildren(recordDeliveyMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                                          UpdateStatus();

                        } else {
                            Toast.makeText(AdminDeliveryMoneyActivity.this, "請開啟網路...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                HashMap<String, Object> recordReceiveMap = new HashMap<>();
                recordReceiveMap.put("rid", RecordKey);
                recordReceiveMap.put("DeliveryAccount", "Admins"+Prevalent.currentOnlineAdmin.getAccount());
                recordReceiveMap.put("DeliveryName",Prevalent.currentOnlineAdmin.getName());
                recordReceiveMap.put("ReceiveAccount",  GiveUser);
                recordReceiveMap.put("RecordDate", RecordCurrentDate);
                recordReceiveMap.put("RecordTime", RecordCurrentTime);
                recordReceiveMap.put("RecordMoney",  eventRecordMoney);
                recordReceiveMap.put("RecordThing",  "參加活動獎勵");
                RecordRef.child("records").child("Receive" + GiveUser).child(RecordKey).updateChildren(recordReceiveMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            UpdateStatus();
                        } else {
                            Toast.makeText(AdminDeliveryMoneyActivity.this, "請開啟網路...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void UpdateStatus() {
        HashMap<String, Object> EventsListMap = new HashMap<>();
        EventsListMap.put("eStatus", "已報到");
        EventsListRef.child("Admin  View").child("Admins"+Prevalent.currentOnlineAdmin.getAccount()).child("Events").child(evendRecordID).child(GiveUser).updateChildren(EventsListMap);
        EventsListRef.child("User  View").child(GiveUser).child("Events").child(GiveUser+evendRecordID).updateChildren(EventsListMap);
         Toast.makeText(AdminDeliveryMoneyActivity.this,GiveUserName+"已經收到紅包錢"+eventRecordMoney+"囉",Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(AdminDeliveryMoneyActivity.this, AdminCategoryActivity.class);
          startActivity(intent);
    }

    /*private void processRawResult(String text) {
        if (text.startsWith("BEGIN:")) {
            String[] tokens = text.split("\n");
            QRVCard qrvCard = new QRVCard();
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].startsWith("BEGIN:")) {
                    qrvCard.setType(tokens[i].substring("BEGIN:".length())); //Remove  BEGIN: to  get Type
                } else if (tokens[i].startsWith("N:")) {
                    qrvCard.setName(tokens[i].substring("N:".length()));
                } else if (tokens[i].startsWith("ORG:")) {
                    qrvCard.setOrg(tokens[i].substring("ORG:".length()));
                } else if (tokens[i].startsWith("TEL:")) {
                    qrvCard.setTel(tokens[i].substring("TEL:".length()));
                } else if (tokens[i].startsWith("URL:")) {
                    qrvCard.setUrl(tokens[i].substring("URL:".length()));
                } else if (tokens[i].startsWith("EMAIL:")) {
                    qrvCard.setEmail(tokens[i].substring("EMAIL:".length()));
                } else if (tokens[i].startsWith("ADR:")) {
                    qrvCard.setAddress(tokens[i].substring("ADR:".length()));
                } else if (tokens[i].startsWith("NOTE:")) {
                    qrvCard.setNote(tokens[i].substring("NOTE:".length()));
                } else if (tokens[i].startsWith("SUMMARY:")) {
                    qrvCard.setSummary(tokens[i].substring("SUMMARY:".length()));
                } else if (tokens[i].startsWith("DTSTART:")) {
                    qrvCard.setDtstart(tokens[i].substring("DTSTART:".length()));
                } else if (tokens[i].startsWith("DREND:")) {
                    qrvCard.setDtend(tokens[i].substring("DTEND".length()));
                }

                //Try to show
            }
        } else if (text.startsWith("http://") ||
                text.startsWith("http://") ||
                text.startsWith("www.")) {
            QRURL qrurl = new QRURL(text);
        } else if (text.startsWith("geo:")) {
            QRGeo qrGeo = new QRGeo();
            String delims = "[  ,  ?q=  ]+";
            String tokens[] = text.split(delims);

            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].startsWith("  geo:")) {
                    qrGeo.setLat(tokens[i].substring("geo:".length()));
                }
            }
            qrGeo.setLat(tokens[0].substring("geo:".length()));
            qrGeo.setLng(tokens[1]);
            qrGeo.setGeo_place(tokens[2]);
        } else {
        }
        scannerView.resumeCameraPreview( AdminDeliveryMoneyActivity.this);
    }*/
}
