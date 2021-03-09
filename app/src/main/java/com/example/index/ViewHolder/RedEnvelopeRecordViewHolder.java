package com.example.index.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.index.Interface.ItemClickListner;
import com.example.index.R;

public class RedEnvelopeRecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtRedEnvelopeRecordName, txtRedEnvelopeRecordTotalPrice;
    public ItemClickListner listner;

    public RedEnvelopeRecordViewHolder(@NonNull View itemView) {
        super(itemView);

        txtRedEnvelopeRecordName = (TextView) itemView.findViewById(R.id.red_envelope_record_name);
        txtRedEnvelopeRecordTotalPrice = (TextView) itemView.findViewById(R.id.red_envelope_record_totalprice);
    }

    public void setItemClickListner(ItemClickListner listner) {

        this.listner = listner;
    }

    @Override
    public void onClick(View view) {

        listner.onClik(view, getAdapterPosition(), false);
    }
}
