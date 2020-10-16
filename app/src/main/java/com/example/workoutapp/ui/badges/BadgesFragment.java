package com.example.workoutapp.ui.badges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.workoutapp.R;

public class BadgesFragment extends Fragment {

    private BadgesViewModel badgesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        badgesViewModel = new ViewModelProvider(requireActivity()).get(BadgesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_badges, container, false);
        final TextView textView = root.findViewById(R.id.text_badges);
        badgesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}