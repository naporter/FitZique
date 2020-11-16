package com.example.workoutapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LeaderboardViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LeaderboardViewModel() {

        mText = new MutableLiveData<>();
        mText.setValue("This is the Leaderboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}