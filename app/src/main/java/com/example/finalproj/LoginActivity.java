package com.example.finalproj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    static EditText emailLogin;
    EditText passwordLogin;

    static TextView loginErrorText;

    TextView confirmRemember;

    boolean toggle = false;

    static String name;
    static int weight;

    User user;




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

    private void handeLogin(String returnMessage) {
        String[] messageData = returnMessage.split(",", -1);
        if(Objects.equals(messageData[0], "loginInfo")){
            if(Objects.equals(messageData[1], "pass")){
                Toast.makeText(getApplicationContext(),"Login Successful.",Toast.LENGTH_SHORT).show();
                String email = messageData[2];

                String password = (passwordLogin.getText().toString());
                password = Encrypt.encryptData(password);

                if(toggle){
                    savedData.writeSavedEmail(getApplicationContext(), emailLogin.getText().toString().getBytes());
                }else{
                    savedData.writeSavedEmail(getApplicationContext(), "".getBytes());
                }

                savedData.writeStoredEmail(getApplicationContext(),emailLogin.getText().toString().getBytes());
                savedData.writeNameWeight(getApplicationContext(), emailLogin.getText().toString().getBytes());

                ClientHandler client = new ClientHandler("returnTargets," + emailLogin.getText().toString());
                String[] arr = client.getReturnMessage().split(",");
                ArrayList<String> list = new ArrayList<>();

                for (String s : arr) {
                    list.add(s);
                }

                //Log.d("prot", list.get(1));
                savedData.writeStoredProtein(getApplicationContext(), list.get(0).getBytes());
                savedData.writeStoredCalories(getApplicationContext(), list.get(1).getBytes());

                Intent myInt = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(myInt);
                overridePendingTransition(0,0);

            }else{
                LoginActivity.setError();
            }
        }
    }

    public void signInButton(View view) throws InterruptedException {
        String email = (emailLogin.getText().toString());
        String password = (passwordLogin.getText().toString());
        password = Encrypt.encryptData(password);

        String message = "loginInfo," + email + "," + password;

        ClientHandler client = new ClientHandler(message);
        Log.d("app", client.getReturnMessage());
        handeLogin(client.getReturnMessage());

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