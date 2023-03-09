package com.example.finalproj;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyViewerHolder extends RecyclerView.ViewHolder {

    TextView nameView;
    TextView repsView;
    TextView weightView;

    TextView setsView;
    TextView caloriesView;

    //Button removeItem;
    private MyAdapter adapter;

    public MyViewerHolder(@NonNull View itemView, String email, String date, List<Workouts> items) {
        super(itemView);



        nameView = itemView.findViewById(R.id.name);
        repsView = itemView.findViewById(R.id.reps);
        weightView = itemView.findViewById(R.id.weight);
        setsView = itemView.findViewById(R.id.sets);
        caloriesView = itemView.findViewById(R.id.calories);
        itemView.findViewById(R.id.removeButton).setOnClickListener(view -> {
            adapter.items.remove(getAdapterPosition());
            adapter.notifyItemRemoved(getAdapterPosition());
            savedData.saveChanges(email, date, items);


        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("demo", ""+getAdapterPosition());

            }
        });
        
    }
    public MyViewerHolder linkAdapter(MyAdapter adapter){
        this.adapter = adapter;
        return this;
    }





}
