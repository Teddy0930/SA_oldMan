package com.example.index;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.index.Model.Events;
import com.example.index.Prevalent.Prevalent;
import com.example.index.ViewHolder.AdminsEventRecordViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ViewGroup;
import android.widget.Toast;

import io.paperdb.Paper;

public class AdminsNewEventRecordActivity extends AppCompatActivity {


    private DatabaseReference AdminsEventRecordRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admins_new_event_record);

        AdminsEventRecordRef = FirebaseDatabase.getInstance().getReference().child("Events").child("Admin  View").child("Admins" + Prevalent.currentOnlineAdmin.getAccount());

        Paper.init(this);

        recyclerView = findViewById(R.id.recycler_menuAdminEvent);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference EventRef = FirebaseDatabase.getInstance().getReference().child("Events").child("User  View");
        FirebaseRecyclerOptions<Events> options =
                new FirebaseRecyclerOptions.Builder<Events>()
                        .setQuery(AdminsEventRecordRef, Events.class)
                        .build();

        FirebaseRecyclerAdapter<Events, AdminsEventRecordViewHolder> adapter =
                new FirebaseRecyclerAdapter<Events, AdminsEventRecordViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminsEventRecordViewHolder holder, int position, @NonNull final Events model) {

                        holder.txtAdminsEventRecordName.setText("????????????:" + model.geteName() + ",  ");
                        holder.txtAdminsEventRecordDate.setText("????????????:" + model.geteDate());
                        holder.txtAdminsEventRecordPlace.setText("????????????:" + model.getePlace());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               CharSequence options[] = new CharSequence[]
                                        {
                                                "??????????????????","?????????????????????????????????"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminsNewEventRecordActivity.this);
                                builder.setTitle("????????????:");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if(i == 0){
                                            Intent intent = new Intent(AdminsNewEventRecordActivity.this, AdminsNewEventRecordDetailsActivity.class);
                                            intent.putExtra("eid", model.getEid());
                                            intent.putExtra("eMoney", model.geteMoney());
                                            startActivity(intent);
                                        }
                                        if(i==1){
                                            EventRef.child(model.getEid()).removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(AdminsNewEventRecordActivity.this,"?????????????????????????????????????????????",Toast.LENGTH_SHORT).show();
                                                                Intent intent= new Intent(AdminsNewEventRecordActivity.this,AdminCategoryActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                                builder.show(); //????????????

                            }
                        });
                    }


                    @NonNull
                    @Override
                    public AdminsEventRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admins_event_record_items_layout, parent, false);
                        AdminsEventRecordViewHolder holder = new AdminsEventRecordViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
