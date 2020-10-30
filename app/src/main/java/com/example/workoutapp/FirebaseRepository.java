package com.example.workoutapp;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Objects;

import static java.lang.Double.isNaN;

public class FirebaseRepository {

    private Application application;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference database;
    private MutableLiveData<FirebaseUser> userMutableLiveData;
    private MutableLiveData<FirebaseUser> registerUserLiveData;
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

    public FirebaseRepository(Application application){
        this.application = application;
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        userMutableLiveData = new MutableLiveData<>();
        registerUserLiveData = new MutableLiveData<>();
        dailyPoints = new MutableLiveData<>();
        weeklyPoints = new MutableLiveData<>();
        lifetimePoints = new MutableLiveData<>();
        mutableWeight = new MutableLiveData<>();
        mutableHeight = new MutableLiveData<>();
        mutableNeckSize = new MutableLiveData<>();
        mutableWaistSize = new MutableLiveData<>();
        mutableHipSize = new MutableLiveData<>();
        mutableGender = new MutableLiveData<>();
        mutableBirthday = new MutableLiveData<>();
        mutableBodyFat = new MutableLiveData<>();
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<FirebaseUser> getRegisterUserLiveData() {
        return registerUserLiveData;
    }

    public MutableLiveData<String> getMutableGender(){
        return mutableGender;
    }

    public MutableLiveData<Integer> getDailyPoints() {
        return dailyPoints;
    }

    public MutableLiveData<Integer> getWeeklyPoints() {
        return weeklyPoints;
    }

    public MutableLiveData<Integer> getLifetimePoints() {
        return lifetimePoints;
    }

    public MutableLiveData<Integer> getMutableWeight() {
        return mutableWeight;
    }

    public MutableLiveData<Integer> getMutableHeight() {
        return mutableHeight;
    }

    public MutableLiveData<Integer> getMutableNeckSize() {
        return mutableNeckSize;
    }

    public MutableLiveData<Integer> getMutableWaistSize() {
        return mutableWaistSize;
    }

    public MutableLiveData<Integer> getMutableHipSize() {
        return mutableHipSize;
    }

    public MutableLiveData<Double> getMutableBodyFat() {
        return mutableBodyFat;
    }

    public MutableLiveData<String> getMutableBirthday() {
        return mutableBirthday;
    }

    public void initCurrentUser(){
        userMutableLiveData.setValue(firebaseAuth.getCurrentUser());
    }

    public void register(String email, String password){ //for registering a new account only
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) { //new account created
                registerUserLiveData.setValue(firebaseAuth.getCurrentUser());
            }
        });
    }

    public void signIn(final String username, final String password){
        firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                userMutableLiveData.setValue(firebaseAuth.getCurrentUser());
            }
        });
    }

    public void signOut(){
        firebaseAuth.signOut();
    }

    public void initPoints(){ //for registering a new account only
        HashMap<String, Object> map = new HashMap<>();
        map.put("dailyPoints", 0);
        map.put("weeklyPoints", 0);
        map.put("lifetimePoints", 0);
        database.child("Users").child(registerUserLiveData.getValue().getUid()).child("points").setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dailyPoints.setValue(0);
                weeklyPoints.setValue(0);
                lifetimePoints.setValue(0);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void initMeasurements(int weight, int height, int neckSize, int waistSize, int hipSize){ //for registering a new account only
        DecimalFormat df = new DecimalFormat("####0.00");
        double bodyFat;
        if(getMutableGender().getValue().equals("Female")){
            bodyFat = 163.205 * Math.log10(waistSize + hipSize - neckSize) - 97.684 * Math.log10(height) + 36.76;
        }else{
            bodyFat = 86.010 * Math.log10(waistSize - neckSize) - 70.041 * Math.log10(height) + 36.76;
        }
        if (bodyFat < 0.1 || isNaN(bodyFat)){
            bodyFat = 0.1;
        }
        mutableBodyFat.postValue(Double.parseDouble(df.format(bodyFat)));
        final HashMap<String, Object> map = new HashMap<>();
        map.put("weight", weight);
        map.put("height", height);
        map.put("neckSize", neckSize);
        map.put("waistSize", waistSize);
        map.put("hipSize", hipSize);
        map.put("bodyFatPercent", bodyFat);
        database.child("Users").child(registerUserLiveData.getValue().getUid()).child("measurements").setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mutableWeight.postValue((Integer) map.get("weight"));
                mutableHeight.postValue((Integer) map.get("height"));
                mutableNeckSize.postValue((Integer) map.get("neckSize"));
                mutableWaistSize.postValue((Integer) map.get("waistSize"));
                mutableHipSize.postValue((Integer) map.get("hipSize"));
                mutableBodyFat.setValue((Double) map.get("bodyFatPercent"));
                userMutableLiveData.postValue(registerUserLiveData.getValue());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void initDemographics(String email, String firstName, String lastName, String phoneNumber, String birthday, String gender){ //for registering a new account only
        final HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("firstName", firstName);
        map.put("lastName", lastName);
        map.put("phoneNumber", phoneNumber);
        map.put("birthday", birthday);
        map.put("gender", gender);
        database.child("Users").child(registerUserLiveData.getValue().getUid()).child("demographics").setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mutableGender.postValue(map.get("gender"));
                mutableBirthday.postValue(map.get("birthday"));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void initBirthday(){
        userMutableLiveData.getValue().getUid();
        database = FirebaseDatabase.getInstance().getReference("Users/" + Objects.requireNonNull(userMutableLiveData.getValue()).getUid() + "/demographics/birthday");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //calculate the users age given their birthday and todays date
                mutableBirthday.setValue(snapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void initGender(){
        database = FirebaseDatabase.getInstance().getReference("Users/" + userMutableLiveData.getValue().getUid() + "/demographics/gender");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //updates the User class with the users gender and initializes body fat
                mutableGender.setValue(snapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void pointListener(){
        database = FirebaseDatabase.getInstance().getReference("Users/" + userMutableLiveData.getValue().getUid() + "/points");
        database.addValueEventListener(new ValueEventListener() { //sets the values for weekly points based on what is in the database
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //this method will run initially and again anytime data changes
                dailyPoints.setValue(snapshot.child("dailyPoints").getValue(int.class));
                weeklyPoints.setValue(snapshot.child("weeklyPoints").getValue(int.class));
                lifetimePoints.setValue(snapshot.child("lifetimePoints").getValue(int.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void measurementListener(){
        database = FirebaseDatabase.getInstance().getReference("Users/" + Objects.requireNonNull(userMutableLiveData.getValue()).getUid() + "/measurements");
        database.addValueEventListener(new ValueEventListener() { //sets the values for the users measurements based on what is in the database
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //this method will run initially and again anytime data changes
                //not a final approach. resets all values even if just one changes. TODO: find a way to set only the value that changes.
                mutableHeight.setValue(snapshot.child("height").getValue(int.class));
                mutableHipSize.setValue(snapshot.child("hipSize").getValue(int.class));
                mutableNeckSize.setValue(snapshot.child("neckSize").getValue(int.class));
                mutableWaistSize.setValue(snapshot.child("waistSize").getValue(int.class));
                mutableWeight.setValue(snapshot.child("weight").getValue(int.class));
                mutableBodyFat.setValue(snapshot.child("bodyFatPercent").getValue(double.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public int updatePoints(int difficulty, int numReps){ //computes and sets the users new point value. only used after a user does an exercise
        double bodyFat = getMutableBodyFat().getValue();
        int points = (int) Math.round(numReps * difficulty - (difficulty * numReps * (1 - (bodyFat / 100)))); //algorithm that computes points for given exercise
        int currentDaily = getDailyPoints().getValue();
        int currentWeekly = getWeeklyPoints().getValue();
        int currentLifetime = getLifetimePoints().getValue();
        dailyPoints.setValue(currentDaily + points);
        weeklyPoints.setValue(currentWeekly + points);
        lifetimePoints.setValue(currentLifetime + points);
        database = FirebaseDatabase.getInstance().getReference("Users/" + Objects.requireNonNull(userMutableLiveData.getValue()).getUid() + "/points/dailyPoints");
        database.setValue(currentDaily + points);
        database = FirebaseDatabase.getInstance().getReference("Users/" + userMutableLiveData.getValue().getUid() + "/points/weeklyPoints");
        database.setValue(currentWeekly + points);
        database = FirebaseDatabase.getInstance().getReference("Users/" + userMutableLiveData.getValue().getUid() + "/points/lifetimePoints");
        database.setValue(currentLifetime + points);
        return points;
    }

    public void updateMeasurement(String source, int measurement){ //updates the users measurement. This will only update the measurement that is changed
        switch (source){
            case "height":
                mutableHeight.setValue(measurement);
                break;
            case "hipSize":
                mutableHipSize.setValue(measurement);
                break;
            case "neckSize":
                mutableNeckSize.setValue(measurement);
                break;
            case "waistSize":
                mutableWaistSize.setValue(measurement);
                break;
            case "weight":
                mutableWeight.setValue(measurement);
                break;
        }
        database = FirebaseDatabase.getInstance().getReference("Users/" + userMutableLiveData.getValue().getUid() + "/measurements/" + source);
        database.setValue(measurement);
        updateBodyFat();
    }

    public void updateBodyFat(){
        DecimalFormat df = new DecimalFormat("####0.00");
        double bodyFat;
        if(getMutableGender().getValue().equals("Female")){
            bodyFat = 163.205 * Math.log10(getMutableWaistSize().getValue() + getMutableHipSize().getValue() - getMutableNeckSize().getValue()) - 97.684 * Math.log10(getMutableHeight().getValue()) + 36.76;
        }else{
            bodyFat = 86.010 * Math.log10(getMutableWaistSize().getValue() - getMutableNeckSize().getValue()) - 70.041 * Math.log10(getMutableHeight().getValue()) + 36.76;
        }
        if (bodyFat < 0.1 || isNaN(bodyFat)){
            bodyFat = 0.1;
        }
        mutableBodyFat.postValue(Double.parseDouble(df.format(bodyFat)));
        database = FirebaseDatabase.getInstance().getReference("Users/" + userMutableLiveData.getValue().getUid() + "/measurements/bodyFatPercent");
        database.setValue(Double.parseDouble(df.format(bodyFat)));
    }

}
