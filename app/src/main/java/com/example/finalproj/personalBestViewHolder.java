package com.example.finalproj;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class personalBestViewHolder extends RecyclerView.ViewHolder {

    TextView nameView;
    TextView weightView;

    TextView dateView;
    private personalBestAdapter adapter;

    public personalBestViewHolder(@NonNull View itemView) {
        super(itemView);



        nameView = itemView.findViewById(R.id.Name);
        weightView = itemView.findViewById(R.id.Weight);
        dateView = itemView.findViewById(R.id.dateView);

    }
    public personalBestViewHolder linkAdapter(personalBestAdapter adapter){
        this.adapter = adapter;
        return this;
    }






}
