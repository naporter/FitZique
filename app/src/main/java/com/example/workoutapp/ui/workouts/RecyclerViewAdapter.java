package com.example.workoutapp.ui.workouts;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.WorkoutViewHolder>{

    private String[] workoutName;
    private TypedArray workoutImages;
    private Context context;
    private WorkoutViewHolder.OnClickListener onClickListener;

    public RecyclerViewAdapter(Context context, String[] workoutName, TypedArray workoutImages, WorkoutViewHolder.OnClickListener onClickListener){
        this.context = context;
        this.workoutName = workoutName;
        this.onClickListener = onClickListener;
        this.workoutImages = workoutImages;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.workout_button, parent, false);
        return new WorkoutViewHolder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        holder.workoutName.setText(workoutName[position]);
        holder.workoutImage.setImageResource(workoutImages.getResourceId(position, -1));
    }

    @Override
    public int getItemCount() {
        return workoutName.length;
    }


    public static class WorkoutViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView workoutName;
        private ImageView workoutImage;
        private OnClickListener onClickListener;

        public WorkoutViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);
            workoutName = itemView.findViewById(R.id.workoutName);
            workoutImage = itemView.findViewById(R.id.workoutImage);
            this.onClickListener = onClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClickListener(getAdapterPosition(), this.workoutName.getText().toString());
        }

        public interface OnClickListener{
            void onClickListener(int position, String workoutName); //passing workout name in case we need it for something. Might remove if we determine we do not.
        }
    }
}
