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
    String name;
    int weight;


    public void fetchUserData(String email){
        ClientHandler client = new ClientHandler("fetchData " + email);
        Log.d("app",client.getReturnMessage());
        String[] messageData = client.getReturnMessage().split(" ", -1);
        this.name = messageData[0];
        try {
            this.weight = Integer.parseInt(messageData[1]);
        }catch (Exception ignored){}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String storedEmail = null;
        try {
            FileInputStream inputStream = openFileInput("Email.txt"); // replace "filename.txt" with the name of the file where you stored the word
            int length = inputStream.available();
            byte[] buffer = new byte[length];
            inputStream.read(buffer);
            storedEmail = new String(buffer);
            inputStream.close();

            // do something with the word, for example, log it
        } catch (Exception e) {
            e.printStackTrace();
        }

        fetchUserData(storedEmail);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        enterName = (EditText) findViewById(R.id.workoutName);
        enterReps = (EditText) findViewById(R.id.workoutReps);
        enterWeight = (EditText) findViewById(R.id.workoutWeight);
        enterSets = (EditText) findViewById(R.id.workoutSets);

        workouts = new ArrayList<Workouts>();
        workouts.add(new Workouts("Bench Press", 8, 45, 4, weight));

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter = new MyAdapter(getApplicationContext(), workouts);
        recyclerView.setAdapter(adapter);

        String email = MainActivity.getUser().getEmail();
        String name = MainActivity.getUser().getName();
        int weight = MainActivity.getUser().getWeight();
        Toast.makeText(getApplicationContext(),email + " " + name + " " + weight,Toast.LENGTH_SHORT).show();

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