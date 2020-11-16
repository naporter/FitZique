package com.example.workoutapp.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.Friend;
import com.example.workoutapp.R;
import com.example.workoutapp.databinding.FriendLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class FriendRecyclerViewAdapter extends RecyclerView.Adapter<FriendRecyclerViewAdapter.FriendViewHolder>{

    private final List<Friend> friends;
    private final LifecycleOwner lifecycleOwner;
    private final FriendViewHolder.OnClickListener onClickListener;

    public FriendRecyclerViewAdapter(LifecycleOwner lifecycleOwner, ArrayList<Friend> friends, FriendViewHolder.OnClickListener onClickListener){
        this.lifecycleOwner = lifecycleOwner;
        this.friends = friends;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        FriendLayoutBinding friendLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.friend_layout, parent, false);
        return new FriendViewHolder(friendLayoutBinding, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        Friend friend = friends.get(position);
        holder.friendLayoutBinding.setFriend(friend);
        holder.friendLayoutBinding.setLifecycleOwner(lifecycleOwner);
        holder.friendLayoutBinding.executePendingBindings();
        holder.friendLayoutBinding.friendFirstName.setText(friends.get(position).getFirstName());
        holder.friendLayoutBinding.friendLastName.setText(friends.get(position).getLastName());
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }


    public static class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private OnClickListener onClickListener;
        private final FriendLayoutBinding friendLayoutBinding;

        public FriendViewHolder(@NonNull FriendLayoutBinding friendLayoutBinding, OnClickListener onClickListener) {
            super(friendLayoutBinding.getRoot());
            this.friendLayoutBinding = friendLayoutBinding;
            this.onClickListener = onClickListener;
        }

        @Override
        public void onClick(View v) {
//            onClickListener.onClickListener(getAdapterPosition(), this.friendFirstName.getText().toString(), this.friendLastName.getText().toString());
        }

        public interface OnClickListener{
//            void onClickListener(int position, String friendFirstName, String friendLastName);
        }
    }
}
