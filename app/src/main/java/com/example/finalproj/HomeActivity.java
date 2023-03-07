package com.example.finalproj;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    TextView nameTag;
    TextView weightTag;
    String email;
    String name;
    int weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        email = new savedData(getApplicationContext()).readStoredEmail();
        name = new savedData(getApplicationContext()).readStoredName();
        weight = Integer.parseInt(new savedData(getApplicationContext()).readStoredWeight());

        nameTag = (TextView) findViewById(R.id.nameTag);
        weightTag = (TextView) findViewById(R.id.weightTag);

        nameTag.setText(name);
        weightTag.setText(weight+ " Kg");
    }

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
}