package com.example.workoutapp.ui.leaderboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.R;
import com.example.workoutapp.UserViewModel;
import com.example.workoutapp.ui.profile.FriendRecyclerViewAdapter;

public class LeaderboardFragment extends Fragment implements View.OnClickListener, LeaderboardRecyclerViewAdapter.FriendViewHolder.OnClickListener {

    private UserViewModel userViewModel;
    private RecyclerView friendRecyclerView;
    private LeaderboardRecyclerViewAdapter leaderboardRecyclerViewAdapter;
    private Button pointsSelector;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        friendRecyclerView = view.findViewById(R.id.leaderboardRecyclerView);
        pointsSelector = view.findViewById(R.id.pointsSelector);
        pointsSelector.setText("Lifetime");
        pointsSelector.setOnClickListener(this);
        leaderboardRecyclerViewAdapter = new LeaderboardRecyclerViewAdapter(getContext(), pointsSelector, userViewModel.getFriends().getValue(), this);
        friendRecyclerView.setAdapter(leaderboardRecyclerViewAdapter);
        friendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onClickListener(int position, String friendFirstName, String friendLastName, String friendPoints) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pointsSelector:
                if (pointsSelector.getText().equals("Lifetime")){
                    pointsSelector.setText("Weekly");
                    leaderboardRecyclerViewAdapter.notifyDataSetChanged();
                }
                else if (pointsSelector.getText().equals("Weekly")){
                    pointsSelector.setText("Daily");
                    leaderboardRecyclerViewAdapter.notifyDataSetChanged();
                }
                else if (pointsSelector.getText().equals("Daily")){
                    pointsSelector.setText("Lifetime");
                    leaderboardRecyclerViewAdapter.notifyDataSetChanged();
                }
        }
    }
}