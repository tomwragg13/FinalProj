package com.example.finalproj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MacrosActivity extends AppCompatActivity {

    RecyclerView mealRecycler;
    mealAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macros);

        mealRecycler = (RecyclerView) findViewById(R.id.mealRecycler);

        //ArrayList<productManager> food = new ArrayList<productManager>();

        foodProduct pringles = new foodProduct("Pringles", 154, 1.7, 30);
        productManager Pringles = new productManager("Pringles", pringles, 30);
        productManager Pringles2 = new productManager("Pringles", pringles, 600);
        List<productManager> food = Arrays.asList(Pringles, Pringles2);
        Log.d("Pringles", String.valueOf(Pringles.getTotalCalories()));
        Log.d("Pringles", String.valueOf(Pringles2.getTotalCalories()));

        mealRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapter = new mealAdapter(getApplicationContext(), food);
        mealRecycler.setAdapter(adapter);

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
}