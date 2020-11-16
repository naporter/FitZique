package com.example.workoutapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.workoutapp.R;
import com.example.workoutapp.viewmodels.UserViewModel;
import com.example.workoutapp.objects.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private NavHostFragment navHostFragment;
    private TextView dailyPoints, weeklyPoints, lifetimePoints;
    private MenuItem userPointsItem;
    private MenuItem logoutBtn;
    private UserViewModel userViewModel;
    private User user;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment); //NavHostFragment must be defined when making calls to NavController while using FragmentContainerView
        navController = navHostFragment.getNavController();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class); //initializes user attributes in constructor
        userViewModel.init(getApplication());
        signOutListener();
        this.user = userViewModel.getUser().getValue();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        getSupportActionBar().setElevation(0); //removes drop shadows from the action bar and next line is navigation view
        navView.setElevation(0);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration); //setup back arrow actions in action bar
        NavigationUI.setupWithNavController(navView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) { //hides back button on Leaderboard, Badges, and Profile tabs
                if (destination.getId() == R.id.navigation_leaderboard || destination.getId() == R.id.navigation_notifications || destination.getId() == R.id.navigation_profile) {
                    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setHomeButtonEnabled(false);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) { //initialize all of the point values and their view
        getMenuInflater().inflate(R.menu.user_points, menu);
        userPointsItem = menu.findItem(R.id.pointsBtn);
        logoutBtn = menu.findItem(R.id.logoutBtn);
        logoutBtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) { //consider adding a prompt to logout after button pressed
                FirebaseAuth.getInstance().signOut();
                return true;
            }
        });
        dailyPoints = userPointsItem.getActionView().findViewById(R.id.dailyPoints);
        weeklyPoints = userPointsItem.getActionView().findViewById(R.id.weeklyPoints);
        lifetimePoints = userPointsItem.getActionView().findViewById(R.id.lifetimePoints);
        pointListeners();
        return true;
    }

    public void signOutListener(){
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    clearViewModel();
                    Intent intent = new Intent(getBaseContext(), LoginPageActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
    }

    public void clearViewModel(){
        this.getViewModelStore().clear();
    }

    public void pointListeners() { //automatically updates the text when the points change
        userViewModel.getUser().getValue().getDailyPoints().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                dailyPoints.setText(String.valueOf(integer));
            }
        });
        userViewModel.getUser().getValue().getWeeklyPoints().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                weeklyPoints.setText(String.valueOf(integer));
            }
        });
        userViewModel.getUser().getValue().getLifetimePoints().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                lifetimePoints.setText(String.valueOf(integer));
            }
        });
    }

    public UserViewModel getUserViewModel(){
        return userViewModel;
    }

    public void updateMeasurement(String measurement, int value){
        String userUID = FirebaseAuth.getInstance().getUid();
        userViewModel.getFirebaseAccessor().updateMeasurement(userUID, measurement, value);
    }

    public void updatePoints(int difficulty, int numReps){
        String userUID = FirebaseAuth.getInstance().getUid();
        int points = (int) (numReps * difficulty - (difficulty * numReps * (1 - (userViewModel.getUser().getValue().getBodyFatPercent()/100))));
        userViewModel.getFirebaseAccessor().updatePoints(userUID, points);
    }

    public void updateBodyFat(double bodyFat){
        String userUID = FirebaseAuth.getInstance().getUid();
        userViewModel.getFirebaseAccessor().updateBodyFat(userUID, bodyFat);
    }

    public void addFriend(String friendUID){
        String userUID = FirebaseAuth.getInstance().getUid();
        userViewModel.getFirebaseAccessor().addFriend(userUID, friendUID);
    }

    public void removeFriend(String friendUID) {
        String userUID = FirebaseAuth.getInstance().getUid();
        userViewModel.getFirebaseAccessor().removeFriend(userUID, friendUID);
    }

    @Override
    public boolean onSupportNavigateUp() { //overriding this method allows us to use a custom navigation controller
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {
        //getConstantState will return the source of the drawable image. comparing these to the current view that calls the on click will result in true if they are equal
        if (v.getBackground().getConstantState() == Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.lifetime_points)).getConstantState()) {
            v.setBackground(ContextCompat.getDrawable(this, R.drawable.weekly_points));
            lifetimePoints.setVisibility(View.GONE);
            weeklyPoints.setVisibility(View.VISIBLE);
        } else if (v.getBackground().getConstantState() == Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.weekly_points)).getConstantState()) {
            v.setBackground(ContextCompat.getDrawable(this, R.drawable.daily_points));
            weeklyPoints.setVisibility(View.GONE);
            dailyPoints.setVisibility(View.VISIBLE);
        } else {
            v.setBackground(ContextCompat.getDrawable(this, R.drawable.lifetime_points));
            dailyPoints.setVisibility(View.GONE);
            lifetimePoints.setVisibility(View.VISIBLE);
        }
    }
}