package com.example.workoutapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.workoutapp.database.FirebaseAccessor;
import com.example.workoutapp.objects.User;

public class UserViewModel extends AndroidViewModel {

    private MutableLiveData<User> user;
    private FirebaseAccessor firebaseAccessor;

    public UserViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(Application application){
        if(user != null){
            return;
        }
        firebaseAccessor = FirebaseAccessor.getInstance(application);
        user = firebaseAccessor.getUser();
    }
    public LiveData<User> getUser(){
        return this.user;
    }

    public FirebaseAccessor getFirebaseAccessor(){
        return firebaseAccessor;
    }

}
