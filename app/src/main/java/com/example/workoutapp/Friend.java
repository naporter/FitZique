package com.example.workoutapp;

public class Friend {
    private String firstName;
    private String lastName;
    private String uid;
    private int dailyPoints;
    private int weeklyPoints;
    private int lifetimePoints;

    Friend(){
        //default constructor;
    }

    Friend(String uid){
        this.uid = uid;
    }

    Friend(String firstName, String lastName, String uid){
        this.firstName = firstName;
        this.lastName = lastName;
        this.uid = uid;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getDailyPoints() {
        return dailyPoints;
    }

    public void setDailyPoints(int dailyPoints) {
        this.dailyPoints = dailyPoints;
    }

    public int getWeeklyPoints() {
        return weeklyPoints;
    }

    public void setWeeklyPoints(int weeklyPoints) {
        this.weeklyPoints = weeklyPoints;
    }

    public int getLifetimePoints() {
        return lifetimePoints;
    }

    public void setLifetimePoints(int lifetimePoints) {
        this.lifetimePoints = lifetimePoints;
    }
}
