package com.example.finalproj;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    TextView nameTag;
    TextView weightTag;
    String email;
    String name;
    int weight;
    String date;
    TextView dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        email = new savedData(getApplicationContext()).readStoredEmail();
        name = new savedData(getApplicationContext()).readStoredName();
        weight = Integer.parseInt(new savedData(getApplicationContext()).readStoredWeight());

        nameTag = (TextView) findViewById(R.id.nameTag);
        weightTag = (TextView) findViewById(R.id.weightTag);
        dateText = (TextView) findViewById(R.id.dateTag);

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        date = sdf.format(new Date());
        date = DateHandler.getDate("null", date)[0];
        dateText.setText(DateHandler.getDate("null", date)[1]);

        nameTag.setText(name);
        weightTag.setText(weight+ " Kg");

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"back",Toast.LENGTH_SHORT).show();
    }

    public void logOutAction(View view){
        Intent myInt = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(myInt);
        overridePendingTransition(0,0);

        savedData.writeSavedEmail(getApplicationContext(), "".getBytes());
        savedData.writeStoredEmail(getApplicationContext(), "".getBytes());
        savedData.writeNameWeight(getApplicationContext(), "".getBytes());
    }

    public void openWorkouts(View view){
        Intent myInt = new Intent(getApplicationContext(), WorkoutActivity.class);
        startActivity(myInt);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    public void forwardDate(View view){
        String[] dateData = DateHandler.getDate( "next", date);
        date = dateData[0];
        dateText.setText(dateData[1]);
    }

    public void backwardsDate(View view){
        String[] dateData = DateHandler.getDate( "back", date);
        date = dateData[0];
        dateText.setText(dateData[1]);
    }
}