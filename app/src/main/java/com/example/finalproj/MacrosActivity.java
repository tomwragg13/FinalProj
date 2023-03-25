package com.example.finalproj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MacrosActivity extends AppCompatActivity {

    RecyclerView mealRecycler;
    mealAdapter adapter;

    TextView dateText;

    EditText macrosName, macrosCalories, macrosProtein, macrosWeight, macrosWeight2;

    String date, email, name;
    int weight;
    Spinner foodSpinner;

    List<String> foodNames;
    List<foodProduct> foodItemList = new ArrayList<>();
    List<productManager> food = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macros);

        email = new savedData(getApplicationContext()).readStoredEmail();
        name = new savedData(getApplicationContext()).readStoredName();
        weight = Integer.parseInt(new savedData(getApplicationContext()).readStoredWeight());

        mealRecycler = (RecyclerView) findViewById(R.id.mealRecycler);
        dateText = (TextView) findViewById(R.id.macrosDate);
        macrosName = (EditText) findViewById(R.id.macrosName);
        macrosCalories = (EditText) findViewById(R.id.macrosCalories);
        macrosProtein = (EditText) findViewById(R.id.macrosProtein);
        macrosWeight = (EditText) findViewById(R.id.macrosWeight);
        macrosWeight2 = (EditText) findViewById(R.id.macrosWeight2);

        foodSpinner = (Spinner) findViewById(R.id.spinner);

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        date = sdf.format(new Date());
        date = DateHandler.getDate("null", date)[0];
        dateText.setText(DateHandler.getDate("null", date)[1]);

        foodNames = new ArrayList<>();
        foodNames.add("Select Food");
        loadSpinner();

        foodProduct pringles = new foodProduct("Pringles", 154, 1.7, 30);
        productManager Pringles = new productManager("Pringles", pringles, 30);
        productManager Pringles2 = new productManager("Pringles", pringles, 600);

        food.add(Pringles);

        Log.d("Pringles", String.valueOf(Pringles.getTotalCalories()));
        Log.d("Pringles", String.valueOf(Pringles2.getTotalCalories()));

        mealRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapter = new mealAdapter(getApplicationContext(), food);
        mealRecycler.setAdapter(adapter);


    }

    public void loadSpinner(){
        ClientHandler clientHandler = new ClientHandler("loadFood," + email);
        String foodList = clientHandler.getReturnMessage();
        //Log.d("list", workoutList);

        String[] messageData = foodList.split(",", -1);

        //foodItemList = new ArrayList<>();
        //foodNames = new ArrayList<>();
        Log.d("l", String.valueOf(messageData[8]));
        String[] maessageDataCPy = Arrays.copyOf(messageData, messageData.length-1);
        messageData = maessageDataCPy;


        for (int i = 0; i < messageData.length; i += 4) {
            foodProduct foodItem = new foodProduct(messageData[i], Double.parseDouble((messageData[i+1])), Double.parseDouble((messageData[i+2])), Integer.parseInt(messageData[i+3]));
            foodItemList.add(foodItem);
        }

        Log.d("q", String.valueOf(foodItemList.get(1).getName()));

        for (foodProduct foodItem : foodItemList) {
            foodNames.add(foodItem.getName());
        }

        Log.d("go", String.valueOf(foodNames));

        //foodNames.remove(foodNames.size()-1);
        foodNames.remove(0);
        Collections.sort(foodNames);
        foodNames.add(0, "Select Food");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, foodNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodSpinner.setAdapter(adapter);


    }

    @Override
    public void onBackPressed() {
        Intent myInt = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(myInt);
        overridePendingTransition(0,R.anim.slide_out_right);
    }

    public void openMain(View view){
        Intent myInt = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(myInt);
        overridePendingTransition(0,R.anim.slide_out_right);
    }

    public void addFood(View view){
        for (foodProduct obj : foodItemList) {
            if (obj.getName().equals(foodSpinner.getSelectedItem().toString())) {

                productManager foods = new productManager(foodSpinner.getSelectedItem().toString(), obj, Integer.parseInt(macrosWeight2.getText().toString()));
                food.add(foods);



                mealRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new mealAdapter(getApplicationContext(), food);
                mealRecycler.setAdapter(adapter);

                String namesString = "";
                for (int i = 0; i < food.size(); i++) {
                    String name = food.get(i).getName();
                    double calories = food.get(i).getTotalCalories();
                    double protein = food.get(i).getTotalProtein();
                    int portion = food.get(i).getPortion();
                    namesString += name + ",";
                    namesString += calories + ",";
                    namesString += protein + ",";
                    namesString += portion + ",";
                }

                Log.d("names", namesString);
                ClientHandler clientHandler = new ClientHandler("saveFood," + email + "," + date + "," + namesString);

                System.out.println(foods.getTotalCalories());
                break;
            }
        }


    }

    public void createFood(View view){
        String name = macrosName.getText().toString();
        String calories = macrosCalories.getText().toString();
        String protein = macrosProtein.getText().toString();
        String weight = macrosWeight.getText().toString();

        Toast.makeText(getApplicationContext(),name + " Created",Toast.LENGTH_SHORT).show();

        ClientHandler clientHandler = new ClientHandler("addFood," + email + "," + name + "," + calories + "," + protein + "," + weight);
        loadSpinner();
    }

    public void nextButton(View view){
        String[] dateData = DateHandler.getDate( "next", date);
        date = dateData[0];
        dateText.setText(dateData[1]);
    }

    public void backButton(View view){
        String[] dateData = DateHandler.getDate( "back", date);
        date = dateData[0];
        dateText.setText(dateData[1]);
    }
}