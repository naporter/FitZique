package com.example.workoutapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.workoutapp.ui.workouts.EasyFragment;
import com.example.workoutapp.ui.workouts.HardFragment;
import com.example.workoutapp.ui.workouts.MediumFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new EasyFragment();
            case 1:
                return new MediumFragment();
            case 2:
                return new HardFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
