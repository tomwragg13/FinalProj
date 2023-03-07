package com.example.finalproj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Workouts> workouts;
    EditText enterName, enterReps, enterWeight, enterSets;
    MyAdapter adapter;
    String email;
    String name;
    int weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        email = new savedData(getApplicationContext()).readStoredEmail();
        name = new savedData(getApplicationContext()).readStoredName();
        weight = Integer.parseInt(new savedData(getApplicationContext()).readStoredWeight());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        enterName = (EditText) findViewById(R.id.workoutName);
        enterReps = (EditText) findViewById(R.id.workoutReps);
        enterWeight = (EditText) findViewById(R.id.workoutWeight);
        enterSets = (EditText) findViewById(R.id.workoutSets);

        workouts = new ArrayList<Workouts>();
        workouts.add(new Workouts("Bench Press", 8, 45, 4, weight));
        workouts.add(new Workouts("Bicep Curl", 8, 10, 4, weight));

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAdapter(getApplicationContext(), workouts);
        recyclerView.setAdapter(adapter);
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
        workouts.add(0,new Workouts(enterName.getText().toString(), Integer.parseInt(enterReps.getText().toString()), Integer.parseInt(enterWeight.getText().toString()), Integer.parseInt(enterSets.getText().toString()), weight));
        adapter.notifyItemInserted(0);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAdapter(getApplicationContext(), workouts);
        recyclerView.setAdapter(adapter);
    }
}