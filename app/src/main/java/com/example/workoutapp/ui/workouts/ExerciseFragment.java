package com.example.workoutapp.ui.workouts;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.workoutapp.activities.MainActivity;
import com.example.workoutapp.R;
import com.example.workoutapp.viewmodels.HomeViewModel;
import com.example.workoutapp.viewmodels.UserViewModel;


public class ExerciseFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private ImageView imageView;
    private TextView descriptionText;
    private EditText numReps;
    private HomeViewModel homeViewModel;
    private UserViewModel userViewModel;
    private Button addPointsBtn;
    private int difficulty;

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
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        this.difficulty = getArguments().getInt("difficulty");
        numReps = view.findViewById(R.id.numReps);
        numReps.addTextChangedListener(this);
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
        addPointsBtn.setClickable(false);
        return view;
    }

    @Override
    public void onClick(View v) {
        int numRepsValue = Integer.parseInt(numReps.getText().toString());
        ((MainActivity)getActivity()).updatePoints(difficulty, numRepsValue);
        numReps.getText().clear();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(addPointsBtn.getWindowToken(), 0);
        addPointsBtn.setClickable(false);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(numReps.getText().toString())){
            addPointsBtn.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.ripple_effect));
            addPointsBtn.setClickable(true);
        }else{
            addPointsBtn.setBackgroundColor(getResources().getColor(R.color.cardview_shadow_start_color));
            addPointsBtn.setClickable(false);
        }
    }
}