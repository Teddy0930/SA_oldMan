package com.example.index.ViewHolder;

import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.index.Interface.ItemClickListner;
import com.example.index.R;


public class AdminsNewEventRecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtAdminsEventRecordUser, txtAdminsEventRecordDate,txtAdminsEventRecordPlace,txtAdminsEventRecordStatus;

    public ItemClickListner listner;


    public AdminsNewEventRecordViewHolder(@NonNull View itemView) {
        super(itemView);
        txtAdminsEventRecordDate = (TextView) itemView.findViewById(R.id.admins_event_record_date);
        txtAdminsEventRecordPlace = (TextView) itemView.findViewById(R.id.admins_event_record_place);
        txtAdminsEventRecordStatus = (TextView) itemView.findViewById(R.id.admins_event_record_status);
        txtAdminsEventRecordUser = (TextView) itemView.findViewById(R.id.admins_event_record_user);
    }

    public void setItemClickListner(ItemClickListner listner) {

        this.listner = listner;
    }

    @Override
    public void onClick(View view) {

        listner.onClik(view, getAdapterPosition(), false);
    }
}
