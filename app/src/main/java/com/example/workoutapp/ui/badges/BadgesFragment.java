package com.example.workoutapp.ui.badges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.R;
import com.example.workoutapp.UserViewModel;
import com.example.workoutapp.ui.profile.FriendRecyclerViewAdapter;

public class BadgesFragment extends Fragment implements BadgesRecyclerViewAdapter.BadgeViewHolder.OnClickListener {

    private RecyclerView badgesRecyclerView;
    private BadgesRecyclerViewAdapter badgesRecyclerViewAdapter;
    private UserViewModel userViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_badges, container, false);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        badgesRecyclerView = view.findViewById(R.id.badgesRecyclerView);
        badgesRecyclerViewAdapter = new BadgesRecyclerViewAdapter(getContext(), userViewModel.getFriends().getValue(), this); // change values to match what to pass for badges
        badgesRecyclerView.setAdapter(badgesRecyclerViewAdapter);
        badgesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); //change span count when we know how many badges we want per row
        return view;
    }

    @Override
    public void onClickListener(int position, String friendFirstName, String friendLastName) {

    }
}