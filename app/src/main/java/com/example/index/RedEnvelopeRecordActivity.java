package com.example.index;

import android.os.Bundle;


import com.example.index.Model.RedEnvelopeRecord;
import com.example.index.Prevalent.Prevalent;
import com.example.index.ViewHolder.RedEnvelopeRecordViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import android.view.LayoutInflater;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ViewGroup;

import io.paperdb.Paper;

public class RedEnvelopeRecordActivity extends AppCompatActivity {
    private DatabaseReference RedEnvelopeRecordDeliveryRef, RedEnvelopeRecordReceiveRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_envelope_record);

        RedEnvelopeRecordDeliveryRef = FirebaseDatabase.getInstance().getReference().child("records").child("DeliveryUsers" + Prevalent.currentOnlineUser.getAccount());


        Paper.init(this);

        recyclerView = findViewById(R.id.recycler_menuRedEnvelope);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<RedEnvelopeRecord> options =
                new FirebaseRecyclerOptions.Builder<RedEnvelopeRecord>()
                        .setQuery(RedEnvelopeRecordDeliveryRef, RedEnvelopeRecord.class)
                        .build();

        FirebaseRecyclerAdapter<RedEnvelopeRecord, RedEnvelopeRecordViewHolder> adapter =
                new FirebaseRecyclerAdapter<RedEnvelopeRecord, RedEnvelopeRecordViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull RedEnvelopeRecordViewHolder holder, int position, @NonNull final RedEnvelopeRecord model) {
                        holder.txtRedEnvelopeRecordName.setText("你送了" + model.getReceiveName() + ",  ");
                        holder.txtRedEnvelopeRecordTotalPrice.setText(model.getRecordMoney() + "$");

                    }


                    @NonNull
                    @Override
                    public RedEnvelopeRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.red_envelope_record_items_layout, parent, false);
                        RedEnvelopeRecordViewHolder holder = new RedEnvelopeRecordViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
