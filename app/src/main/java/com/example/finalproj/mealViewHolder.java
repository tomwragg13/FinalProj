package com.example.finalproj;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class mealViewHolder extends RecyclerView.ViewHolder {

    TextView nameView, mealCalories, mealProtein;

    private mealAdapter adapter;

    public mealViewHolder(@NonNull View itemView) {
        super(itemView);



        nameView = itemView.findViewById(R.id.mealName);
        mealCalories = itemView.findViewById(R.id.mealCaloriesView);
        mealProtein = itemView.findViewById(R.id.mealProteinView);

    }
    public mealViewHolder linkAdapter(mealAdapter adapter){
        this.adapter = adapter;
        return this;
    }






}
