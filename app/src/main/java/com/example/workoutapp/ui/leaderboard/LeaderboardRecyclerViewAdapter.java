package com.example.workoutapp.ui.leaderboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.Friend;
import com.example.workoutapp.R;

import java.util.ArrayList;

public class LeaderboardRecyclerViewAdapter extends RecyclerView.Adapter<LeaderboardRecyclerViewAdapter.FriendViewHolder>{

    private ArrayList<Friend> friends;
    private Context context;
    private FriendViewHolder.OnClickListener onClickListener;
    private TextView pointsSelector;

    public LeaderboardRecyclerViewAdapter(Context context, TextView pointsSelector, ArrayList<Friend> friends, FriendViewHolder.OnClickListener onClickListener){
        this.context = context;
        this.pointsSelector = pointsSelector;
        this.friends = friends;
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
        holder.friendFirstName.setText(this.friends.get(position).getFirstName());
        holder.friendLastName.setText(this.friends.get(position).getLastName());
        if (pointsSelector.getText().equals("Lifetime")){
            holder.friendPoints.setText(String.valueOf(this.friends.get(position).getLifetimePoints()));
        }
        else if (pointsSelector.getText().equals("Weekly")){
            holder.friendPoints.setText(String.valueOf(this.friends.get(position).getWeeklyPoints()));
        }
        else if (pointsSelector.getText().equals("Daily")){
            holder.friendPoints.setText(String.valueOf(this.friends.get(position).getDailyPoints()));
        }

    }

    @Override
    public int getItemCount() {
        return friends.size();
    }


    public static class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView friendFirstName;
        private TextView friendLastName;
        private TextView friendPoints;
        private OnClickListener onClickListener;

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
