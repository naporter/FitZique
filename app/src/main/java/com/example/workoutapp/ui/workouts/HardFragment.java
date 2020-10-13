package com.example.workoutapp.ui.workouts;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workoutapp.R;

public class HardFragment extends Fragment {

    private String[] workoutNames;
    private RecyclerView recyclerView;

    public HardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hard, container, false);
        recyclerView = view.findViewById(R.id.recylerViewHard);
        workoutNames = getResources().getStringArray(R.array.hard_workouts);

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getContext(), workoutNames);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
}