package com.example.finalproj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    public void manageErrors(String message){
        String[] messageData = message.split(",", -1);
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

        String savedEmail = new savedData(getApplicationContext()).readSavedEmail();

        super.onCreate(savedInstanceState);

        ClientHandler client = new ClientHandler("userData,tom5,tom6," + savedEmail + ",pass,10");
        String isTaken = client.getReturnMessage();
        String[] isTakenData = isTaken.split(",", -1);
        String storedOldEmail = isTakenData[1];

        if(Objects.equals(storedOldEmail, "free")){
            savedEmail = "";
            Log.d("app", "free");
        }else{
            Log.d("app", "taken");
        }

        if(!Objects.equals(savedEmail, "")){
            setContentView(R.layout.activity_home);
            Intent myInt = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(myInt);
            overridePendingTransition(0,0);

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

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public void swapToSignIn(View view){
        Intent myInt = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(myInt);
        overridePendingTransition(0,0);
    }

    public void checkSignup(View view) {
        TextView buttonText = (TextView) findViewById(R.id.textView);
        TextView passwordWarningText = (TextView) findViewById(R.id.passwordWarningText);

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
            String userDataMessage = "userData," + firstName.replaceAll("\\s","") + "," + secondName.replaceAll("\\s","")
                    + "," + email.replaceAll("\\s","") + "," + Encrypt.encryptData(password.replaceAll("\\s",""));
            if(passwordPass && namePass && emailPass){

                ClientHandler client = new ClientHandler(userDataMessage);
                Log.d("app", client.getReturnMessage());
                manageErrors(client.getReturnMessage());

            }
        }









    }
}