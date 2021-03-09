package com.example.index;

import android.content.Intent;
import android.os.Bundle;

import com.example.index.Model.AdminsNewEventRecord;
import com.example.index.Model.Admins;
import com.example.index.Prevalent.Prevalent;
import com.example.index.ViewHolder.AdminsNewEventRecordViewHolder;
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
import android.widget.Toast;

import io.paperdb.Paper;

public class AdminsNewEventRecordDetailsActivity extends AppCompatActivity {


    private String evendRecordID="";
    private int eventRecordMoney=0;
    private DatabaseReference AdminsEventRecordRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admins_new_event_record_details);

        evendRecordID=getIntent().getStringExtra("eid");
        eventRecordMoney=getIntent().getIntExtra("eMoney",0);
        AdminsEventRecordRef = FirebaseDatabase.getInstance().getReference().child("EventsList").child("Admin  View").child("Admins" + Prevalent.currentOnlineAdmin.getAccount()).child("Events").child(evendRecordID);

        Paper.init(this);

        recyclerView = findViewById(R.id.recycler_menuAdminEventDetails);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminsNewEventRecord> options =
                new FirebaseRecyclerOptions.Builder<AdminsNewEventRecord>()
                        .setQuery(AdminsEventRecordRef, AdminsNewEventRecord.class)
                        .build();

        FirebaseRecyclerAdapter<AdminsNewEventRecord,AdminsNewEventRecordViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminsNewEventRecord, AdminsNewEventRecordViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminsNewEventRecordViewHolder holder, int position, @NonNull final AdminsNewEventRecord model) {

                        holder.txtAdminsEventRecordUser.setText("參加人員:" + model.geteUserName() + ",  ");
                        holder.txtAdminsEventRecordDate.setText("活動日期:" + model.geteDate());
                        holder.txtAdminsEventRecordPlace.setText("活動地點:" + model.getePlace());
                        holder.txtAdminsEventRecordStatus.setText("是否報到:"+model.geteStatus());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(model.geteStatus().equals("未報到")) {
                                    Intent intent = new Intent(AdminsNewEventRecordDetailsActivity.this, AdminDeliveryMoneyActivity.class);
                                    intent.putExtra("eid", evendRecordID);
                                    intent.putExtra("eMoney", eventRecordMoney);
                                    intent.putExtra("eUser", model.geteUser().toString());
                                    intent.putExtra("eUserName", model.geteUserName().toString());
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(AdminsNewEventRecordDetailsActivity.this,"這個會員已經報到過囉",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }


                    @NonNull
                    @Override
                    public AdminsNewEventRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admins_event_records_details_layout, parent, false);
                        AdminsNewEventRecordViewHolder holder = new AdminsNewEventRecordViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
