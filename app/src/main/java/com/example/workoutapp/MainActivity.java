package com.example.workoutapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
    private NavHostFragment navHostFragment;
    private static DatabaseReference database;
    public static User thisUser;
    private TextView dailyPoints, weeklyPoints, lifetimePoints;
    private MenuItem userPointsItem;
    private MenuItem logoutBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisUser = new User();
        mAuth = FirebaseAuth.getInstance();
        initGender();
        measurementListener();
        initBirthday();
        getSupportActionBar().setElevation(0);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setElevation(0);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment); //NavHostFragment must be defined when making calls to NavController while using FragmentContainerView
        navController = navHostFragment.getNavController();
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
        logoutBtn = menu.findItem(R.id.logoutBtn);
        logoutBtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
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
        pointListener();
        dailyPointListener();
        lifetimePointListener();
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    private void initBirthday(){
        database = FirebaseDatabase.getInstance().getReference("Users/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid() + "/demographics/birthday");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //this method will run initially and again anytime data changes
                thisUser.setBirthday(snapshot.getValue(String.class));
                System.out.println(java.time.LocalDate.now());
                String[] currentDate = java.time.LocalDate.now().toString().split("-");
                String currentDateMod = currentDate[0]+currentDate[1]+currentDate[2];
                String[] userBirthDay = thisUser.getBirthday().split("/");
                String userBirthdayMod = userBirthDay[2] + userBirthDay[0] + userBirthDay[1];
                thisUser.setAge((int)((Integer.parseInt(currentDateMod) - Integer.parseInt(userBirthdayMod))/10000));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.nav_host_fragment), R.string.database_error, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void initGender(){
        database = FirebaseDatabase.getInstance().getReference("Users/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid() + "/demographics/gender");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                thisUser.setGender(snapshot.getValue(String.class));
                initBodyFat();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void pointListener(){ //we could potentially combine all of the point listeners
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

    private void measurementListener(){
        database = FirebaseDatabase.getInstance().getReference("Users/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid() + "/measurements");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //this method will run initially and again anytime data changes
                thisUser.setHeight(snapshot.child("height").getValue(int.class));
                thisUser.setHipSize(snapshot.child("hipSize").getValue(int.class));
                thisUser.setNeckSize(snapshot.child("neckSize").getValue(int.class));
                thisUser.setWaistSize(snapshot.child("waistSize").getValue(int.class));
                thisUser.setWeight(snapshot.child("weight").getValue(int.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.nav_host_fragment), R.string.database_error, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    public static void initBodyFat(){
        if(thisUser.getGender().equals("Female")){
            thisUser.setBodyFatPercent(163.205 * Math.log10(thisUser.getWaistSize() + thisUser.getHipSize() - thisUser.getNeckSize()) -
                    97.684 * Math.log10(thisUser.getHeight()) + 36.76);
        }else{
            double bodyFat = 86.010 * Math.log10(thisUser.getWaistSize() - thisUser.getNeckSize()) -
                    70.041 * Math.log10(thisUser.getHeight()) + 36.76;
            thisUser.setBodyFatPercent(bodyFat);
        }
    }

    public static int updatePoints(int difficulty, int numReps){
        int points = (int) Math.round(numReps * difficulty - (difficulty * numReps * (1 - (thisUser.getBodyFatPercent() / 100))));
        System.out.println(points);
        database = FirebaseDatabase.getInstance().getReference("Users/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid() + "/points/weeklyPoints");
        database.setValue(thisUser.getWeeklyPoints() + points);
        database = FirebaseDatabase.getInstance().getReference("Users/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid() + "/points/dailyPoints");
        database.setValue(thisUser.getDailyPoints() + points);
        database = FirebaseDatabase.getInstance().getReference("Users/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid() + "/points/lifetimePoints");
        database.setValue(thisUser.getLifetimePoints() + points);
        return points;
    }

    public static void updateMeasurement(String source, int measurement){
        switch (source){
            case "height":
                thisUser.setHeight(measurement);
                break;
            case "hipSize":
                thisUser.setHipSize(measurement);
                break;
            case "neckSize":
                thisUser.setNeckSize(measurement);
                break;
            case "waistSize":
                thisUser.setWaistSize(measurement);
                break;
            case "weight":
                thisUser.setWeight(measurement);
                break;
        }
        database = FirebaseDatabase.getInstance().getReference("Users/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid() + "/measurements/" + source);
        database.setValue(measurement);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == (R.id.logoutBtn)){
            mAuth.signOut();
        }
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