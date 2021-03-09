package com.example.index.ViewHolder;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.index.Interface.ItemClickListner;
import com.example.index.R;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txteventName, txteventDescription;
    public ImageView eImageView;
    public ItemClickListner listner;

    public EventViewHolder(@NonNull View itemView) {
        super(itemView);

        eImageView = (ImageView) itemView.findViewById(R.id.event_image);
        txteventName = (TextView) itemView.findViewById(R.id.event_name);
        txteventDescription = (TextView) itemView.findViewById(R.id.event_description);
    }

    public void setItemClickListner(ItemClickListner listner) {

        this.listner = listner;
    }

    @Override
    public void onClick(View view) {

        listner.onClik(view, getAdapterPosition(), false);
    }
}
