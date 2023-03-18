package com.example.finalproj;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class mealAdapter extends RecyclerView.Adapter<mealViewHolder> {

    public mealAdapter(Context context, List<productManager> food) {
        this.context = context;
        this.food = food;
    }

    Context context;
    List<productManager> food;


    @NonNull
    @Override
    public mealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item, parent, false);
        return new mealViewHolder(LayoutInflater.from(context).inflate(R.layout.meal_item,parent,false)).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull mealViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //String date = (food.get(position).getDate());
        //StringBuilder output = new StringBuilder(date);
        //output.insert(2, "/");
        //output.insert(5, "/");
        //holder.nameView.setText(items.get(position).getName()+":");
        //holder.weightView.setText((Integer.toString(items.get(position).getWeight()))+" Kg");
        //holder.dateView.setText(output.toString());
        holder.nameView.setText(food.get(position).getName());
        holder.mealCalories.setText(Integer.toString( (int )food.get(position).getTotalCalories()));
        holder.mealProtein.setText(Double.toString(food.get(position).getTotalProtein()));
    }

    @Override
    public int getItemCount() {
        return food.size();
    }



}
