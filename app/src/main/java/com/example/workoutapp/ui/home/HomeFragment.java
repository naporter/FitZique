package com.example.workoutapp.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.workoutapp.R;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private HomeViewModel homeViewModel;
    NavController navController = null;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        view.findViewById(R.id.armsBtn).setOnClickListener(this);
        view.findViewById(R.id.legsBtn).setOnClickListener(this);
        view.findViewById(R.id.absBtn).setOnClickListener(this);
        view.findViewById(R.id.chestBtn).setOnClickListener(this);
        view.findViewById(R.id.backBtn).setOnClickListener(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        homeViewModel.setImage(ResourcesCompat.getDrawable(getResources(), R.drawable.no_image_found, getContext().getTheme()));
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.armsBtn:
                navController.navigate(R.id.action_homeFragment_to_armsFragment);
                break;
            case R.id.legsBtn:
                navController.navigate(R.id.action_homeFragment_to_legsFragment);
                break;
            case R.id.absBtn:
                navController.navigate(R.id.action_homeFragment_to_absFragment);
                break;
            case R.id.chestBtn:
                navController.navigate(R.id.action_homeFragment_to_chestFragment);
                break;
            case R.id.backBtn:
                navController.navigate(R.id.action_homeFragment_to_backFragment);
                break;
            default:
                break;
        }
    }
}