package com.example.workoutapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class LoginPageActivity extends AppCompatActivity {

    private TextView appName;
    private ConstraintLayout fragmentLayout;
    private TextView userPrompt;
    private ProgressBar progressBar;
    private FirebaseAccessor firebaseAccessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        appName = findViewById(R.id.appName);
        this.fragmentLayout = findViewById(R.id.fragmentLayout);
        this.userPrompt = findViewById(R.id.userPrompt);
        this.progressBar = findViewById(R.id.progressBar);
        this.firebaseAccessor = FirebaseAccessor.getInstance(getApplication());

//        Objects.requireNonNull(firebaseAccessor.getUser().getValue()).getUser().observe(this, new Observer<FirebaseUser>() {
//            @Override
//            public void onChanged(FirebaseUser firebaseUser) {
//                if(FirebaseAuth.getInstance().getCurrentUser() != null){
//                    updateUI("Logging in.", false);
//                }else {
//                    updateUI("Failed to login", true);
//                }
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        signInListener();
    }

    public void signInListener(){
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    updateUI("Logging in.", false);
                }
            }
        };
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
    }

    public FirebaseAccessor getFirebaseAccessor(){
        return firebaseAccessor;
    }

    public Boolean checkIfUserExists(final String email){
        Toast.makeText(this, "Checking if email is available...", Toast.LENGTH_SHORT).show();
        if (firebaseAccessor.checkIfUserExists(email)){
            return true;
        }
        return false;
    }

    public void signIn(final String email, final String password){
        firebaseAccessor.signIn(email, password);
    }

    public void register(String email, String password, String phoneNumber, String birthday, String firstName, String lastName, String gender,
                        double bodyFatPercent, int weight, int height, int neckSize, int waistSize, int hipSize){
        firebaseAccessor.register(email, password, phoneNumber, birthday, firstName, lastName, gender, bodyFatPercent, weight, height, neckSize, waistSize, hipSize);
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
                        }
                    });
                }
            }, 1000);
        }else{
            startMainActivity();
        }
    }

    public void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
