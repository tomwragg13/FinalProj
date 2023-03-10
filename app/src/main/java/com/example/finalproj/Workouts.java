package com.example.finalproj;

public class Workouts {

    String name;
    int reps;
    int weight;
    int sets;
    double calories;

    double bodyW;

    String date;

    public Workouts(String name, int reps, int weight, int sets, double bodyW, String date) {
        this.name = name;
        this.reps = reps;
        this.weight = weight;
        this.sets = sets;
        this.calories = (weight/bodyW)*0.75*reps*sets;
        this.bodyW = bodyW;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public double getCalories() {
        return calories;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReps() {
        return reps;
    }

    public double getBodyW() {
        return bodyW;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public String getDate() {
        return date;
    }
}
