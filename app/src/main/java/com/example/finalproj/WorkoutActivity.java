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
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.util.ArrayList;
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
    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
    String date;
    TextView dateText;

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


        date = sdf.format(new Date());
        getDate("null");
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

    public String getDate(String direction){

        int day = Integer.parseInt(date.substring(0,2));
        int month = Integer.parseInt(date.substring(2,4));
        int year = Integer.parseInt(date.substring(4,8));

        boolean addMonth = false;

        if(Objects.equals(direction, "next")){
            day += 1;
            if(addMonth == false){
                if(month == 1 || month == 3 ||month == 5 || month == 7 || month == 8 || month == 10 || month == 12){
                    addMonth = true;
                    if(day == 32){
                        month+= 1;
                        day = 1;
                    }
                }
            }
            if(addMonth == false){
                if(month == 4 || month == 6 || month == 9 || month == 11){
                    addMonth = true;
                    if(day == 31){
                        month +=1;
                        day = 1;
                    }
                }
            }
            if(addMonth == false){
                if(month == 2){
                    addMonth = true;
                    if(day == 29){
                        month +=1;
                        day = 1;
                    }
                }
            }

            if(month == 13){
                year +=1;
                day = 1;
                month = 1;
            }
        }
        if(Objects.equals(direction, "back")){
            day -= 1;
            if(addMonth == false){
                if(month == 1 || month == 2 || month == 4 || month == 6 || month == 8 || month == 9 || month == 11){
                    addMonth = true;
                    if(day == 0){
                        month -= 1;
                        day = 31;
                    }
                }
            }
            if(addMonth == false){
                if(month == 5 || month == 7 || month == 10 || month == 12){
                    addMonth = true;
                    if(day == 0){
                        month -= 1;
                        day = 30;
                    }
                }
            }
            if(addMonth == false){
                addMonth = true;
                if(month == 3){
                    if(day == 0){
                        month -= 1;
                        day = 28;
                    }
                }
            }

            if(month == 0){
                year -=1;
                day = 31;
                month = 12;
            }
        }


        String dayS = String.valueOf(day);
        String monthS = String.valueOf(month);
        if(String.valueOf(day).length() == 1){
            dayS = String.valueOf(0) + dayS;
        }
        if(String.valueOf(month).length() == 1){
            monthS = String.valueOf(0) + monthS;
        }

        date = dayS + monthS + String.valueOf(year);
        dateText.setText(dayS + "/" + monthS + "/" + String.valueOf(year));

        Log.d("date", String.valueOf(day) + String.valueOf(month) + String.valueOf(year));



        return null;
    }

    public void addWorkout(View view){
        workouts.add(0,new Workouts(enterName.getText().toString(), Integer.parseInt(enterReps.getText().toString()), Integer.parseInt(enterWeight.getText().toString()), Integer.parseInt(enterSets.getText().toString()), weight));
        adapter.notifyItemInserted(0);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAdapter(getApplicationContext(), workouts);
        recyclerView.setAdapter(adapter);


        //ClientHandler client = new ClientHandler("userData tom5 tom6 " + savedEmail + " pass 10");
        //client.getReturnMessage();
    }

    public void nextButton(View view){
        getDate( "next");
    }

    public void backButton(View view){
        getDate( "back");
    }
}