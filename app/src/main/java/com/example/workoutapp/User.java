package com.example.workoutapp;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class User {
    private int weeklyPoints;
    private int dailyPoints;
    private int lifetimePoints;
    private int weight;
    private int height;
    private int neckSize;
    private int waistSize;
    private int hipSize;
    private String birthday;
    private String gender;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private ArrayList<String> friends;

    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    User(int dailyPoints, int weeklyPoints, int lifetimePoints){
        this.setDailyPoints(dailyPoints);
        this.setWeeklyPoints(weeklyPoints);
        this.setLifetimePoints(lifetimePoints);
    }

    User(int weight, int height, int neckSize, int waistSize, int hipSize){
        this.setWeight(weight);
        this.setHeight(height);
        this.setNeckSize(neckSize);
        this.setWaistSize(waistSize);
        this.setHipSize(hipSize);
    }

    User(String email, String firstName, String lastName, String phoneNumber, String birthday, String gender){
        this.setEmail(email);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setPhoneNumber(phoneNumber);
        this.setBirthday(birthday);
        this.setGender(gender);
    }

    public void setDailyPoints(int points) {
        this.dailyPoints = points;
    }

    public int getDailyPoints(){
        return dailyPoints;
    }

    public int getWeeklyPoints() {
        return weeklyPoints;
    }

    public void setWeeklyPoints(int points) {
        this.weeklyPoints = points;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getLifetimePoints() {
        return lifetimePoints;
    }

    public void setLifetimePoints(int lifetimePoints) {
        this.lifetimePoints = lifetimePoints;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getNeckSize() {
        return neckSize;
    }

    public void setNeckSize(int neckSize) {
        this.neckSize = neckSize;
    }

    public int getWaistSize() {
        return waistSize;
    }

    public void setWaistSize(int waistSize) {
        this.waistSize = waistSize;
    }

    public int getHipSize() {
        return hipSize;
    }

    public void setHipSize(int hipSize) {
        this.hipSize = hipSize;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
