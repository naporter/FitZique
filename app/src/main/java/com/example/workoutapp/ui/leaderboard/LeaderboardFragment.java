package com.example.workoutapp.ui.leaderboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.R;
import com.example.workoutapp.adapters.LeaderboardRecyclerViewAdapter;
import com.example.workoutapp.objects.Friend;
import com.example.workoutapp.viewmodels.UserViewModel;
import com.example.workoutapp.databinding.FragmentLeaderboardBinding;

import java.util.ArrayList;

public class LeaderboardFragment extends Fragment implements View.OnClickListener, LeaderboardRecyclerViewAdapter.FriendViewHolder.OnClickListener {

    private UserViewModel userViewModel;
    private RecyclerView leaderboardRecyclerView;
    private LeaderboardRecyclerViewAdapter leaderboardRecyclerViewAdapter;
    private Button pointsSelector;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        pointsSelector = view.findViewById(R.id.pointsSelector);
        leaderboardRecyclerView = view.findViewById(R.id.leaderboardRecyclerView);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        pointsSelector.setText("Lifetime");
        pointsSelector.setOnClickListener(this);
        //create a friend object out of the users info and add it to a new list to be displayed in the RecyclerView
        Friend user = new Friend();
        user.setDailyPoints(userViewModel.getUser().getValue().getDailyPoints().getValue());
        user.setWeeklyPoints(userViewModel.getUser().getValue().getWeeklyPoints().getValue());
        user.setLifetimePoints(userViewModel.getUser().getValue().getLifetimePoints().getValue());
        user.setFirstName("You");
        user.setLastName(null);
        ArrayList<Friend> userAndFriends = new ArrayList<>();
        userAndFriends.addAll(userViewModel.getUser().getValue().getFriends());
        userAndFriends.add(user);

        leaderboardRecyclerViewAdapter = new LeaderboardRecyclerViewAdapter(getContext(), pointsSelector, userAndFriends, userViewModel.getUser().getValue(), this);
        leaderboardRecyclerView.setAdapter(leaderboardRecyclerViewAdapter);
        leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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