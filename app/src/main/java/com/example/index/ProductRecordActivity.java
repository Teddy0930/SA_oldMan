package com.example.index;

import android.os.Bundle;


import com.example.index.Model.ProductRecord;
import com.example.index.Prevalent.Prevalent;
import com.example.index.ViewHolder.ProductRecordViewHolder;
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


public class ProductRecordActivity extends AppCompatActivity {
    private DatabaseReference ProductRecordRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_record);

        ProductRecordRef = FirebaseDatabase.getInstance().getReference().child("buyProductList").child("User  View").child("Users" + Prevalent.currentOnlineUser.getAccount()).child("Products");

        Paper.init(this);

        recyclerView = findViewById(R.id.recycler_menuProduct);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<ProductRecord> options =
                new FirebaseRecyclerOptions.Builder<ProductRecord>()
                        .setQuery(ProductRecordRef, ProductRecord.class)
                        .build();

        FirebaseRecyclerAdapter<ProductRecord, ProductRecordViewHolder> adapter =
                new FirebaseRecyclerAdapter<ProductRecord, ProductRecordViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductRecordViewHolder holder, int position, @NonNull final ProductRecord model) {

                        holder.txtProductRecordName.setText("商品名稱:" + model.getPname());
                        holder.txtProductRecordTotalPrice.setText("總共花費:" + model.getTotalprice() + "$");
                        holder.txtProductRecordQuantity.setText("數量 :" + model.getQuantity());

                    }


                    @NonNull
                    @Override
                    public ProductRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_record_items_layout, parent, false);
                        ProductRecordViewHolder holder = new ProductRecordViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
