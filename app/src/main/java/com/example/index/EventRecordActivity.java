package com.example.index;

import android.os.Bundle;


import com.example.index.Model.EventRecord;
import com.example.index.Prevalent.Prevalent;
import com.example.index.ViewHolder.EventRecordViewHolder;
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

public class EventRecordActivity extends AppCompatActivity {


    private DatabaseReference EventRecordRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_record);

        EventRecordRef = FirebaseDatabase.getInstance().getReference().child("EventsList").child("User  View").child("Users" + Prevalent.currentOnlineUser.getAccount()).child("Events");

        Paper.init(this);

        recyclerView = findViewById(R.id.recycler_menuEvent);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<EventRecord> options =
                new FirebaseRecyclerOptions.Builder<EventRecord>()
                        .setQuery(EventRecordRef, EventRecord.class)
                        .build();

        FirebaseRecyclerAdapter<EventRecord, EventRecordViewHolder> adapter =
                new FirebaseRecyclerAdapter<EventRecord, EventRecordViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull EventRecordViewHolder holder, int position, @NonNull final EventRecord model) {

                        holder.txtEventRecordName.setText("活動名稱:" + model.geteName() + ",  ");
                        holder.txtEventRecordDate.setText("活動日期:" + model.geteDate());
                        holder.txtEventRecordPlace.setText("活動地點:" + model.getePlace());
                        holder.txtEventRecordStatus.setText("是否報到:"+model.geteStatus());

                    }


                    @NonNull
                    @Override
                    public EventRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_record_items_layout, parent, false);
                        EventRecordViewHolder holder = new EventRecordViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
