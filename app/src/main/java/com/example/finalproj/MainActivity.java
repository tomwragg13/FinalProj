package com.example.finalproj;

import androidx.appcompat.app.AppCompatActivity;

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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText firstNameText;
    EditText secondNameText;
    EditText emailText;
    EditText passwordText;

    EditText confirmPasswordText;

    //String message;

    String pattern = "^[a-zA-Z]{1,16}\\s*$";

    Pattern p = Pattern.compile(pattern);

    TextView nameErrorText;
    TextView emailWarning;

    Thread myThread;

    public static User getUser() {
        return user;
    }

    static User user;

    public static void setName(String name) {
        MainActivity.name = name;
    }

    static String name;
    static int weight;


    boolean nameValid = false;
    boolean emailValid = false;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public void onBackPressed() {
    }

    public static void fetchUserData(String email){
        ClientHandler client = new ClientHandler("fetchData " + email);
        Log.d("app",client.getReturnMessage());
        String[] messageData = client.getReturnMessage().split(" ", -1);
        name = messageData[1];
        try {            weight = Integer.parseInt(messageData[2]);
        }catch (Exception ignored){}
    }

    public void manageErrors(String message){
        String[] messageData = message.split(" ", -1);
        if(Objects.equals(messageData[0], "userData")){
            if(Objects.equals(messageData[1], "taken")){
                emailWarning.setText("Email Address is Already In Use");
                emailWarning.setTextColor(Color.RED);
            }
            if(Objects.equals(messageData[2], "false")){
                emailWarning.setText("Please Enter a Valid Email Address");
                emailWarning.setTextColor(Color.RED);
            }
            if(Objects.equals(messageData[2], "true") && Objects.equals(messageData[1], "free")){
                emailWarning.setText("✓");
                emailWarning.setTextColor(Color.GREEN);
                Toast.makeText(getApplicationContext(),"Sign Up Successful.",Toast.LENGTH_SHORT).show();

            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String savedEmail = null;
        try {
            FileInputStream inputStream = openFileInput("saveEmail.txt"); // replace "filename.txt" with the name of the file where you stored the word
            int length = inputStream.available();
            byte[] buffer = new byte[length];
            inputStream.read(buffer);
            savedEmail = new String(buffer);
            inputStream.close();

            // do something with the word, for example, log it
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        //Toast.makeText(getApplicationContext(),storedEmail,Toast.LENGTH_SHORT).show();

        super.onCreate(savedInstanceState);

        //clientHandler client = new clientHandler("userData tom wraggd tomwragg13@gmail.com 3179Jgb215!");
        //Log.d("app",client.getReturnMessage());
        //fetchUserData(savedEmail);

        if(!Objects.equals(savedEmail, "")){
            fetchUserData(savedEmail);
            this.user = new User(savedEmail, name, weight);
            setContentView(R.layout.activity_home);
            Intent myInt = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(myInt);
            overridePendingTransition(0,0);

        }else{
            this.user = new User("null", "null", 10);
        }
        setContentView(R.layout.activity_main);
        firstNameText = (EditText) findViewById(R.id.createFirstName);
        secondNameText = (EditText) findViewById(R.id.createSecondName);
        emailText = (EditText) findViewById(R.id.createEmail);
        passwordText = (EditText) findViewById(R.id.createPassword);
        confirmPasswordText = (EditText) findViewById(R.id.createConfirmPassword);
        nameErrorText = (TextView) findViewById(R.id.nameErrorText);
        emailWarning = (TextView) findViewById(R.id.emailWarning);





    }
    class MyServerThread implements Runnable{
        Socket s;

        ServerSocket ss;
        InputStreamReader isr;
        BufferedReader br;
        Handler h = new Handler();



        @Override
        public void run(){
            try{
                ss = new ServerSocket(7801);
                while (true){



                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] messageData = "message".split(" ", -1);
                            if(Objects.equals(messageData[0], "userData")){
                                if(Objects.equals(messageData[1], "taken")){
                                    emailWarning.setText("Email Address is Already In Use");
                                    emailWarning.setTextColor(Color.RED);
                                }
                                if(Objects.equals(messageData[2], "false")){
                                    emailWarning.setText("Please Enter a Valid Email Address");
                                    emailWarning.setTextColor(Color.RED);
                                }
                                if(Objects.equals(messageData[2], "true") && Objects.equals(messageData[1], "free")){
                                    emailWarning.setText("✓");
                                    emailWarning.setTextColor(Color.GREEN);
                                    Toast.makeText(getApplicationContext(),"Sign Up Successful.",Toast.LENGTH_SHORT).show();

                                }
                            }
                            if(Objects.equals(messageData[0], "loginInfo")){
                                if(Objects.equals(messageData[1], "pass")){
                                    Toast.makeText(getApplicationContext(),"Login Successful.",Toast.LENGTH_SHORT).show();
                                    String email = messageData[2];
                                    fetchUserData(email);
                                    user = new User(email, name, weight);

                                    //setContentView(R.layout.activity_home);
                                    Intent myInt = new Intent(getApplicationContext(), HomeActivity.class);
                                    startActivity(myInt);
                                    overridePendingTransition(0,0);

                                }else{
                                    //Toast.makeText(getApplicationContext(),"set false",Toast.LENGTH_SHORT).show();
                                    LoginActivity.setError();
                                    //loginErrorText.setText("Email or Password is Incorrect");
                                    //loginErrorText.setTextColor(Color.RED);

                                }

                            }
                            if(Objects.equals(messageData[0], "fetchData")){
                                name = messageData[1];
                                try{
                                    weight = Integer.parseInt(messageData[2]);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    weight = 1;
                                }

                                //Toast.makeText(getApplicationContext(),name + weight1,Toast.LENGTH_SHORT).show();
                                user.setName(name);
                                user.setWeight(weight);
                            }
                        }
                    });

                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public void swapToSignin(View view){
        Intent myInt = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(myInt);
        overridePendingTransition(0,0);
    }

    public void checkSignup(View view) {
        TextView buttonText = (TextView) findViewById(R.id.textView);
        TextView passwordWarningText = (TextView) findViewById(R.id.passwordWarningText);
        //buttonText.setText("Button Pressed");


        String firstName = (firstNameText.getText().toString());
        String secondName = (secondNameText.getText().toString());
        String email = (emailText.getText().toString());
        String password = (passwordText.getText().toString());
        String confirmPassword = (confirmPasswordText.getText().toString());

        Matcher m = p.matcher(firstNameText.getText().toString());
        Matcher m2 = p.matcher(secondNameText.getText().toString());
        boolean namePass = false;
        if (m.find() && m2.find()) {
            nameErrorText.setText("✓");
            nameErrorText.setTextColor(Color.GREEN);
            namePass = true;
        } else {
            nameErrorText.setText("Enter a Valid Name");
            nameErrorText.setTextColor(Color.RED);
            namePass = false;
        }

        boolean emailPass = false;
        if(validate(email)){
            emailPass = true;
            emailWarning.setText("✓");
            emailWarning.setTextColor(Color.GREEN);
        }else{
            emailPass = false;
            emailWarning.setText("Enter a Valid Email Address");
            emailWarning.setTextColor(Color.RED);
        }

        boolean passwordPass = false;
        if((password.replaceAll("\\s", "").equals(""))||(confirmPassword.replaceAll("\\s", "").equals(""))){
            passwordWarningText.setText("Enter and Confirm Passwords");
            passwordWarningText.setTextColor(Color.RED);
            passwordPass = false;
        }else{
            if(!password.equals(confirmPassword)){
                passwordWarningText.setText("Make Sure Both Passwords Match");
                passwordWarningText.setTextColor(Color.RED);
                passwordPass = false;
            }else{
                passwordWarningText.setText("✓");
                passwordWarningText.setTextColor(Color.GREEN);
                passwordPass = true;
            }

        }

        if((firstName.replaceAll("\\s", "").equals(""))||(secondName.replaceAll("\\s", "").equals(""))||
                (email.replaceAll("\\s", "").equals(""))||(password.replaceAll("\\s", "").equals(""))||
                (confirmPassword.replaceAll("\\s", "").equals(""))){
            buttonText.setText("Fill Out All Fields");
            buttonText.setTextColor(Color.RED);
        }else{
            buttonText.setText("✓");
            buttonText.setTextColor(Color.GREEN);
            String userDataMessage = "userData " + firstName.replaceAll("\\s","") + " " + secondName.replaceAll("\\s","")
                    + " " + email.replaceAll("\\s","") + " " + Encrypt.encryptData(password.replaceAll("\\s",""));
            if(passwordPass && namePass && emailPass){

                ClientHandler client = new ClientHandler(userDataMessage);
                Log.d("app", client.getReturnMessage());
                manageErrors(client.getReturnMessage());

            }
        }









    }
}