package com.example.finalproj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class HomeActivity extends AppCompatActivity {

    TextView nameTag;
    TextView weightTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        MainActivity.fetchUserName(storedEmail);

        nameTag = (TextView) findViewById(R.id.nameTag);
        weightTag = (TextView) findViewById(R.id.weightTag);

        String name = MainActivity.getUser().getName();
        int weight = MainActivity.getUser().getWeight();
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

        try {
            FileOutputStream outputStream = openFileOutput("saveEmail.txt", Context.MODE_PRIVATE);
            outputStream.write("".getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream outputStream = openFileOutput("Email.txt", Context.MODE_PRIVATE);
            outputStream.write("".getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openWorkouts(View view){
        Intent myInt = new Intent(getApplicationContext(), WorkoutActivity.class);
        startActivity(myInt);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
}