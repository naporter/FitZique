package com.example.workoutapp.objects;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.workoutapp.BR;

public class Friend extends BaseObservable {
    private String firstName;
    private String lastName;
    private String uid;
    private int dailyPoints;
    private int weeklyPoints;
    private int lifetimePoints;

    public Friend(){
        //default constructor;
    }

    public Friend(String uid){
        this.uid = uid;
    }

    Friend(String firstName, String lastName, String uid){
        this.firstName = firstName;
        this.lastName = lastName;
        this.uid = uid;
    }

    @Bindable
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
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
