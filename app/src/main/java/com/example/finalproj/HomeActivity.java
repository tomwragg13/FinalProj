package com.example.finalproj;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    TextView nameTag;
    TextView weightTag;
    String email;
    String name;
    int weight;
    String date;
    TextView dateText;
    EditText weightText, caloriesText, proteinText;

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
        weightText = findViewById(R.id.weightTextBox);
        caloriesText = findViewById(R.id.caloriesText);
        proteinText = findViewById(R.id.proteinText);

        weightText.setText(String.valueOf(weight));
        proteinText.setText(savedData.readStoredProtein(getApplicationContext()));
        caloriesText.setText(savedData.readStoredCalories(getApplicationContext()));

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        date = sdf.format(new Date());
        date = DateHandler.getDate("null", date)[0];
        dateText.setText(DateHandler.getDate("null", date)[1]);

        nameTag.setText(name);
        weightTag.setText(weight+ " Kg");

    }

    public void setProtein(View view){
        ClientHandler clientHandler = new ClientHandler("updateProtein," + email+ "," + proteinText.getText().toString());
        Toast.makeText(getApplicationContext(),"Protein Target Updated",Toast.LENGTH_SHORT).show();
        savedData.writeStoredProtein(getApplicationContext(), proteinText.getText().toString().getBytes());
    }
    public void setCalories(View view){
        ClientHandler clientHandler = new ClientHandler("updateCalories," + email+ "," + caloriesText.getText().toString());
        Toast.makeText(getApplicationContext(),"Calorie Target Updated",Toast.LENGTH_SHORT).show();
        savedData.writeStoredCalories(getApplicationContext(), caloriesText.getText().toString().getBytes());
    }

    public void setWeight(View view){
        weightTag.setText(weightText.getText().toString() + " Kg");
        ClientHandler clientHandler = new ClientHandler("updateWeight," + email+ "," + weightText.getText().toString());
        savedData.writeNameWeight(getApplicationContext(), email.getBytes(StandardCharsets.UTF_8));
        Toast.makeText(getApplicationContext(),"Weight Updated",Toast.LENGTH_SHORT).show();
        weightText.setText("");
    }

    public void openMacros(View view){
        Intent myInt = new Intent(getApplicationContext(), MacrosActivity.class);
        startActivity(myInt);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
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