package com.example.workoutapp.ui.workouts;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.workoutapp.MainActivity;
import com.example.workoutapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class ExerciseFragment extends Fragment implements View.OnClickListener{

    private ImageView imageView;
    private TextView descriptionText;
    private HomeViewModel homeViewModel;
    private Button addPointsBtn;

    public ExerciseFragment() {
        // Required empty public constructor
    }

    public static ExerciseFragment newInstance() {
        ExerciseFragment fragment = new ExerciseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                descriptionText.setText(s);
            }
        });
        homeViewModel.getImage().observe(getViewLifecycleOwner(), new Observer<Drawable>() {
            @Override
            public void onChanged(Drawable drawable) {
                imageView.setBackground(drawable);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        imageView = view.findViewById(R.id.imageView);
        descriptionText = view.findViewById(R.id.descriptionText);
        addPointsBtn = view.findViewById(R.id.addPointsBtn);
        addPointsBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        MainActivity.updatePoints(1);
    }

}