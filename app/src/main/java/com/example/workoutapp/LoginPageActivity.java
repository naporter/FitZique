package com.example.workoutapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.workoutapp.ui.logon.RegisterFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class LoginPageActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView appName;
    private ConstraintLayout fragmentLayout;
    private TextView userPrompt;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private FirebaseUser firebaseUser;
    private UserViewModel userViewModel;
    private Calendar calendar;
    private MutableLiveData<FirebaseUser> userMutableLiveData;

    @Override
    protected void onStart() {
        firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser != null) {
            startActivity(firebaseUser);
        }
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        appName = findViewById(R.id.appName);
        appName.findViewById(R.id.appName).setOnClickListener(this);
        database = FirebaseDatabase.getInstance().getReference();
        userMutableLiveData = new MutableLiveData<>();
        this.fragmentLayout = findViewById(R.id.fragmentLayout);
        this.userPrompt = findViewById(R.id.userPrompt);
        this.progressBar = findViewById(R.id.progressBar);
        this.mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null){
                    updateUI("Logging in.", false);
                    startActivity(firebaseUser);
//                    Date Functions
                    calendar = Calendar.getInstance();
                    SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd-yyyy");
                    final String dateString = sdf2.format(calendar.getTime());
                    checkForNewDay(dateString);
                    checkWeeklyDate(dateString);//see's if the weekly points need to be reset
                    checkMonthlyDate(dateString);//see's if the weekly points need to be reset.
                }
                else {
                    updateUI("Incorrect credentials", true);
                }
            }
        });
    }
    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void signIn(final String username, final String password){
        userViewModel.signIn(username, password);
    }

    public void register(String email, String password){
        userViewModel.register(email, password);

    }

    public void initMeasurements(int weight, int height, int neckSize, int waistSize, int hipSize, String  gender){
        userViewModel.initMeasurements(weight, height, neckSize, waistSize, hipSize, gender);
    }

    public void initDemographics(String email, String firstName, String lastName, String phoneNumber, String birthday, String gender){
        userViewModel.initDemographics(email, firstName, lastName, phoneNumber, birthday, gender);
    }

    public void updateUI(String message, Boolean error){ //updates the ui with a given message, considering adding another value for if error occurs and only update certain elements
        fragmentLayout.setVisibility(View.GONE);
        userPrompt.setText(message);
        progressBar.setVisibility(View.VISIBLE);
        appName.setTextSize(50);
        if(error){
            progressBar.setVisibility(View.GONE);
            Timer timer = new Timer(false);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onClick(appName);
                        }
                    });
                }
            }, 1000);
        }
    }

    public void startActivity(FirebaseUser firebaseUser){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("User", firebaseUser);
            startActivity(intent); //may need to bundle this classes instance of User to use in MainActivity
            finish();
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.appName: // keeping for future transitions
//                if (fragmentLayout.getVisibility() == View.VISIBLE){
//                    fragmentLayout.setVisibility(View.GONE);
//                    appName.setTextSize(50);
//                } else{
//                    progressBar.setVisibility(View.GONE);
//                    fragmentLayout.setVisibility(View.VISIBLE);
//                    appName.setTextSize(36);
//                }
//                break;
//        }
    }

    public void checkForNewDay(final String dateString){

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String SDF = snapshot.child("Dates").child("NextDay").getValue(String.class);
//                checks to see if the weekly points need to be reset
                if(SDF.equals(dateString)){
                    userViewModel.updateDailyDate();
                    database = FirebaseDatabase.getInstance().getReference("Users/" + userViewModel.getUserMutableLiveData().getValue().getUid() + "/points/dailyPoints");
                    database.setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Weekly Date not updated" + error);
            }
        });
    }

    public void checkWeeklyDate(final String dateString){

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String SDF = snapshot.child("Dates").child("EndOfWeek").getValue(String.class);
//                checks to see if the weekly points need to be reset
                if(SDF.equals(dateString)){
                    userViewModel.updateWeeklyDate();
                    database = FirebaseDatabase.getInstance().getReference("Users/" + userViewModel.getUserMutableLiveData().getValue().getUid() + "/points/weeklyPoints");
                    database.setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Weekly Date not updated" + error);
            }
        });
    }

    public void checkMonthlyDate(final String dateString){

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String SDF = snapshot.child("Dates").child("EndOfMonth").getValue(String.class);
//                checks to see if the weekly points need to be reset
                if(SDF.equals(dateString)){
                    userViewModel.updateMonthlyDate();
                    userViewModel.updateWeeklyDate();
                    database = FirebaseDatabase.getInstance().getReference("Users/" + userViewModel.getUserMutableLiveData().getValue().getUid() + "/points/weeklyPoints");
                    database.setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Monthly Date not updated" + error);
            }
        });
    }


}
