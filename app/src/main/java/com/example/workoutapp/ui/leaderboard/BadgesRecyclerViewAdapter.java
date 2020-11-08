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

public class BadgesRecyclerViewAdapter extends RecyclerView.Adapter<com.example.workoutapp.ui.leaderboard.BadgesRecyclerViewAdapter.BadgeViewHolder>{
    //TODO: class needs restructured for Badges recyclerview, elements were copied from FriendRecyclerViewAdapter

    private ArrayList<Friend> friends;
    private Context context;
    private com.example.workoutapp.ui.leaderboard.BadgesRecyclerViewAdapter.BadgeViewHolder.OnClickListener onClickListener;

    public BadgesRecyclerViewAdapter(Context context, ArrayList<Friend> friends, com.example.workoutapp.ui.leaderboard.BadgesRecyclerViewAdapter.BadgeViewHolder.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public com.example.workoutapp.ui.leaderboard.BadgesRecyclerViewAdapter.BadgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.friend_layout, parent, false);
        return new com.example.workoutapp.ui.leaderboard.BadgesRecyclerViewAdapter.BadgeViewHolder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.workoutapp.ui.leaderboard.BadgesRecyclerViewAdapter.BadgeViewHolder holder, int position) {
        holder.friendFirstName.setText(this.friends.get(position).getFirstName());
        holder.friendLastName.setText(this.friends.get(position).getLastName());
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }


    public static class BadgeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView friendFirstName;
        private TextView friendLastName;
        private com.example.workoutapp.ui.leaderboard.BadgesRecyclerViewAdapter.BadgeViewHolder.OnClickListener onClickListener;

        public BadgeViewHolder(@NonNull View itemView, com.example.workoutapp.ui.leaderboard.BadgesRecyclerViewAdapter.BadgeViewHolder.OnClickListener onClickListener) {
            super(itemView);
            friendFirstName = itemView.findViewById(R.id.friendFirstName);
            friendLastName = itemView.findViewById(R.id.friendLastName);
            this.onClickListener = onClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClickListener(getAdapterPosition(), this.friendFirstName.getText().toString(), this.friendLastName.getText().toString());
        }

        public interface OnClickListener{
            void onClickListener(int position, String friendFirstName, String friendLastName);
        }
    }
}
