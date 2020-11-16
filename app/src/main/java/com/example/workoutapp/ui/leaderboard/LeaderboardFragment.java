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
import com.example.workoutapp.UserViewModel;
import com.example.workoutapp.databinding.FragmentLeaderboardBinding;

public class LeaderboardFragment extends Fragment implements View.OnClickListener, LeaderboardRecyclerViewAdapter.FriendViewHolder.OnClickListener {

    private UserViewModel userViewModel;
    private RecyclerView leaderboardRecyclerView;
    private LeaderboardRecyclerViewAdapter leaderboardRecyclerViewAdapter;
    private Button pointsSelector;
    private FragmentLeaderboardBinding fragmentLeaderboardBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        pointsSelector = view.findViewById(R.id.pointsSelector);
        pointsSelector.setText("Lifetime");
        pointsSelector.setOnClickListener(this);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        leaderboardRecyclerViewAdapter = new LeaderboardRecyclerViewAdapter(getContext(), pointsSelector, userViewModel.getUser().getValue().getFriends(), userViewModel.getUser().getValue(), this);
        leaderboardRecyclerView = view.findViewById(R.id.leaderboardRecyclerView);
        leaderboardRecyclerView.setAdapter(leaderboardRecyclerViewAdapter);
        leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        fragmentLeaderboardBinding.leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        fragmentLeaderboardBinding.leaderboardRecyclerView.setAdapter(leaderboardRecyclerViewAdapter);
//        fragmentLeaderboardBinding.setLifecycleOwner(requireActivity());
//        fragmentLeaderboardBinding.executePendingBindings();
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