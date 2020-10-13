package com.example.workoutapp.ui.workouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.WorkoutViewHolder> {

    private String[] workoutName;
    private Context context;

    public RecyclerViewAdapter(Context context, String[] workoutName){
        this.context = context;
        this.workoutName = workoutName;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.workout_button, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        holder.workoutTitle.setText(workoutName[position]);
    }

    @Override
    public int getItemCount() {
        return workoutName.length;
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {

        private TextView workoutTitle;
        private ImageView workoutImage;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutTitle = itemView.findViewById(R.id.workoutName);
            workoutImage = itemView.findViewById(R.id.workoutImage);
        }
    }

}
