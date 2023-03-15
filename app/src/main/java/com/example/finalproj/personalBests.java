package com.example.finalproj;

public class personalBests {

    String name;
    int weight;

    String date;

    public personalBests(String name, int weight, String date) {
        this.name = name;
        this.weight = weight;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}

