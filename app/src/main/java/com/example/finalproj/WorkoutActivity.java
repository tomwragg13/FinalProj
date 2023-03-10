package com.example.finalproj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    ScrollView scrollView;

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
        dateText = (TextView) findViewById(R.id.dateID);
        nameText = (TextView) findViewById(R.id.workoutNameTag);
        weightText = (TextView) findViewById(R.id.workoutWeightTag);
        scrollView = (ScrollView) findViewById(R.id.scroller);

        nameText.setText(name);
        weightText.setText(String.valueOf(weight + " Kg"));

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        date = sdf.format(new Date());
        date = DateHandler.getDate("null", date)[0];
        dateText.setText(DateHandler.getDate("null", date)[1]);

        loadWorkouts();

        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (75 * scale + 0.5f);
        int imgPixels = (int) (32 * scale + 0.5f);
        Log.d("pix", String.valueOf(imgPixels-pixels));
        recyclerView.getLayoutParams().height = workouts.size()*pixels;
        Log.d("d", String.valueOf(scrollView.getChildAt(0).getHeight()));


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

        adapter = new MyAdapter(getApplicationContext(), workouts, email, date);
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

        workouts.add(0,new Workouts(enterName.getText().toString(), Integer.parseInt(enterReps.getText().toString()), Integer.parseInt(enterWeight.getText().toString()), Integer.parseInt(enterSets.getText().toString()), weight, date));
        savedData.saveChanges(email, date, workouts);
        adapter.notifyItemInserted(0);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAdapter(getApplicationContext(), workouts, email, date);
        recyclerView.setAdapter(adapter);

        loadWorkouts();


    }

    public void nextButton(View view){
        String[] dateData = DateHandler.getDate( "next", date);
        date = dateData[0];
        dateText.setText(dateData[1]);
        loadWorkouts();
    }

    public void backButton(View view){
        String[] dateData = DateHandler.getDate( "back", date);
        date = dateData[0];
        dateText.setText(dateData[1]);
        loadWorkouts();
    }
}