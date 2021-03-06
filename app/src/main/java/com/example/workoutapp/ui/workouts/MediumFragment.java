package com.example.workoutapp.ui.workouts;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.R;
import com.example.workoutapp.adapters.WorkoutRecyclerViewAdapter;
import com.example.workoutapp.viewmodels.HomeViewModel;

public class MediumFragment extends Fragment implements WorkoutRecyclerViewAdapter.WorkoutViewHolder.OnClickListener {

    private String[] workoutNames;
    private TypedArray workoutImages;
    private RecyclerView recyclerView;
    private NavController navController;
    private WorkoutRecyclerViewAdapter workoutRecyclerViewAdapter;
    private HomeViewModel homeViewModel;

    public MediumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medium, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewMedium);
        workoutNames = getResources().getStringArray(R.array.medium_workouts);
        workoutImages = getResources().obtainTypedArray(R.array.medium_workout_images);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        workoutRecyclerViewAdapter = new WorkoutRecyclerViewAdapter(getContext(), workoutNames, workoutImages,this);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        recyclerView.setAdapter(workoutRecyclerViewAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        return view;
    }

    @Override
    public void onClickListener(int position, String workoutName) {
        String[] description = getResources().getStringArray(R.array.medium_workouts_descriptions);
        homeViewModel.setText(description[position]);
        homeViewModel.setImage(ContextCompat.getDrawable(requireActivity(), workoutImages.getResourceId(position, -1)));
        Bundle bundle = new Bundle();
        bundle.putInt("difficulty", 5);
        navController.navigate(R.id.action_navigation_workouts_to_exerciseFragment, bundle);
    }
}