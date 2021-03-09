package com.example.index;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.index.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ReceiveRedEnvelopeActivity extends AppCompatActivity {

    private ImageView getenvelope;
    private DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_red_envelope);
        getCode();
        receiveRedEnveloper();
    }

    private void receiveRedEnveloper() {
        UserRef.child("Users"+Prevalent.currentOnlineUser.getAccount()).child("money").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(Integer.parseInt(dataSnapshot.getValue().toString())!=Prevalent.currentOnlineUser.getMoney()){
                    Toast.makeText(ReceiveRedEnvelopeActivity.this,"恭喜!收到紅包囉!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getCode() {
        getenvelope = (ImageView) findViewById(R.id.iv_get);
        BarcodeEncoder encoder = new BarcodeEncoder();
        try {
            Bitmap bit = encoder.encodeBitmap("Users" + Prevalent.currentOnlineUser.getAccount(), BarcodeFormat.QR_CODE, 250, 250);
            getenvelope.setImageBitmap(bit);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

}
