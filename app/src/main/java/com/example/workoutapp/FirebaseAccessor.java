package com.example.workoutapp;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class FirebaseAccessor {

    private final Application application;
    private static FirebaseAccessor instance;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference database;

    private final User user = new User();

    FirebaseAccessor(Application application){
        this.application = application;
    }

    public static FirebaseAccessor getInstance(Application application){
        if(instance == null){
            instance = new FirebaseAccessor(application);
        }
        return instance;
    }

    public MutableLiveData<User> getUser(){
        MutableLiveData<User> user = new MutableLiveData<>();
        database = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        user.setValue(this.user);
        initUser();
        return user;
    }


    //Methods only ran when registering

    public Boolean checkIfUserExists(final String email){
        if (firebaseAuth.fetchSignInMethodsForEmail(email).isSuccessful()){
            return true;
        }
        return false;
    }


    public void register(final String email, String password, final String phoneNumber, final String birthday, final String firstName, final String lastName, final String gender,
                         final double bodyFatPercent, final int weight, final int height, final int neckSize, final int waistSize, final int hipSize){ //for registering a new account only
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) { //new account created
                if(task.isSuccessful()){
                    Toast.makeText(application.getApplicationContext(), "User created successfully.", Toast.LENGTH_LONG).show();
                    initPoints(firebaseAuth.getUid());
                    initUserByNumber(firebaseAuth.getUid(), phoneNumber);
                    initDemographics(birthday, email, firstName, lastName, gender, phoneNumber);
                    initMeasurements(bodyFatPercent, weight, height, neckSize, waistSize, hipSize);
                }else {
                    Toast.makeText(application.getApplicationContext(), "User already exists.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void initDemographics(String birthday, String email, String firstName, String lastName, String gender, String phoneNumber){
        HashMap<String, Object> map = new HashMap<>();
        map.put("birthday", birthday);
        map.put("email", email);
        map.put("firstName", firstName);
        map.put("lastName", lastName);
        map.put("gender", gender);
        map.put("phoneNumber", phoneNumber);
        database.child("Users").child(Objects.requireNonNull(firebaseAuth.getUid())).child("demographics").setValue(map);
    }

    public void initMeasurements(double bodyFatPercent, int weight, int height, int neckSize, int waistSize, int hipSize){
        HashMap<String, Object> map = new HashMap<>();
        map.put("bodyFatPercent", bodyFatPercent);
        map.put("weight", weight);
        map.put("height", height);
        map.put("neckSize", neckSize);
        map.put("waistSize", waistSize);
        map.put("hipSize", hipSize);
        database.child("Users").child(Objects.requireNonNull(firebaseAuth.getUid())).child("measurements").setValue(map);
    }

    public void initPoints(String userUID){ //for registering a new account only
        HashMap<String, Object> map = new HashMap<>();
        map.put("dailyPoints", 0);
        map.put("weeklyPoints", 0);
        map.put("lifetimePoints", 0);
        database.child("Users").child(userUID).child("points").setValue(map);
    }

    public void initUserByNumber(String userUID, String phoneNumber){
        database.child("UsersByNumber").child(phoneNumber).setValue(userUID);
    }

    //Methods ran upon sign in

    public void signIn(final String email, final String password){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(application.getApplicationContext(), "Successfully signed in.", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(application.getMainExecutor(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(application.getApplicationContext(), "Incorrect credentials.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void initUser(){
        if (firebaseAuth.getCurrentUser() == null){ //fail safe
            return;
        }
        retrieveUserDemographics(firebaseAuth.getUid());
        pointListener(firebaseAuth.getUid());
        measurementListener(firebaseAuth.getUid());
        friendListener(firebaseAuth.getUid());
        checkForPointReset();
    }

    public void retrieveUserDemographics(String userUID){
        database.child("Users").child(userUID).child("demographics").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user.setBirthday(snapshot.child("birthday").getValue(String.class));
                user.setFirstName(snapshot.child("firstName").getValue(String.class));
                user.setGender(snapshot.child("gender").getValue(String.class));
                user.setLastName(snapshot.child("lastName").getValue(String.class));
                user.setPhoneNumber(snapshot.child("phoneNumber").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(application.getApplicationContext(), "Failed to retrieve demographics.", Toast.LENGTH_LONG).show();
            }
        });
    }


    //Listener methods


    public void pointListener(String userUID){
        database.child("Users").child(userUID).child("points").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user.setDailyPoints(snapshot.child("dailyPoints").getValue(int.class));
                user.setLifetimePoints(snapshot.child("lifetimePoints").getValue(int.class));
                user.setWeeklyPoints(snapshot.child("weeklyPoints").getValue(int.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(application.getApplicationContext(), "Point listener stopped.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void measurementListener(String userUID){
        database.child("Users").child(userUID).child("measurements").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user.setBodyFatPercent(snapshot.child("bodyFatPercent").getValue(double.class));
                user.setHeight(snapshot.child("height").getValue(int.class));
                user.setHipSize(snapshot.child("hipSize").getValue(int.class));
                user.setNeckSize(snapshot.child("neckSize").getValue(int.class));
                user.setWaistSize(snapshot.child("waistSize").getValue(int.class));
                user.setWeight(snapshot.child("weight").getValue(int.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(application.getApplicationContext(), "Measurement listener stopped.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void friendListener(String userUID){
        database.child("Users").child(userUID).child("friends").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (!isFriend(snapshot.getKey())){
                    user.getFriends().add(new Friend(snapshot.getKey()));
                    retrieveFriendName(snapshot.getKey());
                    friendPointListener(snapshot.getKey());
                    System.out.println("Running child added");
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(application.getApplicationContext(), "Friend listener stopped", Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean isFriend(String friendCode){
        //checks to see if friend is already in friend list.
        for(Friend friend : user.getFriends()){
             if (friend.getUid().equals(friendCode)){
                 return true;
             }
        }
        return false;
    }

    public void friendPointListener(final String friendUID){
        database.child("Users").child(friendUID).child("points").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(Friend friend : user.getFriends()){
                    if (friend.getUid().equals(friendUID)){
                        friend.setDailyPoints(snapshot.child("dailyPoints").getValue(int.class));
                        friend.setLifetimePoints(snapshot.child("lifetimePoints").getValue(int.class));
                        friend.setWeeklyPoints(snapshot.child("weeklyPoints").getValue(int.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(application.getApplicationContext(), "Friend point listener stopped", Toast.LENGTH_LONG).show();
            }
        });
    }


    //Update values methods


    public void updateMeasurement(String userUID, String measurement, int value){
        switch (measurement){
            case "height":
                user.setHeight(value);
                break;
            case "weight":
                user.setWeight(value);
                break;
            case "hipSize":
                user.setHipSize(value);
                break;
            case "neckSize":
                user.setNeckSize(value);
                break;
            case "waistSize":
                user.setWaistSize(value);
                break;
        }
        database.child("Users").child(userUID).child("measurements").child(measurement).setValue(value);
    }

    public void updatePoints(String userUID, int value){
        database.child("Users").child(userUID).child("points").child("dailyPoints").setValue(user.getDailyPoints().getValue() + value);
        user.getDailyPoints().postValue(user.getDailyPoints().getValue() + value);
        database.child("Users").child(userUID).child("points").child("lifetimePoints").setValue(user.getLifetimePoints().getValue() + value);
        user.getLifetimePoints().postValue(user.getLifetimePoints().getValue() + value);
        database.child("Users").child(userUID).child("points").child("weeklyPoints").setValue(user.getWeeklyPoints().getValue() + value);
        user.getWeeklyPoints().postValue(user.getWeeklyPoints().getValue() + value);
        Toast.makeText(application, value + " points awarded!", Toast.LENGTH_SHORT).show();
    }

    public void updateBodyFat(String userUID, double bodyFat){
        user.setBodyFatPercent(bodyFat);
        database.child("Users").child(userUID).child("measurements").child("bodyFatPercent").setValue(bodyFat);
    }

    public void checkForPointReset(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        final String today = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DATE, 7);
        final String startOfWeek = dateFormat.format(calendar.getTime());
        database.child("Dates").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!today.equals(snapshot.child("today").getValue())){
                    updateDailyPoints();
                    database.child("Dates").child("today").setValue(today);
                    if(today.equals(snapshot.child("startOfWeek").getValue())){
                        updateWeeklyPoints();
                        database.child("Dates").child("startOfWeek").setValue(startOfWeek);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateDailyPoints(){
        database.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot user : snapshot.getChildren()){
                    database.child("Users").child(Objects.requireNonNull(user.getKey())).child("points").child("dailyPoints").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateWeeklyPoints(){
        database.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot user : snapshot.getChildren()){
                    database.child("Users").child(Objects.requireNonNull(user.getKey())).child("points").child("weeklyPoints").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //Friend related methods


    public void addFriend(final String userUID, final String friendPhoneNumber){
        if (friendPhoneNumber.equals(user.getPhoneNumber())){
            Toast.makeText(application, "You cannot add yourself as a friend.", Toast.LENGTH_SHORT).show();
            return;
        }
        database.child("UsersByNumber").child(friendPhoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){ //user with the phone number found.
                    database.child("Users").child(userUID).child("friends").child(Objects.requireNonNull(snapshot.getValue(String.class))).setValue(true);
                }else{
                    Toast.makeText(application, "User with phone number " + friendPhoneNumber + " does not exist.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void removeFriend(String userUID, String friendUID){
        database.child("Users").child(userUID).child("friends").child(friendUID).removeValue();
    }

    public void retrieveFriendName(final String friendUID){
        database.child("Users").child(friendUID).child("demographics").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(Friend friend : user.getFriends()){
                    if (friend.getUid().equals(friendUID)){
                        friend.setFirstName(snapshot.child("firstName").getValue(String.class));
                        friend.setLastName(snapshot.child("lastName").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
