package com.example.workoutapp;

import java.util.ArrayList;

public class User {
    private String email;
    private String points;
    private ArrayList<String> friends;
    private String firstName;
    private String lastName;

    public User(){

    }

    public User(String email, String points, String firstName, String lastName){
        this.setEmail(email);
        this.setPoints(points);
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
