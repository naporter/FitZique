package com.example.workoutapp.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workoutapp.R;


public class LegsFragment extends Fragment implements View.OnClickListener{

    NavController navController = null;

    public LegsFragment() {
    }

    public static ArmsFragment newInstance(String param1, String param2) {
        ArmsFragment fragment = new ArmsFragment();
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
        navController = Navigation.findNavController(view);
        view.findViewById(R.id.frontSquatsBtn).setOnClickListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_legs, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.frontSquatsBtn:
                navController.navigate(R.id.action_legsFragment_to_exerciseFragment);
                break;
            default:
                break;
        }
    }
}