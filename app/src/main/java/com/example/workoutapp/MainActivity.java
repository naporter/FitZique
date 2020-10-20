package com.example.workoutapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private static DatabaseReference database;
    private static User thisUser;
    private TextView dailyPoints, weeklyPoints, lifetimePoints;
    private MenuItem userPointsItem;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisUser = new User();
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        database = FirebaseDatabase.getInstance().getReference("Users/" + firebaseUser.getUid());
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) { //hides back button on Leaderboard and Badges
                if(destination.getId() == R.id.navigation_leaderboard || destination.getId() == R.id.navigation_notifications || destination.getId() == R.id.navigation_profile){
                    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setHomeButtonEnabled(false);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_points, menu);
        userPointsItem = menu.findItem(R.id.pointsBtn);
        dailyPoints = userPointsItem.getActionView().findViewById(R.id.dailyPoints);
        weeklyPoints = userPointsItem.getActionView().findViewById(R.id.weeklyPoints);
        lifetimePoints = userPointsItem.getActionView().findViewById(R.id.lifetimePoints);
        pointListener();
        dailyPointListener();
        lifetimePointListener();
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    private void pointListener(){
        database = FirebaseDatabase.getInstance().getReference("Users/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid() + "/points/weeklyPoints");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //this method will run initially and again anytime data changes
                thisUser.setWeeklyPoints(snapshot.getValue(int.class));
                weeklyPoints.setText(String.valueOf(thisUser.getWeeklyPoints()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.nav_host_fragment), R.string.database_error, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void dailyPointListener(){
        database = FirebaseDatabase.getInstance().getReference("Users/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid() + "/points/dailyPoints");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //this method will run initially and again anytime data changes
                thisUser.setDailyPoints(snapshot.getValue(int.class));
                dailyPoints.setText(String.valueOf(thisUser.getDailyPoints()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.nav_host_fragment), R.string.database_error, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void lifetimePointListener(){
        database = FirebaseDatabase.getInstance().getReference("Users/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid() + "/points/lifetimePoints");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //this method will run initially and again anytime data changes
                thisUser.setLifetimePoints(snapshot.getValue(int.class));
                lifetimePoints.setText(String.valueOf(thisUser.getLifetimePoints()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.nav_host_fragment), R.string.database_error, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    public static void updatePoints(int points){
        database = FirebaseDatabase.getInstance().getReference("Users/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid() + "/points/weeklyPoints");
        database.setValue(thisUser.getWeeklyPoints() + points);
        database = FirebaseDatabase.getInstance().getReference("Users/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid() + "/points/dailyPoints");
        database.setValue(thisUser.getDailyPoints() + points);
        database = FirebaseDatabase.getInstance().getReference("Users/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid() + "/points/lifetimePoints");
        database.setValue(thisUser.getLifetimePoints() + points);
    }

    @Override
    public void onClick(View v) {
        if(v.getBackground().getConstantState() == Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.lifetime_points)).getConstantState()){
            v.setBackground(ContextCompat.getDrawable(this, R.drawable.weekly_points));
            weeklyPoints.setText(String.valueOf(thisUser.getWeeklyPoints()));
            lifetimePoints.setVisibility(View.GONE);
            weeklyPoints.setVisibility(View.VISIBLE);
        } else if(v.getBackground().getConstantState() == Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.weekly_points)).getConstantState()){
            v.setBackground(ContextCompat.getDrawable(this, R.drawable.daily_points));
            dailyPoints.setText(String.valueOf(thisUser.getDailyPoints()));
            weeklyPoints.setVisibility(View.GONE);
            dailyPoints.setVisibility(View.VISIBLE);
        }else {
            v.setBackground(ContextCompat.getDrawable(this, R.drawable.lifetime_points));
            lifetimePoints.setText(String.valueOf(thisUser.getLifetimePoints()));
            dailyPoints.setVisibility(View.GONE);
            lifetimePoints.setVisibility(View.VISIBLE);
        }
    }
}