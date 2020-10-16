package com.example.workoutapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginPageActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView appName;
    private ConstraintLayout fragmentLayout;
    private TextView userPrompt;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private FirebaseUser firebaseUser;
    private User thisUser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = mAuth.getCurrentUser();
        thisUser = new User();
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
        updateUI("Checking credentials...");
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    updateUI("Login Successful.\nRetrieving your data... Please wait.");
                    firebaseUser = mAuth.getCurrentUser();
                    database = FirebaseDatabase.getInstance().getReference("Users/" + firebaseUser.getUid());
                    startActivity(firebaseUser);
                } else {
                    userPrompt.setText("Incorrect credentials.");
                    //TODO: handle event when credentials are incorrect
                }
            }
        });
    }

    public void createAccount(final String email, final String firstName, final String lastName, final String phoneNumber, final String password) { //creates users account
        updateUI("Creating account...");
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    thisUser = new User(email,0,firstName,lastName,phoneNumber);
                    firebaseUser = mAuth.getCurrentUser();
                    updateUI("Account created successfully.");
                    database = FirebaseDatabase.getInstance().getReference("Users/" + firebaseUser.getUid());
                    database.setValue(thisUser);
                    startActivity(firebaseUser);
                }else{
                    //TODO: handle event of user already exists
                    updateUI("A user with " + email + " email already exists.");
                }
            }
        });
    }

    public void updateUI(String message){ //updates the ui with a given message, considering adding another value for if error occurs and only update certain elements
        fragmentLayout.setVisibility(View.GONE);
        userPrompt.setText(message);
        progressBar.setVisibility(View.VISIBLE);
        appName.setTextSize(50);
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
                    fragmentLayout.setVisibility(View.VISIBLE);
                    appName.setTextSize(36);
                }
                break;
        }
    }
}
