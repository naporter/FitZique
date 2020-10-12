package com.example.workoutapp.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BadgesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BadgesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the Badges fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}