package com.pag.socialz.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pag.socialz.Models.Message;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = MessageViewHolder.class.getSimpleName();

    private Context context;
    private TextView message;
    private ImageView image;

    public MessageViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();

        //message = itemView.findViewById();
    }

    public void bindData(Message message){

    }
}
