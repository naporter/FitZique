package com.example.workoutapp;

import android.app.Application;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Calendar;
import java.text.DateFormat;

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
    private MutableLiveData<ArrayList<String>> friends;
    private MutableLiveData<ArrayList<String>> friendFirstNames;
    private MutableLiveData<ArrayList<String>> friendLastNames;

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
        friends = new MutableLiveData<>();
        friendFirstNames = new MutableLiveData<>();
        friendLastNames = new MutableLiveData<>();

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

    public MutableLiveData<ArrayList<String>> getFriends() {
        return friends;
    }

    public MutableLiveData<ArrayList<String>> getFriendFirstNames() {
        return friendFirstNames;
    }

    public MutableLiveData<ArrayList<String>> getFriendLastNames() {
        return friendLastNames;
    }

    public void initCurrentUser(){
        userMutableLiveData.setValue(firebaseAuth.getCurrentUser());
    }

    public void register(String email, String password){ //for registering a new account only
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) { //new account created
                registerUserLiveData.setValue(firebaseAuth.getCurrentUser());
                initPoints();
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

    public void initMeasurements(int weight, int height, int neckSize, int waistSize, int hipSize, String gender){ //for registering a new account only
        DecimalFormat df = new DecimalFormat("####0.00");
        double bodyFat;
        if(gender.equals("Female")){
            bodyFat = 163.205 * Math.log10(waistSize + hipSize - neckSize) - 97.684 * Math.log10(height) + 36.76;
        }else{
            bodyFat = 86.010 * Math.log10(waistSize - neckSize) - 70.041 * Math.log10(height) + 36.76;
        }
        if (bodyFat < 0.1 || isNaN(bodyFat)){
            bodyFat = 0.1;
        }
        bodyFat = Double.parseDouble(df.format(bodyFat)); //reduce body fat to two decimal places
        mutableBodyFat.postValue(bodyFat);
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
        database.child("UsersByNumber").child(phoneNumber).setValue(Objects.requireNonNull(registerUserLiveData.getValue()).getUid()); //adds user to UsersByNumber child
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
                //not a final approach. resets all values even if just one changes. TODO: find a way to set only the value that changes
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

    public void friendsListener(){
        database = FirebaseDatabase.getInstance().getReference("Users/" + Objects.requireNonNull(userMutableLiveData.getValue()).getUid() + "/friends");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> friends = new ArrayList<>();
                final ArrayList<String> friendsFirstNames = new ArrayList<>();
                final ArrayList<String> friendsLastNames = new ArrayList<>();
//                for(DataSnapshot child : snapshot.getChildren()) {
//                    String friendCode = child.getKey();
//                    friends.add(friendCode);
//                }
//                getFriends().postValue(friends);
                for(DataSnapshot child : snapshot.getChildren()){
                    String friendCode = child.getKey();
                    friends.add(friendCode);
                    database = FirebaseDatabase.getInstance().getReference("Users/" + friendCode + "/demographics/");
                    database.child("firstName").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            friendsFirstNames.add(snapshot.getValue(String.class));
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            System.out.println("Friend not found");
                        }
                    });
                    database.child("lastName").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            friendsLastNames.add(snapshot.getValue(String.class));
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            System.out.println("Friend not found");
                        }
                    });
                }
                getFriends().postValue(friends);
                getFriendFirstNames().postValue(friendsFirstNames);
                getFriendLastNames().postValue(friendsLastNames);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("No friends to display");
            }
        });
    }

    public void addFriend(String phoneNumber){
        database = FirebaseDatabase.getInstance().getReference("UsersByNumber/" + phoneNumber);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String newFriendUID = snapshot.getValue(String.class);
                database = FirebaseDatabase.getInstance().getReference("Users/" + Objects.requireNonNull(userMutableLiveData.getValue()).getUid() + "/friends/" + newFriendUID);
                database.setValue(true);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed to add friend. " + error);
            }
        });
    }

    public void removeFriend(final int index){
        database = FirebaseDatabase.getInstance().getReference("Users/" + Objects.requireNonNull(userMutableLiveData.getValue()).getUid() + "/friends/" + friends.getValue().get(index));
        database.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
//                friends.getValue().remove(index);
//                friendFirstNames.getValue().remove(index);
//                friendLastNames.remove(index);
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

    public void updateDailyDate(){
//      Creates weekly start and end date for when the points need updating.
        Calendar calendar = Calendar.getInstance();

        final String startDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            calendar.setTime(sdf.parse(startDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
        final String output = sdf1.format(calendar.getTime());

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                database = FirebaseDatabase.getInstance().getReference("Dates/Daily");
                database.setValue(output);

                Calendar calendar = Calendar.getInstance();

                calendar.add(Calendar.DATE, 1);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
                final String output = sdf1.format(calendar.getTime());
                database = FirebaseDatabase.getInstance().getReference("Dates/NextDay");
                database.setValue(output);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Daily Date not updated" + error);
            }
        });
    }

    public void updateWeeklyDate(){
//      Creates weekly start and end date for when the points need updating.
        Calendar calendar = Calendar.getInstance();

        final String startDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            calendar.setTime(sdf.parse(startDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //        advance one week
        calendar.add(Calendar.DATE, 7);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
        final String output = sdf1.format(calendar.getTime());

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                database = FirebaseDatabase.getInstance().getReference("Dates/EndOfWeek");
                database.setValue(output);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Weekly Date not updated" + error);
            }
        });
    }

    public void updateMonthlyDate(){

        Calendar calendar = Calendar.getInstance();
        //        advance one month
        calendar.add(Calendar.MONTH, 1);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd-yyyy");
        final String output = sdf2.format(calendar.getTime());

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                database = FirebaseDatabase.getInstance().getReference("Dates/EndOfMonth");
                database.setValue(output);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Monthly Date not updated" + error);
            }
        });
    }

}
