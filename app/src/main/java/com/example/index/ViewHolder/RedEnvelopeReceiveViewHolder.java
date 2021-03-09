package com.example.index.ViewHolder;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.index.Interface.ItemClickListner;
import com.example.index.R;

import org.w3c.dom.Text;

import io.paperdb.Paper;

public class RedEnvelopeReceiveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtRedEnvelopeRecordName, txtRedEnvelopeRecordTotalPrice;
    public ItemClickListner listner;

    public RedEnvelopeReceiveViewHolder(@NonNull View itemView) {
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
