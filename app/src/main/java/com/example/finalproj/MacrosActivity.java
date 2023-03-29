package com.example.finalproj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    boolean expandState = false;

    ImageView expandButton2;

    ConstraintLayout recyclerHeader, clickBlocker2;

    float mealRecyclerHeight;

    TextView nameTag;
    TextView weightTag;

    float calorieGoal = 1800f;
    float proteinGoal = 100f;

    private PieChart pieChart, pieChart2;
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
        expandButton2 = (ImageView) findViewById(R.id.expandButton2);
        clickBlocker2 = (ConstraintLayout) findViewById(R.id.clickBlocker2);

        pieChart = findViewById(R.id.pieChart);
        pieChart2 = findViewById(R.id.pieChart2);

        nameTag = (TextView) findViewById(R.id.nameTag);
        weightTag = (TextView) findViewById(R.id.weightTag);

        nameTag.setText(name);
        weightTag.setText(weight+ " Kg");


        foodSpinner = (Spinner) findViewById(R.id.spinner);
        recyclerHeader = (ConstraintLayout) findViewById(R.id.recyclerHeader);

        expandButton2.setElevation(1000);
        //recyclerHeader.setElevation(998);
        mealRecycler.setElevation(999);
        clickBlocker2.setElevation(980);

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        date = sdf.format(new Date());
        date = DateHandler.getDate("null", date)[0];
        dateText.setText(DateHandler.getDate("null", date)[1]);

        foodNames = new ArrayList<>();
        foodNames.add("Select Food");
        loadSpinner();
        foodRead();
        addIfEmpty();

        mealRecycler.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mealRecyclerHeight = mealRecycler.getY();

            }
        });


    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public void openFood(View view){
        expandFood();
    }

    public void expandFood(){

        if(!Objects.equals(food.get(0).getName(), "Add Food To Track Macros")){
            float px = 0;
            if(!expandState){
                px = pxFromDp(getApplicationContext(), -(75*food.size()-75));
                recyclerHeader.animate().translationYBy(px).setDuration(500);
                mealRecycler.animate().translationYBy(px).setDuration(500);
                expandButton2.animate().translationYBy(px).setDuration(500);
                expandButton2.animate().rotation(90);

                clickBlocker2.animate().y(0);
                expandState = true;
            }else {
                px = pxFromDp(getApplicationContext(), (75*food.size()-75));
                recyclerHeader.animate().translationYBy(px).setDuration(500);
                mealRecycler.animate().translationYBy(px).setDuration(500);
                expandButton2.animate().translationYBy(px).setDuration(500);
                expandButton2.animate().rotation(-90);

                clickBlocker2.animate().y(mealRecyclerHeight+pxFromDp(getApplicationContext(), 75));
                expandState = false;
            }
        }
    }

    public void addIfEmpty(){
        if(food.size() == 0){

            foodProduct pringles = new foodProduct("Add Food To Track Macros", 1, 1, 1);
            food.add(new productManager("Add Food To Track Macros", pringles, 1, date));

            mealRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            adapter = new mealAdapter(getApplicationContext(), food);
            mealRecycler.setAdapter(adapter);
        }
    }

    private void foodRead() {
        ClientHandler clientHandler = new ClientHandler("foodRead," + email+ "," + date);
        Log.d("foodRet", clientHandler.getReturnMessage());

        String str = clientHandler.getReturnMessage();
        String[] strArray = str.split(",");
        List<String> list = new ArrayList<>(Arrays.asList(strArray));
        list.remove(0);

        ArrayList<productManager> foodToAdd = new ArrayList<>();
        for (int i = 0; i < list.size(); i = i+4) {
            for (int j = 0; j < foodItemList.size(); j++){
                if(Objects.equals(foodItemList.get(j).getName(), list.get(i))){
                    foodToAdd.add(new productManager(list.get(i), foodItemList.get(j), Integer.parseInt(list.get(i+3)), date));
                }
            }

        }

        food = foodToAdd;
        //Log.d("oo", food.get(2).getName());

        mealRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new mealAdapter(getApplicationContext(), food);
        mealRecycler.setAdapter(adapter);



        Log.d("size", String.valueOf(food.size()));
        if(food.size() <= 1){
            expandButton2.setVisibility(View.GONE);
        }else{
            expandButton2.setVisibility(View.VISIBLE);
        }

        loadPieChart();
        loadPieChart2();



    }

    private void loadPieChart2() {
        pieChart2.setDrawHoleEnabled(true);
        pieChart2.setUsePercentValues(true);
        pieChart2.setEntryLabelTextSize(12);
        pieChart2.setEntryLabelColor(Color.rgb(11, 4, 82));
        pieChart2.setCenterText("Calories");
        pieChart2.setCenterTextSize(16);
        pieChart2.getDescription().setEnabled(false);

        ArrayList<PieEntry> entries = new ArrayList<>();
        //entries.add(new PieEntry(0.3f, "Pie"));
        //entries.add(new PieEntry(0.7f, ""));

        double totalProtein = 0;
        float totalPercentage = 0;
        for(productManager product: food)    {
            totalProtein += product.getTotalProtein();

            float percentage = (float) (product.getTotalProtein()/proteinGoal);
            totalPercentage += percentage;
            String name = product.getName();
            entries.add(new PieEntry(percentage, name));
        }
        Log.d("totalp", String.valueOf(totalPercentage));
        if(totalPercentage < 1f){
            entries.add(new PieEntry(1f-totalPercentage, ""));
        }
        pieChart2.setCenterText(String.format("%.0f", totalPercentage*100)+ "%");

        ArrayList<Integer> colors = new ArrayList<>();
        float prot = (float) totalProtein;
        Log.d("Cals", String.valueOf(prot));
        float percentageEaten = (prot/calorieGoal)*100;
        if(percentageEaten <= 50){
            for (int i = 0; i < food.size(); i++) {
                colors.add(Color.RED);
            }
        }
        if(percentageEaten > 50 && percentageEaten <= 80){
            for (int i = 0; i < food.size(); i++) {
                colors.add(Color.rgb(255, 106, 0));
            }
        }
        if(percentageEaten > 80){
            for (int i = 0; i < food.size(); i++) {
                colors.add(Color.GREEN);
            }
        }

        colors.add(Color.rgb(238, 234, 249));


        PieDataSet dataSet = new PieDataSet(entries, "Categories");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart2));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.rgb(11, 4, 82));

        Legend L = pieChart2.getLegend();
        L.setEnabled(false);

        pieChart2.setData(data);
        pieChart2.invalidate();
    }

    private void loadPieChart() {
        //pieChart.getWidth()
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.rgb(11, 4, 82));
        pieChart.setCenterText("Calories");
        pieChart.setCenterTextSize(16);
        pieChart.getDescription().setEnabled(false);

        ArrayList<PieEntry> entries = new ArrayList<>();
        //entries.add(new PieEntry(0.3f, "Pie"));
        //entries.add(new PieEntry(0.7f, ""));

        double totalCalories = 0;
        float totalPercentage = 0;
        for(productManager product: food)    {
            totalCalories += product.getTotalCalories();

            float percentage = (float) (product.getTotalCalories()/calorieGoal);
            totalPercentage += percentage;
            String name = product.getName();
            entries.add(new PieEntry(percentage, name));
        }
        Log.d("totalp", String.valueOf(totalPercentage));
        if(totalPercentage < 1f){
            entries.add(new PieEntry(1f-totalPercentage, ""));
        }
        pieChart.setCenterText(String.format("%.0f", totalPercentage*100)+ "%");

        ArrayList<Integer> colors = new ArrayList<>();
        float calorie = (float) totalCalories;
        Log.d("Cals", String.valueOf(calorie));
        float percentageEaten = (calorie/calorieGoal)*100;
        if(percentageEaten <= 50){
            for (int i = 0; i < food.size(); i++) {
                colors.add(Color.RED);
            }
        }
        if(percentageEaten > 50 && percentageEaten <= 80){
            for (int i = 0; i < food.size(); i++) {
                colors.add(Color.rgb(255, 106, 0));
            }
        }
        if(percentageEaten > 80){
            for (int i = 0; i < food.size(); i++) {
                colors.add(Color.GREEN);
            }
        }

        colors.add(Color.rgb(238, 234, 249));


        PieDataSet dataSet = new PieDataSet(entries, "Categories");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.rgb(11, 4, 82));

        Legend L = pieChart.getLegend();
        L.setEnabled(false);

        pieChart.setData(data);
        pieChart.invalidate();
    }

    public void loadSpinner(){
        ClientHandler clientHandler = new ClientHandler("loadFood," + email);
        String foodList = clientHandler.getReturnMessage();
        //Log.d("list", workoutList);

        String[] messageData = foodList.split(",", -1);

        //foodItemList = new ArrayList<>();
        //foodNames = new ArrayList<>();
        //Log.d("l", String.valueOf(messageData[8]));
        String[] maessageDataCPy = Arrays.copyOf(messageData, messageData.length-1);
        messageData = maessageDataCPy;


        for (int i = 0; i < messageData.length; i += 4) {
            foodProduct foodItem = new foodProduct(messageData[i], Double.parseDouble((messageData[i+1])), Double.parseDouble((messageData[i+2])), Integer.parseInt(messageData[i+3]));
            foodItemList.add(foodItem);
        }

        //Log.d("q", String.valueOf(foodItemList.get(1).getName()));

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

                productManager foods = new productManager(foodSpinner.getSelectedItem().toString(), obj, Integer.parseInt(macrosWeight2.getText().toString()), date);
                food.add(foods);

                if(Objects.equals(food.get(0).getName(), "Add Food To Track Macros")){
                    food.remove(0);
                }



                mealRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new mealAdapter(getApplicationContext(), food);
                mealRecycler.setAdapter(adapter);

                String namesString = "";
                for (int i = 0; i < food.size(); i++) {
                    if(Objects.equals(food.get(i).getDate(), date)){
                        String name = food.get(i).getName();
                        double calories = food.get(i).getTotalCalories();
                        double protein = food.get(i).getTotalProtein();
                        int portion = food.get(i).getPortion();
                        namesString += name + ",";
                        namesString += calories + ",";
                        namesString += protein + ",";
                        namesString += portion + ",";
                    }
                }

                Log.d("names", namesString);
                ClientHandler clientHandler = new ClientHandler("saveFood," + email + "," + date + "," + namesString);

                System.out.println(foods.getTotalCalories());
                break;
            }
        }
        foodRead();


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
        foodRead();
        addIfEmpty();
    }

    public void backButton(View view){
        String[] dateData = DateHandler.getDate( "back", date);
        date = dateData[0];
        dateText.setText(dateData[1]);
        foodRead();
        addIfEmpty();
    }
}