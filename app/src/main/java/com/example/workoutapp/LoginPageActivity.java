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
import java.util.Locale;
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

    @Override
    protected void onStart() {
        super.onStart();
        observer();
        firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser != null) {
            userViewModel.initCurrentUser();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        appName = findViewById(R.id.appName);
        appName.findViewById(R.id.appName).setOnClickListener(this);
        database = FirebaseDatabase.getInstance().getReference();
        this.fragmentLayout = findViewById(R.id.fragmentLayout);
        this.userPrompt = findViewById(R.id.userPrompt);
        this.progressBar = findViewById(R.id.progressBar);
        this.mAuth = FirebaseAuth.getInstance();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    public void signIn(final String username, final String password){
        userViewModel.signIn(username, password);
    }

    public void register(String email, String password){
        userViewModel.register(email, password);

    }

    public void observer(){
        userViewModel.getUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null){
                    updateUI("Logging in.", false);
                    startActivity(firebaseUser);
                }
                else {
                    updateUI("Incorrect credentials", true);
                }
            }
        });
    }

    public void initUser(){
        userViewModel.initUser();
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
                            if (fragmentLayout.getVisibility() == View.VISIBLE){
                                fragmentLayout.setVisibility(View.GONE);
                                appName.setTextSize(50);
                            } else{
                                progressBar.setVisibility(View.GONE);
                                fragmentLayout.setVisibility(View.VISIBLE);
                                appName.setTextSize(36);
                            }
//                            onClick(appName);
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




}
