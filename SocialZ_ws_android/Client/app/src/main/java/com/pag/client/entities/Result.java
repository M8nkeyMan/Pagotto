package com.pag.client.entities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pag.client.R;

import java.util.ArrayList;

public class Result {

    private int id;
    private String text_main, text_secondary;

    public Result() {
    }

    public Result(int id, String text_main, String text_secondary) {
        this.id = id;
        this.text_main = text_main;
        this.text_secondary = text_secondary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText_main() {
        return text_main;
    }

    public void setText_main(String text_main) {
        this.text_main = text_main;
    }

    public String getText_secondary() {
        return text_secondary;
    }

    public void setText_secondary(String text_secondary) {
        this.text_secondary = text_secondary;
    }

    public static class Adapter extends ArrayAdapter<Result> {

        private class ViewHolder {
            private TextView itemView_text_main;
            private TextView itemView_text_secondary;
        }

        public Adapter(Context context, int textViewResourceId, ArrayList<Result> items) {
            super(context, textViewResourceId, items);
        }

        @NonNull
        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        private View getCustomView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.listitem_layout, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.itemView_text_main = convertView.findViewById(R.id.text_main);
                viewHolder.itemView_text_secondary = convertView.findViewById(R.id.text_secondary);
                convertView.setTag(viewHolder);
            } else viewHolder = (ViewHolder) convertView.getTag();
            Result item = getItem(position);
            if (item != null) {
                viewHolder.itemView_text_main.setText(item.text_main);
                viewHolder.itemView_text_secondary.setText(item.text_secondary);
            }
            return convertView;
        }
    }
}
