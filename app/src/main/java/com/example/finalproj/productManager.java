package com.example.finalproj;

public class productManager {
    String name;
    foodProduct productName;
    int portion;

    double totalCalories;
    double totalProtein;

    public productManager(String name, foodProduct productName, int portion) {
        this.name = name;
        this.productName = productName;
        this.portion = portion;
        this.totalCalories = productName.getcPg()*portion;
        this.totalProtein = productName.getpPg()*portion;
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

}
