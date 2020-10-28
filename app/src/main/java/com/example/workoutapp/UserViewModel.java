package com.example.workoutapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.auth.FirebaseUser;

public class UserViewModel extends AndroidViewModel {

    private MutableLiveData<FirebaseUser> userMutableLiveData;
    private MutableLiveData<FirebaseUser> registerUserLiveData;
    private FirebaseRepository firebaseRepository;
    private MutableLiveData<Integer> dailyPoints;
    private MutableLiveData<Integer> weeklyPoints;
    private MutableLiveData<Integer> lifetimePoints;
    private MutableLiveData<Integer> mutableWeight;
    private MutableLiveData<Integer> mutableHeight;
    private MutableLiveData<Integer> mutableNeckSize;
    private MutableLiveData<Integer> mutableWaistSize;
    private MutableLiveData<Integer> mutableHipSize;
    public MutableLiveData<Double> mutableBodyFat;
    public MutableLiveData<String> mutableGender;
    private MutableLiveData<String> mutableBirthday;
    private Application application;

    public UserViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        firebaseRepository = new FirebaseRepository(application);
        userMutableLiveData = firebaseRepository.getUserMutableLiveData();
        registerUserLiveData = firebaseRepository.getRegisterUserLiveData();
        dailyPoints = firebaseRepository.getDailyPoints();
        weeklyPoints = firebaseRepository.getWeeklyPoints();
        lifetimePoints = firebaseRepository.getLifetimePoints();
        mutableWeight = firebaseRepository.getMutableWeight();
        mutableHeight = firebaseRepository.getMutableHeight();
        mutableNeckSize = firebaseRepository.getMutableNeckSize();
        mutableWaistSize = firebaseRepository.getMutableWaistSize();
        mutableHipSize = firebaseRepository.getMutableHipSize();
        mutableGender = firebaseRepository.getMutableGender();
        mutableBirthday = firebaseRepository.getMutableBirthday();
        mutableBodyFat = firebaseRepository.getMutableBodyFat();
    }

    public LiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public LiveData<FirebaseUser> getRegisterUserLiveData() {
        return registerUserLiveData;
    }

    public LiveData<Integer> getDailyPoints() {
        return dailyPoints;
    }

    public LiveData<Integer> getWeeklyPoints() {
        return weeklyPoints;
    }

    public LiveData<Integer> getLifetimePoints() {
        return lifetimePoints;
    }

    public LiveData<Integer> getMutableWeight() {
        return mutableWeight;
    }

    public LiveData<Integer> getMutableHeight() {
        return mutableHeight;
    }

    public LiveData<Integer> getMutableNeckSize() {
        return mutableNeckSize;
    }

    public LiveData<Integer> getMutableWaistSize() {
        return mutableWaistSize;
    }

    public LiveData<Integer> getMutableHipSize() {
        return mutableHipSize;
    }

    public LiveData<Double> getMutableBodyFat() {
        return mutableBodyFat;
    }

    public MutableLiveData<String> getMutableGender() {
        return mutableGender;
    }

    public void register(String email, String password){
        firebaseRepository.register(email, password);
    }

    public void signIn(String username, String password){
        firebaseRepository.signIn(username, password);
    }

    public void signOut(){
        firebaseRepository.signOut();
    }

    public void initPoints(){
        firebaseRepository.initPoints();
    }

    public void initMeasurements(int weight, int height, int neckSize, int waistSize, int hipSize){
        firebaseRepository.initMeasurements(weight, height, neckSize, waistSize, hipSize);
    }

    public void initDemographics(String email, String firstName, String lastName, String phoneNumber, String birthday, String gender){
        firebaseRepository.initDemographics(email, firstName, lastName, phoneNumber, birthday, gender);
    }

    public void initUser(){
        initCurrentUser();
        initBirthday();
        initGender();
        measurementListener();
        pointListener();
    }

    public void initCurrentUser(){
        firebaseRepository.initCurrentUser();
    }

    public void initBirthday(){
        firebaseRepository.initBirthday();
    }

    public void initGender(){
        firebaseRepository.initGender();
    }

    public void pointListener(){
        firebaseRepository.pointListener();
    }

    public void measurementListener(){
        firebaseRepository.measurementListener();
    }

    public void updateMeasurement(String source, int measurement){
        firebaseRepository.updateMeasurement(source, measurement);
    }

    public int updatePoints(int difficulty, int numReps){
        int points = firebaseRepository.updatePoints(difficulty, numReps);
        return points;
    }
}