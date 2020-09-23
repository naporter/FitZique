package com.example.workoutapp.ui.home;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workoutapp.R;
import com.example.workoutapp.ui.notifications.NotificationsViewModel;

public class ChestFragment extends Fragment implements View.OnClickListener{

    NavController navController = null;
    private HomeViewModel homeViewModel;

    public ChestFragment() {
        // Required empty public constructor
    }
    public static ChestFragment newInstance(String param1, String param2) {
        ChestFragment fragment = new ChestFragment();
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
        view.findViewById(R.id.barbellBenchPressBtn).setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chest, container, false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.barbellBenchPressBtn:
                homeViewModel.setText("This is the description text.");
                homeViewModel.setImage(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, getContext().getTheme()));
                break;
            default:
                break;
        }
        navController.navigate(R.id.action_chestFragment_to_exerciseFragment);
    }

}