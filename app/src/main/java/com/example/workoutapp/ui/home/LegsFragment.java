package com.example.workoutapp.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workoutapp.R;


public class LegsFragment extends Fragment {

    public LegsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LegsFragment newInstance(String param1, String param2) {
        LegsFragment fragment = new LegsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_legs, container, false);
    }
}