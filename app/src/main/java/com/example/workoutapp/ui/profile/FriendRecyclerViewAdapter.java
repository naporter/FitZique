package com.example.workoutapp.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.Friend;
import com.example.workoutapp.R;

import java.util.ArrayList;

public class FriendRecyclerViewAdapter extends RecyclerView.Adapter<FriendRecyclerViewAdapter.FriendViewHolder>{

    private ArrayList<Friend> friends;
    private Context context;
    private FriendViewHolder.OnClickListener onClickListener;

    public FriendRecyclerViewAdapter(Context context, ArrayList<Friend> friends, FriendViewHolder.OnClickListener onClickListener){
        this.context = context;
        this.friends = friends;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.friend_layout, parent, false);
        return new FriendViewHolder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        holder.friendFirstName.setText(this.friends.get(position).getFirstName());
        holder.friendLastName.setText(this.friends.get(position).getLastName());
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }


    public static class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView friendFirstName;
        private TextView friendLastName;
        private OnClickListener onClickListener;

        public FriendViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
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
