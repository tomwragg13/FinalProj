package com.example.finalproj;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewerHolder> {
    public MyAdapter(Context context, List<Workouts> items, String email, String date) {
        this.context = context;
        this.items = items;
        this.email = email;
        this.date = date;
    }

    Context context;
    List<Workouts> items;

    String email;
    String date;

    @NonNull
    @Override
    public MyViewerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item, parent, false);
        return new MyViewerHolder(LayoutInflater.from(context).inflate(R.layout.workout_item,parent,false), email, date, items).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewerHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nameView.setText(items.get(position).getName());
        holder.repsView.setText("x"+(Integer.toString(items.get(position).getReps())));
        holder.weightView.setText((Integer.toString(items.get(position).getWeight()))+"Kg");
        holder.setsView.setText("x"+(Integer.toString(items.get(position).getSets())));
        holder.caloriesView.setText((Integer.toString((int) items.get(position).getCalories()))+"Kcal");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



}
