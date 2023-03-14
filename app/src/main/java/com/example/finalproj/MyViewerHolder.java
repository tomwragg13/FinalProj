package com.example.finalproj;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

public class MyViewerHolder extends RecyclerView.ViewHolder {

    TextView nameView;
    TextView repsView;
    TextView weightView;

    TextView setsView;
    TextView caloriesView;

    ImageView expandButton;

    //Button removeItem;
    private MyAdapter adapter;

    public MyViewerHolder(@NonNull View itemView, String email, String date, List<Workouts> items, Context context, RecyclerView recyclerView, ImageView expandButton, float recyclerHeight) {
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
            //savedData.changeRecyclerSize(context, items, recyclerView);
            savedData.addIfEmpty(recyclerView, context, items, email, date, expandButton, recyclerHeight);
            //recyclerView.animate().translationY(0).setDuration(1000);

            Log.d("l1", String.valueOf(recyclerHeight));
            Log.d("l2", String.valueOf(recyclerView.getY()));
            if(items.size() <5 && items.size() > 0 && recyclerView.getY() != recyclerHeight){
                recyclerView.animate().translationYBy(pxFromDp(context, 75));
                expandButton.animate().translationYBy(pxFromDp(context, 75));

            }
            if(items.size() == 1 ){
                expandButton.animate().rotation(90);
            }

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

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }





}
