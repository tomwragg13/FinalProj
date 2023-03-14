package com.example.finalproj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class WorkoutActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Workouts> workouts;
    List<String> itemList;
    EditText enterName, enterReps, enterWeight, enterSets;
    MyAdapter adapter;
    String email;
    String name;
    int weight;

    String date;
    TextView dateText, nameText, weightText, workoutNameInput;
    ConstraintLayout mainPanel, hiddenInput;
    ImageView expandButton;

    Button createID;

    Spinner workoutSpinner;

    float recyclerHeight;
    boolean expandState, isDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        email = new savedData(getApplicationContext()).readStoredEmail();
        name = new savedData(getApplicationContext()).readStoredName();
        weight = Integer.parseInt(new savedData(getApplicationContext()).readStoredWeight());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        expandState = false;
        isDown = false;
        //workoutNameInput.setFocusable(false);

        enterReps = (EditText) findViewById(R.id.workoutReps);
        enterWeight = (EditText) findViewById(R.id.workoutWeight);
        enterSets = (EditText) findViewById(R.id.workoutSets);
        dateText = (TextView) findViewById(R.id.dateID);
        nameText = (TextView) findViewById(R.id.workoutNameTag);
        weightText = (TextView) findViewById(R.id.workoutWeightTag);
        mainPanel = (ConstraintLayout) findViewById(R.id.mainPanel);
        expandButton = (ImageView) findViewById(R.id.expandButton);
        createID = (Button) findViewById(R.id.createID);
        hiddenInput = (ConstraintLayout) findViewById(R.id.hiddenInput);
        workoutNameInput = (TextView) findViewById(R.id.workoutNameInput);
        workoutSpinner = (Spinner) findViewById(R.id.workoutSpinner);

        nameText.setText(name);
        weightText.setText(String.valueOf(weight + " Kg"));

        itemList = new ArrayList<>();
        itemList.add("Select Workout");
        loadSpinner();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workoutSpinner.setAdapter(adapter);

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        date = sdf.format(new Date());
        date = DateHandler.getDate("null", date)[0];
        dateText.setText(DateHandler.getDate("null", date)[1]);

        loadWorkouts();
        Log.d("h", String.valueOf(recyclerHeight));


        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        recyclerHeight = recyclerView.getY();

                        // Don't forget to remove your listener when you are done with it.
                        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

        savedData.addIfEmpty(recyclerView, getApplicationContext(), workouts, email, date, expandButton, recyclerHeight);
    }

    public void openPBS(View view){
        ClientHandler clientHandler = new ClientHandler("loadPBS," + email);
        Log.d("PBS", clientHandler.getReturnMessage());
        String pbList = clientHandler.getReturnMessage();
        String[] messageData = pbList.split(",", -1);
    }

    public void loadSpinner(){
        ClientHandler clientHandler = new ClientHandler("loadWorkouts," + email);
        String workoutList = clientHandler.getReturnMessage();
        Log.d("list", workoutList);

        String[] messageData = workoutList.split(",", -1);

        itemList.addAll(Arrays.asList(messageData));
        itemList.remove(itemList.size()-1);
        itemList.remove(0);
        Collections.sort(itemList);
        itemList.add(0, "Select Workout");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workoutSpinner.setAdapter(adapter);


    }

    public void createButton(View view){
        if(!isDown){
            workoutNameInput.setText("");
            hiddenInput.animate().translationYBy(pxFromDp(getApplicationContext(), 60));
            isDown = true;
        }else{
            if(!workoutNameInput.getText().toString().equals("")){
                String input = workoutNameInput.getText().toString();

                String[] words = input.split(" ");

                for (int i = 0; i < words.length; i++) {
                    String word = words[i];
                    if (word.length() > 0) {
                        String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1);
                        words[i] = capitalizedWord;
                    }
                }
                String capitalizedString = String.join(" ", words);

                Toast.makeText(getApplicationContext(),"Workout Created",Toast.LENGTH_SHORT).show();
                ClientHandler clientHandler = new ClientHandler("createWorkout," + email + "," + capitalizedString);

                itemList.remove(0);
                itemList.add(capitalizedString);
                Collections.sort(itemList);
                itemList.add(0, "Select Workout");


                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                workoutSpinner.setAdapter(adapter);


            }
            hiddenInput.animate().translationYBy(pxFromDp(getApplicationContext(), -60));
            isDown = false;

        }

    }



    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
    public void expandView(View view){
        Log.d("state", String.valueOf(expandState));

        int expandNum = 5;
        int duration = 500;

        //Log.d("height", String.valueOf(pxFromDp(getApplicationContext(), recyclerHeight)));
        Log.d("pass1", String.valueOf(recyclerHeight));
        Log.d("pass2", String.valueOf(recyclerView.getY()));
        if(recyclerView.getY() == recyclerHeight){

            expandState = false;
        }

        if(workouts.size() != 1){
            //savedData.changeRecyclerSize(getApplicationContext(), workouts, recyclerView);



            if(!Objects.equals(workouts.get(0).getName(), "Add Workouts")){
                //Log.d("pass", String.valueOf(workouts.size()));
                float px = 0;
                if(!expandState){
                    if(workouts.size()>expandNum){
                        px = pxFromDp(getApplicationContext(), -(75*(expandNum-1)));
                    }
                    if(workouts.size() >1 && workouts.size() <=expandNum){
                        px = pxFromDp(getApplicationContext(), -(75*workouts.size()-75));

                    }
                    expandButton.animate().rotation(-90);
                    expandState = true;
                }else{
                    px = 0;
                    expandState = false;
                    expandButton.animate().rotation(90);
                }

                expandButton.animate().translationY(px).setDuration(duration);
                recyclerView.animate().translationY(px).setDuration(duration);

                recyclerView.setElevation(1000);
                expandButton.setElevation(1000);

            }
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int px = mainPanel.getHeight();

        //DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        //int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));

        float dp = dpFromPx(getApplicationContext(), px);
        //float dp = dpFromPx(px);
        Log.d("hi", String.valueOf(dp));

        //dp -= 120;
        dp -= 75;
        //recyclerHeight = dp;

        //Log.d("hi", String.valueOf(dp));

        //float px = pxFromDp(getApplicationContext(), (75*workouts.size())-75);

        //recyclerView.animate().translationY(-px).setDuration(100000);
    }

    private void loadWorkouts(){
        workouts = new ArrayList<Workouts>();

        ClientHandler client = new ClientHandler("workoutRead," + email + "," + date);
        String[] messageData = client.getReturnMessage().split(",", -1);
        Log.d("ret", client.getReturnMessage());

        ArrayList<String> strList = new ArrayList<String>(
                Arrays.asList(messageData));
        strList.remove(0);

        int items = strList.size()/5;

        if(strList.size() >0){
            Log.d("items", strList.get(0));
            for (int i = 0; i < items; i++) {
                workouts.add(new Workouts(strList.get(5 * i), Integer.parseInt(strList.get(3+(5*i))), Integer.parseInt(strList.get(1+(5*i))), Integer.parseInt(strList.get(2+(5*i))), Double.parseDouble(strList.get(4+(5*i))), date));
            }
        }


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAdapter(getApplicationContext(), workouts, email, date, recyclerView, expandButton, recyclerHeight);
        recyclerView.setAdapter(adapter);

        //savedData.changeRecyclerSize(getApplicationContext(), workouts, recyclerView);
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
        if(workouts.size() == 1 && Objects.equals(workouts.get(0).getName(), "Add Workouts")){
            workouts.remove(0);
        }

        workouts.add(0,new Workouts(workoutSpinner.getSelectedItem().toString(), Integer.parseInt(enterReps.getText().toString()), Integer.parseInt(enterWeight.getText().toString()), Integer.parseInt(enterSets.getText().toString()), weight, date));
        savedData.saveChanges(email, date, workouts);
        adapter.notifyItemInserted(0);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAdapter(getApplicationContext(), workouts, email, date, recyclerView, expandButton, recyclerHeight);
        recyclerView.setAdapter(adapter);

        loadWorkouts();


    }

    public void nextButton(View view){
        String[] dateData = DateHandler.getDate( "next", date);
        date = dateData[0];
        dateText.setText(dateData[1]);
        loadWorkouts();

        savedData.addIfEmpty(recyclerView, getApplicationContext(), workouts, email, date, expandButton, recyclerHeight);
    }

    public void backButton(View view){
        String[] dateData = DateHandler.getDate( "back", date);
        date = dateData[0];
        dateText.setText(dateData[1]);
        loadWorkouts();

        savedData.addIfEmpty(recyclerView, getApplicationContext(), workouts, email, date, expandButton, recyclerHeight);
    }

}