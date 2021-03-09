package com.example.index;

import android.os.Bundle;


import com.example.index.Model.AdminsNewProductRecord;
import com.example.index.Prevalent.Prevalent;
import com.example.index.ViewHolder.AdminsProductRecordViewHolder;
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


public class AdminsNewProductRecordActivity extends AppCompatActivity {
    private DatabaseReference ProductRecordRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admins_new_product_record);

        ProductRecordRef = FirebaseDatabase.getInstance().getReference().child("buyProductList").child("Admin  View").child("Admins" + Prevalent.currentOnlineAdmin.getAccount()).child("Products");

        Paper.init(this);

        recyclerView = findViewById(R.id.recycler_menuAdminProduct);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<AdminsNewProductRecord> options =
                new FirebaseRecyclerOptions.Builder<AdminsNewProductRecord>()
                        .setQuery(ProductRecordRef, AdminsNewProductRecord.class)
                        .build();

        FirebaseRecyclerAdapter<AdminsNewProductRecord, AdminsProductRecordViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminsNewProductRecord, AdminsProductRecordViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminsProductRecordViewHolder holder, int position, @NonNull final AdminsNewProductRecord model) {

                        holder.txtAdminsProductRecordName.setText("商品名稱:" + model.getPname());
                        holder.txtAdminsProductRecordTotalPrice.setText("總共花費:" + model.getTotalprice() + "$");
                        holder.txtAdminsProductRecordQuantity.setText("數量 :" + model.getQuantity());
                        holder.txtAdminsProductRecordUser.setText("兌換會員 :" + model.getUserName());

                    }


                    @NonNull
                    @Override
                    public AdminsProductRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_product_record_items_layout, parent, false);
                        AdminsProductRecordViewHolder holder = new AdminsProductRecordViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
