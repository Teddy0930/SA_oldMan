package com.example.index;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserRecordsActivity extends AppCompatActivity {

    private Button EventRecord, ProductRecord, RedEnvelopeRecord, RedEvnelopeRecord1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_records);

        EventRecord = findViewById(R.id.btn_event_record);
        ProductRecord = findViewById(R.id.btn_redeem_product_record);
        RedEnvelopeRecord = findViewById(R.id.btn_red_envelope_record);
        RedEvnelopeRecord1 = findViewById(R.id.btn_red_envelope_record1);

        EventRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserRecordsActivity.this, EventRecordActivity.class);
                startActivity(intent);

            }
        });
        ProductRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserRecordsActivity.this, ProductRecordActivity.class);
                startActivity(intent);

            }
        });
        RedEnvelopeRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserRecordsActivity.this, RedEnvelopeRecordActivity.class);
                startActivity(intent);

            }
        });
        RedEvnelopeRecord1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserRecordsActivity.this, RedEnvelopeRecord1Activity.class);
                startActivity(intent);
            }
        });
    }
}
