package com.example.finalproj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WorkoutActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Workouts> workouts;
    List<personalBests> finalPBs;
    List<String> workoutNames;
    EditText enterName, enterReps, enterWeight, enterSets;
    workoutAdapter adapter;
    personalBestAdapter adapter1;
    String email;
    String name;
    int weight;

    String date, expandType;
    TextView dateText, nameText, weightText, workoutNameInput;
    ConstraintLayout mainPanel, hiddenInput, clickBlocker;
    ImageView expandButton;

    Button createID;

    Spinner workoutSpinner;

    float recyclerHeight, expandButtonHeight, mainPanelHeight = 0;
    boolean expandState, isDown;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        email = new savedData(getApplicationContext()).readStoredEmail();
        name = new savedData(getApplicationContext()).readStoredName();
        weight = Integer.parseInt(new savedData(getApplicationContext()).readStoredWeight());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        expandState = false;
        isDown = false;
        expandType = "workouts";

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
        clickBlocker = (ConstraintLayout) findViewById(R.id.clickBlocker);

        nameText.setText(name);
        weightText.setText(String.valueOf(weight + " Kg"));

        workoutNames = new ArrayList<>();
        workoutNames.add("Select Workout");
        loadSpinner();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, workoutNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workoutSpinner.setAdapter(adapter);

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        date = sdf.format(new Date());
        date = DateHandler.getDate("null", date)[0];
        dateText.setText(DateHandler.getDate("null", date)[1]);

        loadWorkouts();

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                recyclerHeight = recyclerView.getY();
                expandButtonHeight = expandButton.getY();
                mainPanelHeight = mainPanel.getHeight();

                Log.d("PanelH", String.valueOf(mainPanelHeight));
                savedData.addIfEmpty(recyclerView, getApplicationContext(), workouts, email, date, expandButton, recyclerHeight, finalPBs, expandType, clickBlocker);

                // Don't forget to remove your listener when you are done with it.
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });

    }


    public void openPBSbutton(View view){
        boolean toggle = false;
        Log.d("type", expandType);

        if(Objects.equals(expandType, "workouts") && !toggle){
            StringBuilder workoutSearch = new StringBuilder();
            for (int i = 0; i < workoutNames.size(); i++) {
                if(!Objects.equals(workoutNames.get(i), "Select Workout")){
                    workoutSearch.append(workoutNames.get(i));
                    workoutSearch.append(",");
                }
            }

            String newStr = "";
            if(workoutSearch.length() > 0){
                newStr = workoutSearch.substring(0, workoutSearch.length() - 1);
            }

            ClientHandler clientHandler = new ClientHandler("loadPBS," + email + "," + newStr);
            Log.d("PBS", clientHandler.getReturnMessage());
            String pbList = clientHandler.getReturnMessage();
            String[] messageData = pbList.split(",", -1);

            List<personalBests> PBs = new ArrayList<personalBests>();

            for (int i = 0; i < messageData.length-1; i += 3) {
                // Get the current name and number
                String name = messageData[i+1];
                int number = Integer.parseInt(messageData[i + 2]);
                String date = messageData[i];
                Log.d("data", name + " " + number + " " + date);

                // Create a new object with the current name and number
                personalBests pb = new personalBests(name, number, date);

                PBs.add(pb);
            }

            Map<String, personalBests> map = new HashMap<>();
            for (personalBests pb : PBs) {
                String name = pb.getName();
                if (!map.containsKey(name) || map.get(name).getWeight() < pb.getWeight()) {
                    map.put(name, pb);
                }
            }
            finalPBs = new ArrayList<>(map.values());
            finalPBs.sort(Comparator.comparing(personalBests::getName));

            if(finalPBs.size() > 0){
                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));

                adapter1 = new personalBestAdapter(getApplicationContext(), finalPBs);
                recyclerView.setAdapter(adapter1);
            }else{
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
                String today = sdf.format(new Date());

                finalPBs = new ArrayList<>();
                finalPBs.add(new personalBests("Add Workouts To See PBs", 0, today));

                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));

                adapter1 = new personalBestAdapter(getApplicationContext(), finalPBs);
                recyclerView.setAdapter(adapter1);
            }
            expandType = "PB";
            toggle = true;
            expandView();
        }
        if(Objects.equals(expandType, "PB") && !toggle){
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            expandType = "workouts";
            adapter = new workoutAdapter(getApplicationContext(), workouts, email, date, recyclerView, expandButton, recyclerHeight, finalPBs, expandType, clickBlocker);
            recyclerView.setAdapter(adapter);
            toggle = true;
        }

        Log.d("type", expandType);
        expanderToggle();
    }

    public void loadSpinner(){
        ClientHandler clientHandler = new ClientHandler("loadWorkouts," + email);
        String workoutList = clientHandler.getReturnMessage();
        Log.d("list", workoutList);

        String[] messageData = workoutList.split(",", -1);

        workoutNames.addAll(Arrays.asList(messageData));
        workoutNames.remove(workoutNames.size()-1);
        workoutNames.remove(0);
        Collections.sort(workoutNames);
        workoutNames.add(0, "Select Workout");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, workoutNames);
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

                workoutNames.remove(0);
                workoutNames.add(capitalizedString);
                Collections.sort(workoutNames);
                workoutNames.add(0, "Select Workout");


                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, workoutNames);
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

    public void expandView(){
        Log.d("state", String.valueOf(expandState));

        int expandNum = 5;
        int duration = 1000;

        //Log.d("height", String.valueOf(pxFromDp(getApplicationContext(), recyclerHeight)));
        Log.d("pass1", String.valueOf(recyclerHeight));
        Log.d("pass2", String.valueOf(recyclerView.getY()));
        if(recyclerView.getY() == recyclerHeight){

            expandState = false;
        }

        if(workouts.size() != 1){
            if(!Objects.equals(workouts.get(0).getName(), "Add Workouts") && Objects.equals(expandType, "workouts")){

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

                    expandButton.animate().translationY(px).setDuration(duration);
                    recyclerView.animate().translationY(px).setDuration(duration);

                    clickBlocker.animate().y(pxFromDp(getApplicationContext(), 120));
                    clickBlocker.setElevation(999);
                }else{
                    closeRecycler();
                }



                recyclerView.setElevation(1000);
                expandButton.setElevation(1000);



            }
        }
        try {
            if(finalPBs.size() !=1){
                if(!Objects.equals(finalPBs.get(0).getName(), "Add Workouts To See PBs") && Objects.equals(expandType, "PB")){
                    float px = 0;
                    if(!expandState){
                        if(finalPBs.size()>expandNum){
                            px = pxFromDp(getApplicationContext(), -(75*(expandNum-1)));
                        }
                        if(finalPBs.size() >1 && finalPBs.size() <=expandNum){
                            px = pxFromDp(getApplicationContext(), -(75*finalPBs.size()-75));

                        }
                        expandButton.animate().rotation(-90);
                        expandState = true;

                        expandButton.animate().translationY(px).setDuration(duration);
                        recyclerView.animate().translationY(px).setDuration(duration);

                        clickBlocker.animate().y(pxFromDp(getApplicationContext(), 120));
                        clickBlocker.setElevation(999);
                    }else{
                        closeRecycler();
                    }


                    recyclerView.setElevation(1000);
                    expandButton.setElevation(1000);
                }
            }
        } catch (Exception e) {
            Log.d("Error", "PBNull");
        }
    }
    public void expandViewButton(View view){
        expandView();
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

        //expandType = "workouts";
        adapter = new workoutAdapter(getApplicationContext(), workouts, email, date, recyclerView, expandButton, recyclerHeight, finalPBs, expandType, clickBlocker);
        recyclerView.setAdapter(adapter);

        Log.d("size", String.valueOf(workouts.size()));

        expandType = "workouts";
        expanderToggle();

    }

    @Override
    public void onBackPressed() {
        if(expandState){
            closeRecycler();
        }else{
            Intent myInt = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(myInt);
            overridePendingTransition(0,R.anim.slide_out_right);
        }
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

        adapter = new workoutAdapter(getApplicationContext(), workouts, email, date, recyclerView, expandButton, recyclerHeight, finalPBs, expandType, clickBlocker);
        recyclerView.setAdapter(adapter);

        loadWorkouts();





    }

    public void nextButton(View view){
        String[] dateData = DateHandler.getDate( "next", date);
        date = dateData[0];
        dateText.setText(dateData[1]);
        loadWorkouts();
        closeRecycler();



        savedData.addIfEmpty(recyclerView, getApplicationContext(), workouts, email, date, expandButton, recyclerHeight, finalPBs, expandType, clickBlocker);

    }

    public void backButton(View view){
        String[] dateData = DateHandler.getDate( "back", date);
        date = dateData[0];
        dateText.setText(dateData[1]);
        loadWorkouts();
        closeRecycler();

        savedData.addIfEmpty(recyclerView, getApplicationContext(), workouts, email, date, expandButton, recyclerHeight, finalPBs, expandType, clickBlocker);
    }

    public void closeRecycler(){
        Log.d("tpp", expandType);
        if(Objects.equals(expandType, "workouts")){

            recyclerView.animate().y(recyclerHeight).setDuration(1000);
            expandButton.animate().y(expandButtonHeight).setDuration(1000);
            expandButton.animate().rotation(90).setDuration(1000);
            expandState = false;

            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            adapter = new workoutAdapter(getApplicationContext(), workouts, email, date, recyclerView, expandButton, recyclerHeight, finalPBs, expandType, clickBlocker);
            recyclerView.setAdapter(adapter);
        }
        if(Objects.equals(expandType, "PB")){


            Log.d("test", "test");
            recyclerView.animate().y(mainPanelHeight).setDuration(1000);
            expandButton.animate().y(mainPanelHeight-pxFromDp(getApplicationContext(), 40)).setDuration(1000);
            //expandButton.animate().rotation(90).setDuration(2000);

            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    expandState = false;
                    adapter = new workoutAdapter(getApplicationContext(), workouts, email, date, recyclerView, expandButton, recyclerHeight, finalPBs, expandType, clickBlocker);
                    recyclerView.setAdapter(adapter);
                    recyclerView.animate().y(recyclerHeight).setDuration(1000);
                    expandButton.animate().y(expandButtonHeight).setDuration(1000);
                    expandButton.animate().rotation(90).setDuration(1000);
                }
            }, 1000);

        }

        clickBlocker.animate().y(mainPanelHeight).setDuration(1);



        Log.d("close", expandType);
        expandType = "workouts";
        expanderToggle();

    }


    public void expanderToggle(){
        Log.d("WS", String.valueOf(workouts.size()));
        //Log.d("PBS", String.valueOf(finalPBs.size()));
        Log.d("ET", expandType);
        if(workouts.size() >= 2  && Objects.equals(expandType, "workouts")){
            Animation fadeout = new AlphaAnimation(0.f, 1.f);
            fadeout.setDuration(1000);
            expandButton.startAnimation(fadeout);
            expandButton.postDelayed(new Runnable() {
                @Override
                public void run() {
                    expandButton.setVisibility(View.VISIBLE);
                }
            }, 1000);
        }
        if(workouts.size() <= 1  && Objects.equals(expandType, "workouts")){
            Animation fadeout = new AlphaAnimation(1.f, 0.f);
            fadeout.setDuration(1000);
            expandButton.startAnimation(fadeout);
            expandButton.postDelayed(new Runnable() {
                @Override
                public void run() {
                    expandButton.setVisibility(View.GONE);
                }
            }, 1000);
        }

        try{
            if(finalPBs.size() < 2 && Objects.equals(expandType, "PB")){
                Animation fadeout = new AlphaAnimation(1.f, 0.f);
                fadeout.setDuration(1000);
                expandButton.startAnimation(fadeout);
                expandButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        expandButton.setVisibility(View.GONE);
                    }
                }, 1000);
            }
            if(finalPBs.size() >= 2 && Objects.equals(expandType, "PB")){
                Animation fadeout = new AlphaAnimation(0.f, 1.f);
                fadeout.setDuration(1000);
                expandButton.startAnimation(fadeout);
                expandButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        expandButton.setVisibility(View.VISIBLE);
                    }
                }, 1000);
            }
        } catch (Exception e) {
            Log.d("NULLPB", "NULLPB");
        }


    }

    public void preventTouch(View view){

    }

    public void recyclerCloseView(View view){
        closeRecycler();
    }



}