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

public class AdminsProductRecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtAdminsProductRecordName, txtAdminsProductRecordTotalPrice, txtAdminsProductRecordQuantity,txtAdminsProductRecordUser;
    public ItemClickListner listner;

    public AdminsProductRecordViewHolder(@NonNull View itemView) {
        super(itemView);
        txtAdminsProductRecordName = (TextView) itemView.findViewById(R.id.admins_product_record_name);
        txtAdminsProductRecordTotalPrice = (TextView) itemView.findViewById(R.id.admins_product_record_totalprice);
        txtAdminsProductRecordQuantity = (TextView) itemView.findViewById(R.id.admins_product_record_quantity);
        txtAdminsProductRecordUser= (TextView) itemView.findViewById(R.id.admins_product_record_user);

    }

    public void setItemClickListner(ItemClickListner listner) {

        this.listner = listner;
    }

    @Override
    public void onClick(View view) {

        listner.onClik(view, getAdapterPosition(), false);
    }
}
