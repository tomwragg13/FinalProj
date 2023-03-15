package com.example.finalproj;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class workoutAdapter extends RecyclerView.Adapter<workoutViewHolder> {

    public workoutAdapter(Context context, List<Workouts> items, String email, String date, RecyclerView recyclerView, ImageView expandButton, float recyclerHeight,
                          List<personalBests> PBs, String expandType, ConstraintLayout clickBlocker) {
        this.context = context;
        this.items = items;
        this.email = email;
        this.date = date;
        this.recyclerView = recyclerView;
        this.expandButton = expandButton;
        this.recyclerHeight = recyclerHeight;
        this.PBs = PBs;
        this.expandType = expandType;
        this.clickBlocker = clickBlocker;
    }

    Context context;
    List<Workouts> items;
    List<personalBests> PBs;

    String email;
    String date, expandType;

    RecyclerView recyclerView;
    ImageView expandButton;

    float recyclerHeight;

    ConstraintLayout clickBlocker;


    @NonNull
    @Override
    public workoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new workoutViewHolder(LayoutInflater.from(context).inflate(R.layout.workout_item,parent,false), email, date, items, context,
                recyclerView, expandButton, recyclerHeight, expandType, PBs, clickBlocker).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull workoutViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nameView.setText(items.get(position).getName()+":");
        holder.repsView.setText("Reps: x"+(Integer.toString(items.get(position).getReps())));
        holder.weightView.setText((Integer.toString(items.get(position).getWeight()))+" Kg");
        holder.setsView.setText("Sets: x"+(Integer.toString(items.get(position).getSets())));
        holder.caloriesView.setText("-"+(Integer.toString((int) items.get(position).getCalories()))+" kcal");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



}
