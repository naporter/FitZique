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

public class ChestFragment extends Fragment implements View.OnClickListener{

    private NavController navController = null;
    private HomeViewModel homeViewModel;

    public ChestFragment() {
    }

    public static ChestFragment newInstance() {
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
        homeViewModel.setImage(ResourcesCompat.getDrawable(getResources(), R.drawable.no_image_found, getContext().getTheme()));
        homeViewModel.setText("No description found.");
        ViewGroup group = view.findViewById(R.id.chestLayout);
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

        return inflater.inflate(R.layout.fragment_chest, container, false);
    }


    @Override
    public void onClick(View v) { //onClick for all buttons in the fragment
        switch (v.getId()){
            case R.id.barbellBenchPressBtn:
                homeViewModel.setText("This is the description text for Barbell Bench Press.");
//                homeViewModel.setImage(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, getContext().getTheme()));
                break;
            case R.id.inclineBenchPressBtn:
                homeViewModel.setText("This is the description text for Incline Bench Press");
//                homeViewModel.setImage(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, getContext().getTheme()));
                break;
            case R.id.dumbbellBenchPressBtn:
                homeViewModel.setText("This is the description text for Dumbbell Bench Press");
//                homeViewModel.setImage(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, getContext().getTheme()));
                break;
            case R.id.peckdeckFlyBtn:
                homeViewModel.setText("This is the description text for Peckdeck Fly");
//                homeViewModel.setImage(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, getContext().getTheme()));
                break;
            case R.id.dumbbellInclineFlyBtn:
                homeViewModel.setText("This is the description text for Dumbbell Incline Fly");
//                homeViewModel.setImage(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, getContext().getTheme()));
                break;
            case R.id.dumbbellSqueezePressBtn:
                homeViewModel.setText("This is the description text for Dumbbell Squeeze Press");
//                homeViewModel.setImage(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, getContext().getTheme()));
                break;
            case R.id.inclineBarbellBenchPressBtn:
                homeViewModel.setText("This is the description text for Incline Barbell Bench Press");
//                homeViewModel.setImage(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, getContext().getTheme()));
                break;
            case R.id.closegripBarbellBenchPressBtn:
                homeViewModel.setText("This is the description text for Close-Grip Barbell Bench Press");
//                homeViewModel.setImage(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, getContext().getTheme()));
                break;
            case R.id.declinePressupBtn:
                homeViewModel.setText("This is the description text for Decline Press-up");
//                homeViewModel.setImage(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, getContext().getTheme()));
                break;
            case R.id.cableFlyBtn:
                homeViewModel.setText("This is the description text for Cable Fly");
//                homeViewModel.setImage(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, getContext().getTheme()));
                break;
            case R.id.declineBarbellBenchPressBtn:
                homeViewModel.setText("This is the description text for Decline Barbell Bench Press");
//                homeViewModel.setImage(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, getContext().getTheme()));
                break;
            case R.id.staggeredPressupBtn:
                homeViewModel.setText("This is the description text for Staggered Press-up");
//                homeViewModel.setImage(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, getContext().getTheme()));
                break;
            case R.id.chestDipsBtn:
                homeViewModel.setText("This is the description text for Chest Dips");
//                homeViewModel.setImage(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, getContext().getTheme()));
                break;
            case R.id.clapPressupBtn:
                homeViewModel.setText("This is the description text for Clap Press-up");
//                homeViewModel.setImage(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, getContext().getTheme()));
                break;
            default:
                break;
        }
        navController.navigate(R.id.action_chestFragment_to_exerciseFragment);
    }

}