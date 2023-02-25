package com.example.finalproj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    static EditText emailLogin;
    EditText passwordLogin;

    static TextView loginErrorText;

    TextView confirmRemember;

    boolean toggle = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailLogin = (EditText) findViewById(R.id.emailLogin);
        passwordLogin = (EditText) findViewById(R.id.passwordLogin);
        loginErrorText = (TextView) findViewById(R.id.loginError);
        confirmRemember = (TextView) findViewById(R.id.confirmRemember);


    }


    public void openSignUp(View view){
        Intent myInt = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(myInt);
        overridePendingTransition(0,0);
    }

    @Override
    public void onBackPressed() {
        Intent myInt = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(myInt);
        overridePendingTransition(0,0);
    }


    public void signInButton(View view) throws InterruptedException {
        String email = (emailLogin.getText().toString());
        String password = (passwordLogin.getText().toString());
        password = Encrypt.encryptData(password);
        String message = "loginInfo " + email + " " + password;
        Client client = new Client();
        client.execute(message);
        //save email for remember me
        if(toggle){
            try {
                FileOutputStream outputStream = openFileOutput("saveEmail.txt", Context.MODE_PRIVATE);
                outputStream.write(emailLogin.getText().toString().getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            try {
                FileOutputStream outputStream = openFileOutput("saveEmail.txt", Context.MODE_PRIVATE);
                outputStream.write("".getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //save email for email storage
        try {
            FileOutputStream outputStream = openFileOutput("Email.txt", Context.MODE_PRIVATE);
            outputStream.write(emailLogin.getText().toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onSwitchClick(View view){
        //Toast.makeText(getApplicationContext(),csvStorage.read(),Toast.LENGTH_SHORT).show();

        if(!toggle){
            toggle = true;
            confirmRemember.setText("✓");
            confirmRemember.setTextColor(Color.GREEN);
        }else{
            toggle = false;
            confirmRemember.setText("✘");
            confirmRemember.setTextColor(Color.RED);
        }


    }

    public static void setError(){
        loginErrorText.setText("Email or Password is Incorrect");
        loginErrorText.setTextColor(Color.RED);
    }
    public static void setPass(){
        loginErrorText.setText("✓");
        loginErrorText.setTextColor(Color.GREEN);
        //csvStorage.saveName(emailLogin.getText().toString());

    }

}