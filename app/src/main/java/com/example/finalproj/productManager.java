package com.example.finalproj;

public class productManager {
    String name;
    foodProduct productName;
    int portion;

    double totalCalories;
    double totalProtein;

    String date;

    public productManager(String name, foodProduct productName, int portion, String date) {
        this.name = name;
        this.productName = productName;
        this.portion = portion;
        this.totalCalories = productName.getcPg()*portion;
        this.totalProtein = productName.getpPg()*portion;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public foodProduct getProductName() {
        return productName;
    }

    public int getPortion() {
        return portion;
    }

    public double getTotalCalories() {
        return totalCalories;
    }

    public double getTotalProtein() {
        return totalProtein;
    }

    public String getDate() {
        return date;
    }
}
