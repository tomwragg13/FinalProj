package com.example.finalproj;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class personalBestAdapter extends RecyclerView.Adapter<personalBestViewHolder> {

    public personalBestAdapter(Context context, List<personalBests> items) {
        this.context = context;
        this.items = items;
    }

    Context context;
    List<personalBests> items;


    @NonNull
    @Override
    public personalBestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item, parent, false);
        return new personalBestViewHolder(LayoutInflater.from(context).inflate(R.layout.pb_item,parent,false)).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull personalBestViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String date = (items.get(position).getDate());
        StringBuilder output = new StringBuilder(date);
        output.insert(2, "/");
        output.insert(5, "/");
        holder.nameView.setText(items.get(position).getName()+":");
        holder.weightView.setText((Integer.toString(items.get(position).getWeight()))+" Kg");
        holder.dateView.setText(output.toString());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



}
