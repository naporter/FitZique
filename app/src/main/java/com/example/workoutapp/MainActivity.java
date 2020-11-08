package com.example.workoutapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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
    private FirebaseAuth mAuth;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class); //initializes user attributes in constructor
        BottomNavigationView navView = findViewById(R.id.nav_view);
        getSupportActionBar().setElevation(0); //removes drop shadows from the action bar and next line is navigation view
        navView.setElevation(0);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment); //NavHostFragment must be defined when making calls to NavController while using FragmentContainerView
        navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration); //setup back arrow actions in action bar
        NavigationUI.setupWithNavController(navView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) { //hides back button on Leaderboard, Badges, and Profile tabs
                if(destination.getId() == R.id.navigation_leaderboard || destination.getId() == R.id.navigation_notifications || destination.getId() == R.id.navigation_profile){
                    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setHomeButtonEnabled(false);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //initialize all of the point values and their views
        getMenuInflater().inflate(R.menu.user_points, menu);
        userPointsItem = menu.findItem(R.id.pointsBtn);
        logoutBtn = menu.findItem(R.id.logoutBtn);
        logoutBtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) { //consider adding a prompt to logout after button pressed
                mAuth.signOut();
                Intent intent = new Intent(getBaseContext(), LoginPageActivity.class);
                startActivity(intent);
                finish();
                return false;
            }
        });
        dailyPoints = userPointsItem.getActionView().findViewById(R.id.dailyPoints);
        weeklyPoints = userPointsItem.getActionView().findViewById(R.id.weeklyPoints);
        lifetimePoints = userPointsItem.getActionView().findViewById(R.id.lifetimePoints);
        pointListeners();
        return true;
    }

    public void pointListeners(){ //automatically updates the text when the points change
        userViewModel.getDailyPoints().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                dailyPoints.setText(String.valueOf(integer));
            }
        });
        userViewModel.getWeeklyPoints().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                weeklyPoints.setText(String.valueOf(integer));
            }
        });
        userViewModel.getLifetimePoints().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                lifetimePoints.setText(String.valueOf(integer));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() { //overriding this method allows us to use a custom navigation controller
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == (R.id.logoutBtn)){
            mAuth.signOut();
            finish();
        }
        //getConstantState will return the source of the drawable image. comparing these to the current view that calls the on click will result in true if they are equal
        if(v.getBackground().getConstantState() == Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.lifetime_points)).getConstantState()){
            v.setBackground(ContextCompat.getDrawable(this, R.drawable.weekly_points));
            lifetimePoints.setVisibility(View.GONE);
            weeklyPoints.setVisibility(View.VISIBLE);
        } else if(v.getBackground().getConstantState() == Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.weekly_points)).getConstantState()){
            v.setBackground(ContextCompat.getDrawable(this, R.drawable.daily_points));
            weeklyPoints.setVisibility(View.GONE);
            dailyPoints.setVisibility(View.VISIBLE);
        }else {
            v.setBackground(ContextCompat.getDrawable(this, R.drawable.lifetime_points));
            dailyPoints.setVisibility(View.GONE);
            lifetimePoints.setVisibility(View.VISIBLE);
        }
    }
}