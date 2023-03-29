package com.example.finalproj;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

public class workoutViewHolder extends RecyclerView.ViewHolder {

    TextView nameView;
    TextView repsView;
    TextView weightView;

    TextView setsView;
    TextView caloriesView;

    ImageView expandButton;

    //Button removeItem;
    private workoutAdapter adapter;

    boolean expandButtonView = true;

    public workoutViewHolder(@NonNull View itemView, String email, String date, List<Workouts> workouts, Context context, RecyclerView recyclerView, ImageView expandButton, float recyclerHeight,
                             String expandType, List<personalBests> finalPBs, ConstraintLayout clickBlocker, ConstraintLayout recyclerHeader) {
        super(itemView);
        Log.d("AIE", String.valueOf(recyclerHeight));



        nameView = itemView.findViewById(R.id.Name);
        repsView = itemView.findViewById(R.id.reps);
        weightView = itemView.findViewById(R.id.Weight);
        setsView = itemView.findViewById(R.id.sets);
        caloriesView = itemView.findViewById(R.id.calories);
        itemView.findViewById(R.id.removeButton).setOnClickListener(view -> {
            adapter.items.remove(getAdapterPosition());
            adapter.notifyItemRemoved(getAdapterPosition());
            savedData.saveChanges(email, date, workouts);
            //savedData.changeRecyclerSize(context, items, recyclerView);
            savedData.addIfEmpty(recyclerView, context, workouts, email, date, expandButton, recyclerHeight, finalPBs, expandType, clickBlocker, recyclerHeader);
            //recyclerView.animate().translationY(0).setDuration(1000);

            Log.d("l1", String.valueOf(recyclerHeight));
            Log.d("l2", String.valueOf(recyclerView.getY()));
            if(workouts.size() <5 && workouts.size() > 0 && recyclerView.getY() != recyclerHeight){
                recyclerView.animate().translationYBy(pxFromDp(context, 75));
                expandButton.animate().translationYBy(pxFromDp(context, 75));
                recyclerHeader.animate().translationYBy(pxFromDp(context, 75));
            }

            if(workouts.size() == 1 ){
                expandButton.animate().rotation(90);
                clickBlocker.animate().y(20000).setDuration(1);

                if(workouts.size() < 2 && Objects.equals(expandType, "workouts") && expandButtonView){
                    Animation fadeout = new AlphaAnimation(1.f, 0.f);
                    fadeout.setDuration(500);
                    expandButton.startAnimation(fadeout);
                    expandButton.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            expandButton.setVisibility(View.GONE);
                            expandButtonView = false;
                        }
                    }, 500);
                }
                if(workouts.size() >= 2 && !expandButtonView){
                    Animation fadeout = new AlphaAnimation(0.f, 1.f);
                    fadeout.setDuration(500);
                    expandButton.startAnimation(fadeout);
                    expandButton.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            expandButton.setVisibility(View.VISIBLE);
                            expandButtonView = true;
                        }
                    }, 500);
                }
                if(workouts.size() < 2 && Objects.equals(expandType, "workouts") && !expandButtonView){
                    expandButton.setVisibility(View.GONE);
                    expandButtonView = false;
                }

            }

        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("demo", ""+getAdapterPosition());

            }
        });
        
    }
    public workoutViewHolder linkAdapter(workoutAdapter adapter){
        this.adapter = adapter;
        return this;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }





}
