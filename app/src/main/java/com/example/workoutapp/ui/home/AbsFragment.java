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
import android.widget.Toolbar;

import com.example.workoutapp.R;

public class AbsFragment extends Fragment implements View.OnClickListener{

    private NavController navController = null;
    private HomeViewModel homeViewModel;

    public AbsFragment() {
    }

    public static AbsFragment newInstance() {
        AbsFragment fragment = new AbsFragment();
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
        ViewGroup group = view.findViewById(R.id.absLayout);
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
        return inflater.inflate(R.layout.fragment_abs, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cruntchesBtn:
                break;
            case R.id.sittingTwistsBtn:
                break;
            case R.id.raisedLegHoldBtn:
                break;
            case R.id.plankHoldBtn:
                break;
            case R.id.plankLegRaisesBtn:
                break;
            case R.id.kneeToElbowsBtn:
                break;
            case R.id.sideToSideChopsBtn:
                break;
            case R.id.crossChopsBtn:
                break;
            case R.id.vupsBtn:
                break;
            case R.id.wipersBtn:
                break;
            case R.id.lsitBtn:
                break;
            case R.id.scissorsBtn:
                break;
            case R.id.sitUpsBtn:
                break;
            case R.id.toeTapsBtn:
                break;
            default:
                break;
        }
        navController.navigate(R.id.action_absFragment_to_exerciseFragment);
    }
}