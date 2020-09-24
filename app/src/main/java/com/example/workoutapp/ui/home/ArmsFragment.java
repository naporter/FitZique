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

public class ArmsFragment extends Fragment implements View.OnClickListener{

    private NavController navController = null;
    private HomeViewModel homeViewModel;

    public ArmsFragment() {
    }

    public static ArmsFragment newInstance() {
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
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        homeViewModel.setImage(ResourcesCompat.getDrawable(getResources(), R.drawable.no_image_found, getContext().getTheme()));
        homeViewModel.setText("No description found.");
        ViewGroup group = view.findViewById(R.id.armsLayout);
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
        return inflater.inflate(R.layout.fragment_arms, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.curlsBtn:
                break;
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
            case R.id.overUnderPullupsBtn:
                break;
            case R.id.diamondPushupsBtn:
                break;
            default:
                break;
        }
        navController.navigate(R.id.action_armsFragment_to_exerciseFragment);
    }
}