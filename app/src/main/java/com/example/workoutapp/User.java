package com.example.workoutapp;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class User extends BaseObservable {
    //authentication
    public String UID;
    //demographics
    public String birthday;
    public String firstName;
    public String gender;
    public String lastName;
    public String phoneNumber;
    //friends
    private ArrayList<Friend> friends;
    //measurements
    public double bodyFatPercent;
    public int height;
    public int hipSize;
    public int neckSize;
    public int waistSize;
    public int weight;
    //points
    public MutableLiveData<Integer> dailyPoints;
    public MutableLiveData<Integer> lifetimePoints;
    public MutableLiveData<Integer> weeklyPoints;

    public User(){
        friends = new ArrayList<>();
        dailyPoints = new MutableLiveData<>();
        lifetimePoints = new MutableLiveData<>();
        weeklyPoints = new MutableLiveData<>();
    }


    public void setUser(FirebaseUser user) {
        this.setUID(user.getUid());
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Bindable
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

    @Bindable
    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }

    @Bindable
    public double getBodyFatPercent() {
        return bodyFatPercent;
    }

    public void setBodyFatPercent(double bodyFatPercent) {
        this.bodyFatPercent = bodyFatPercent;
        notifyPropertyChanged(BR.bodyFatPercent);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHipSize() {
        return hipSize;
    }

    public void setHipSize(int hipSize) {
        this.hipSize = hipSize;
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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public MutableLiveData<Integer> getDailyPoints() {
        return dailyPoints;
    }

    public void setDailyPoints(int dailyPoints) {
        this.dailyPoints.postValue(dailyPoints);
    }

    public MutableLiveData<Integer> getLifetimePoints() {
        return lifetimePoints;
    }

    public void setLifetimePoints(int lifetimePoints) {
        this.lifetimePoints.postValue(lifetimePoints);
    }

    public MutableLiveData<Integer> getWeeklyPoints() {
        return weeklyPoints;
    }

    public void setWeeklyPoints(int weeklyPoints) {
        this.weeklyPoints.postValue(weeklyPoints);
    }
}
