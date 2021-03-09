package com.example.index.ViewHolder;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.index.Interface.ItemClickListner;
import com.example.index.R;

import io.paperdb.Paper;

public class ProductRecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductRecordName, txtProductRecordTotalPrice, txtProductRecordQuantity;
    public ItemClickListner listner;

    public ProductRecordViewHolder(@NonNull View itemView) {
        super(itemView);
        txtProductRecordName = (TextView) itemView.findViewById(R.id.product_record_name);
        txtProductRecordTotalPrice = (TextView) itemView.findViewById(R.id.product_record_totalprice);
        txtProductRecordQuantity = (TextView) itemView.findViewById(R.id.product_record_quantity);

    }

    public void setItemClickListner(ItemClickListner listner) {

        this.listner = listner;
    }

    @Override
    public void onClick(View view) {

        listner.onClik(view, getAdapterPosition(), false);
    }
}
