package com.example.index;

import android.os.Bundle;


import com.example.index.Model.RedEnvelopeRecord;
import com.example.index.Prevalent.Prevalent;
import com.example.index.ViewHolder.RedEnvelopeReceiveViewHolder;
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

public class RedEnvelopeRecord1Activity extends AppCompatActivity {
    private DatabaseReference RedEnvelopeRecordReceiveRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_envelope_record1);

        RedEnvelopeRecordReceiveRef = FirebaseDatabase.getInstance().getReference().child("records").child("ReceiveUsers" + Prevalent.currentOnlineUser.getAccount());


        Paper.init(this);

        recyclerView = findViewById(R.id.recycler_menuRedEnvelope1);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<RedEnvelopeRecord> options =
                new FirebaseRecyclerOptions.Builder<RedEnvelopeRecord>()
                        .setQuery(RedEnvelopeRecordReceiveRef, RedEnvelopeRecord.class)
                        .build();

        FirebaseRecyclerAdapter<RedEnvelopeRecord, RedEnvelopeReceiveViewHolder> adapter =
                new FirebaseRecyclerAdapter<RedEnvelopeRecord, RedEnvelopeReceiveViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull RedEnvelopeReceiveViewHolder holder, int position, @NonNull final RedEnvelopeRecord model) {
                        holder.txtRedEnvelopeRecordName.setText(model.getDeliveryName() + ", 送了你 ");
                        holder.txtRedEnvelopeRecordTotalPrice.setText(model.getRecordMoney() + "$");

                    }


                    @NonNull
                    @Override
                    public RedEnvelopeReceiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.red_envelope_record_items_layout, parent, false);
                        RedEnvelopeReceiveViewHolder holder = new RedEnvelopeReceiveViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
