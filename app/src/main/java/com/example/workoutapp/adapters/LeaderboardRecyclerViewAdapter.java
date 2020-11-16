package com.example.workoutapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.objects.Friend;
import com.example.workoutapp.R;
import com.example.workoutapp.objects.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LeaderboardRecyclerViewAdapter extends RecyclerView.Adapter<LeaderboardRecyclerViewAdapter.FriendViewHolder>{

    private final ArrayList<Friend> friends;
    private final User user;
    private final Context context;
    private final FriendViewHolder.OnClickListener onClickListener;
    private final Button pointsSelector;

    public LeaderboardRecyclerViewAdapter(Context context, Button pointsSelector, ArrayList<Friend> friends, User user, FriendViewHolder.OnClickListener onClickListener){
        this.context = context;
        this.pointsSelector = pointsSelector;
        this.friends = friends;
        this.user = user;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.friend_layout_leaderboard, parent, false);
        return new FriendViewHolder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        if (pointsSelector.getText().equals("Lifetime")){
            Collections.sort(friends, new Comparator<Friend>() { //sort the list based on lifetime points
                @Override
                public int compare(Friend friendA, Friend friendB) {
                    return Integer.compare(friendB.getLifetimePoints(), friendA.getLifetimePoints()); //swapping order from compare gives descending order
                }
            });
            holder.friendPoints.setText(String.valueOf(this.friends.get(position).getLifetimePoints()));
        }
        else if (pointsSelector.getText().equals("Weekly")){
            Collections.sort(friends, new Comparator<Friend>() { //sort the list based on weekly points
                @Override
                public int compare(Friend friendA, Friend friendB) {
                    return Integer.compare(friendB.getWeeklyPoints(), friendA.getWeeklyPoints()); //swapping order from compare gives descending order
                }
            });
            holder.friendPoints.setText(String.valueOf(this.friends.get(position).getWeeklyPoints()));
        }
        else if (pointsSelector.getText().equals("Daily")){
            Collections.sort(friends, new Comparator<Friend>() { //sort the list based on daily points
                @Override
                public int compare(Friend friendA, Friend friendB) {
                    return Integer.compare(friendB.getDailyPoints(), friendA.getDailyPoints()); //swapping order from compare gives descending order
                }
            });
            holder.friendPoints.setText(String.valueOf(this.friends.get(position).getDailyPoints()));
        }
        holder.friendFirstName.setText(this.friends.get(position).getFirstName());
        holder.friendLastName.setText(this.friends.get(position).getLastName());


    }

    @Override
    public int getItemCount() {
        return friends.size();
    }


    public static class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView friendFirstName;
        private final TextView friendLastName;
        private final TextView friendPoints;
        private final OnClickListener onClickListener;

        public FriendViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);
            friendFirstName = itemView.findViewById(R.id.friendFirstName);
            friendLastName = itemView.findViewById(R.id.friendLastName);
            friendPoints = itemView.findViewById(R.id.friendPoints);
            this.onClickListener = onClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClickListener(getAdapterPosition(), this.friendFirstName.getText().toString(), this.friendLastName.getText().toString(), this.friendPoints.getText().toString());
        }

        public interface OnClickListener{
            void onClickListener(int position, String friendFirstName, String friendLastName, String friendPoints);
        }
    }
}
