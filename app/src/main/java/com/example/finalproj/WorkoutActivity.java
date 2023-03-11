package com.example.finalproj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import android.widget.LinearLayout.LayoutParams;

public class WorkoutActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Workouts> workouts;
    EditText enterName, enterReps, enterWeight, enterSets;
    MyAdapter adapter;
    String email;
    String name;
    int weight;

    String date;
    TextView dateText, nameText, weightText;
    ConstraintLayout mainPanel;
    ImageView expandButton;

    boolean expandState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        email = new savedData(getApplicationContext()).readStoredEmail();
        name = new savedData(getApplicationContext()).readStoredName();
        weight = Integer.parseInt(new savedData(getApplicationContext()).readStoredWeight());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        expandState = false;

        enterName = (EditText) findViewById(R.id.workoutName);
        enterReps = (EditText) findViewById(R.id.workoutReps);
        enterWeight = (EditText) findViewById(R.id.workoutWeight);
        enterSets = (EditText) findViewById(R.id.workoutSets);
        dateText = (TextView) findViewById(R.id.dateID);
        nameText = (TextView) findViewById(R.id.workoutNameTag);
        weightText = (TextView) findViewById(R.id.workoutWeightTag);
        mainPanel = (ConstraintLayout) findViewById(R.id.mainPanel);
        expandButton = (ImageView) findViewById(R.id.expandButton);

        nameText.setText(name);
        weightText.setText(String.valueOf(weight + " Kg"));

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        date = sdf.format(new Date());
        date = DateHandler.getDate("null", date)[0];
        dateText.setText(DateHandler.getDate("null", date)[1]);

        loadWorkouts();

        savedData.addIfEmpty(recyclerView, getApplicationContext(), workouts, email, date, expandButton);

        onWindowFocusChanged(true);




    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
    public void expandView(View view){
        Log.d("state", String.valueOf(expandState));

        int expandNum = 4;
        int duration = 500;
        if(workouts.size() != 1){
            //savedData.changeRecyclerSize(getApplicationContext(), workouts, recyclerView);

            if(!Objects.equals(workouts.get(0).getName(), "Add Workouts")){
                Log.d("pass", String.valueOf(workouts.size()));
                float px = 0;
                if(!expandState){
                    if(workouts.size()>expandNum){
                        px = pxFromDp(getApplicationContext(), -(75*(expandNum-1)));
                    }
                    if(workouts.size() >1 && workouts.size() <=expandNum){
                        px = pxFromDp(getApplicationContext(), -(75*workouts.size()-75));

                    }
                    expandButton.animate().rotationBy(180);
                    expandState = true;
                }else{
                    px = 0;
                    expandButton.animate().rotationBy(180);
                    expandState = false;
                }

                expandButton.animate().translationY(px).setDuration(duration);
                recyclerView.animate().translationY(px).setDuration(duration);

                recyclerView.setElevation(1000);
                expandButton.setElevation(1000);

            }
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //int px = mainPanel.getHeight();

        //DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        //int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));

        //Log.d("hi", String.valueOf(dp));

        //dp -= 120;
        //dp -= 75;

        //Log.d("hi", String.valueOf(dp));

        //float px = pxFromDp(getApplicationContext(), (75*workouts.size())-75);

        //recyclerView.animate().translationY(-px).setDuration(100000);
    }

    private void loadWorkouts(){
        workouts = new ArrayList<Workouts>();

        ClientHandler client = new ClientHandler("workoutRead," + email + "," + date);
        String[] messageData = client.getReturnMessage().split(",", -1);
        Log.d("ret", client.getReturnMessage());

        ArrayList<String> strList = new ArrayList<String>(
                Arrays.asList(messageData));
        strList.remove(0);

        int items = strList.size()/5;

        if(strList.size() >0){
            Log.d("items", strList.get(0));
            for (int i = 0; i < items; i++) {
                workouts.add(new Workouts(strList.get(5 * i), Integer.parseInt(strList.get(3+(5*i))), Integer.parseInt(strList.get(1+(5*i))), Integer.parseInt(strList.get(2+(5*i))), Double.parseDouble(strList.get(4+(5*i))), date));
            }
        }


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAdapter(getApplicationContext(), workouts, email, date, recyclerView, expandButton);
        recyclerView.setAdapter(adapter);

        //savedData.changeRecyclerSize(getApplicationContext(), workouts, recyclerView);
    }

    @Override
    public void onBackPressed() {
        Intent myInt = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(myInt);
        overridePendingTransition(0,R.anim.slide_out_right);
    }

    public void goBack(View view) {
        Intent myInt = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(myInt);
        overridePendingTransition(0,R.anim.slide_out_right);
    }


    public void addWorkout(View view){
        if(workouts.size() == 1 && Objects.equals(workouts.get(0).getName(), "Add Workouts")){
            workouts.remove(0);
        }

        workouts.add(0,new Workouts(enterName.getText().toString(), Integer.parseInt(enterReps.getText().toString()), Integer.parseInt(enterWeight.getText().toString()), Integer.parseInt(enterSets.getText().toString()), weight, date));
        savedData.saveChanges(email, date, workouts);
        adapter.notifyItemInserted(0);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAdapter(getApplicationContext(), workouts, email, date, recyclerView, expandButton);
        recyclerView.setAdapter(adapter);

        loadWorkouts();


    }

    public void nextButton(View view){
        String[] dateData = DateHandler.getDate( "next", date);
        date = dateData[0];
        dateText.setText(dateData[1]);
        loadWorkouts();

        savedData.addIfEmpty(recyclerView, getApplicationContext(), workouts, email, date, expandButton);
    }

    public void backButton(View view){
        String[] dateData = DateHandler.getDate( "back", date);
        date = dateData[0];
        dateText.setText(dateData[1]);
        loadWorkouts();

        savedData.addIfEmpty(recyclerView, getApplicationContext(), workouts, email, date, expandButton);
    }

}