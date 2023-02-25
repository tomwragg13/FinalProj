package com.example.finalproj;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewerHolder extends RecyclerView.ViewHolder {

    TextView nameView;
    TextView repsView;
    TextView weightView;

    TextView setsView;
    TextView caloriesView;

    //Button removeItem;
    private MyAdapter adapter;

    public MyViewerHolder(@NonNull View itemView) {
        super(itemView);
        nameView = itemView.findViewById(R.id.name);
        repsView = itemView.findViewById(R.id.reps);
        weightView = itemView.findViewById(R.id.weight);
        setsView = itemView.findViewById(R.id.sets);
        caloriesView = itemView.findViewById(R.id.calories);
        itemView.findViewById(R.id.removeButton).setOnClickListener(view -> {
            adapter.items.remove(getAdapterPosition());
            adapter.notifyItemRemoved(getAdapterPosition());
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
