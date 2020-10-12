package com.example.workoutapp.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.workoutapp.R;

public class BackFragment extends Fragment implements View.OnClickListener{

    private NavController navController = null;
    private HomeViewModel homeViewModel;

    public BackFragment() {
    }

    public static BackFragment newInstance() {
        BackFragment fragment = new BackFragment();
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
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        ViewGroup group = view.findViewById(R.id.backLayout);
        View id;
        for (int i = 0; i < group.getChildCount(); i++){ //sets onClickListener for all buttons
            id = group.getChildAt(i);
            if(id instanceof Button){
                id.setOnClickListener(this);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_back, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tricepDipBtn:
                break;
            case R.id.tricepExtensionBtn:
                break;
            case R.id.skullCrusherBtn:
                break;
            case R.id.rowExtensionBtn:
                break;
            case R.id.forwardExtensionBtn:
                break;
            case R.id.dumbellKickbackBtn:
                break;
            case R.id.bandBentOverRowsBtn:
                break;
            case R.id.renegadeRowBtn:
                break;
            case R.id.dumbbellSingleArmBtn:
                break;
            default:
                break;
        }
        navController.navigate(R.id.action_backFragment_to_exerciseFragment);
    }
}