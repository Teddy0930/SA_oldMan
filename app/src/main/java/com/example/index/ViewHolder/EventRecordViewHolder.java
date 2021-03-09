package com.example.index.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.index.Interface.ItemClickListner;
import com.example.index.R;

import io.paperdb.Paper;

public class EventRecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtEventRecordName, txtEventRecordDate,txtEventRecordPlace,txtEventRecordStatus;

    public ItemClickListner listner;

    public EventRecordViewHolder(@NonNull View itemView) {
        super(itemView);
        txtEventRecordName = (TextView) itemView.findViewById(R.id.event_record_name);
        txtEventRecordDate = (TextView) itemView.findViewById(R.id.event_record_date);
        txtEventRecordPlace = (TextView) itemView.findViewById(R.id.event_record_place);
        txtEventRecordStatus =(TextView) itemView.findViewById(R.id.event_record_status);
    }

    public void setItemClickListner(ItemClickListner listner) {

        this.listner = listner;
    }

    @Override
    public void onClick(View view) {

        listner.onClik(view, getAdapterPosition(), false);
    }
}
