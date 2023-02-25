package com.example.finalproj;

public class User {

    String email;
    String name;
    int weight;

    public User(String email, String name, int weight) {
        this.email = email;
        this.name = name;
        this.weight = weight;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
