package com.example.finalproj;

public class foodProduct {

    String name;
    double calories;
    double protein;
    int size;

    double cPg;
    double pPg;

    public foodProduct(String name, double calories, double protein, int size) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.size = size;
        this.cPg = calories/size;
        this.pPg = protein/size;
    }

    public String getName() {
        return name;
    }

    public double getCalories() {
        return calories;
    }

    public double getProtein() {
        return protein;
    }

    public int getSize() {
        return size;
    }

    public double getcPg() {
        return cPg;
    }

    public double getpPg() {
        return pPg;
    }

}
