package com.example.workoutapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class LoginPageActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView appName;
    private ConstraintLayout fragmentLayout;
    private TextView userPrompt;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = mAuth.getCurrentUser();
        //TODO: handle event when user is already signed in
//        updateUI(currentUser.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        appName = findViewById(R.id.appName);
        appName.findViewById(R.id.appName).setOnClickListener(this);
        this.fragmentLayout = findViewById(R.id.fragmentLayout);
        this.userPrompt = findViewById(R.id.userPrompt);
        this.progressBar = findViewById(R.id.progressBar);
        this.mAuth = FirebaseAuth.getInstance();
    }
    public void signIn(final String username, final String password){
        updateUI("Checking credentials...", true);
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    updateUI("Login Successful.\nRetrieving your data... Please wait.", true);
                    firebaseUser = mAuth.getCurrentUser();
                    startActivity(firebaseUser);
                } else {
                    userPrompt.setText("Incorrect credentials.");
                    //TODO: handle event when credentials are incorrect
                }
            }
        });
    }

    public void createAccount(final String email, String password, final String firstName, final String lastName, final String phoneNumber,
                              final int weight, final int height, final int neckSize, final int waistSize, final int hipSize, final String birthday,
                              final String gender){ //creates users account
        updateUI("Creating account...", true);
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    firebaseUser = mAuth.getCurrentUser();
                    database = FirebaseDatabase.getInstance().getReference();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("dailyPoints", 0);
                    map.put("weeklyPoints", 0);
                    map.put("lifetimePoints", 0);
                    database.child("Users").child(firebaseUser.getUid()).child("points").setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            updateUI("Points initialized.", false);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            updateUI("An error has occurred while initializing points: " + e.toString(), true);
                        }
                    });
                    map.clear();
                    map.put("weight", weight);
                    map.put("height", height);
                    map.put("neckSize", neckSize);
                    map.put("waistSize", waistSize);
                    map.put("hipSize", hipSize);
                    database.child("Users").child(firebaseUser.getUid()).child("measurements").setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            updateUI("Setting your measurements.", false);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            updateUI("An error has occurred while setting measurements: " + e.toString(), true);
                        }
                    });
                    map.clear();
                    map.put("email", email);
                    map.put("firstName", firstName);
                    map.put("lastName", lastName);
                    map.put("phoneNumber", phoneNumber);
                    map.put("birthday", birthday);
                    map.put("gender", gender);
                    database.child("Users").child(firebaseUser.getUid()).child("demographics").setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            updateUI("Demographics added.", false);
                            updateUI("Account created successfully.", true);
                            startActivity(firebaseUser);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            updateUI("An error has occurred: " + e.toString(), true);
                        }
                    });
                }else{
                    //TODO: handle event of user already exists
                    updateUI("A user with " + email + " email already exists.", true);
                }
            }
        });
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

    public void displayError(String error){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.appName: // keeping for future transitions
                if (fragmentLayout.getVisibility() == View.VISIBLE){
                    fragmentLayout.setVisibility(View.GONE);
                    appName.setTextSize(50);
                } else{
                    progressBar.setVisibility(View.GONE);
                    fragmentLayout.setVisibility(View.VISIBLE);
                    appName.setTextSize(36);
                }
                break;
        }
    }
}
