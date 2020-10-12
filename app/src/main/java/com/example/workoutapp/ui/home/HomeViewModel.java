package com.example.workoutapp.ui.home;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.workoutapp.R;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> text;
    private MutableLiveData<Drawable> image;

    public HomeViewModel() { //creates defaults in the event that an image and/or text is not provided
        text = new MutableLiveData<>();
        image = new MutableLiveData<>();
        text.setValue("No Description Found");
    }

    //setters for ExerciseFragments ImageView and TextView
    public void setText(String text){
        this.text.setValue(text);
    }

    public void setImage(Drawable image){
        this.image.setValue(image);
    }

    //getters for ExerciseFragments ImageView and TextView
    public LiveData<Drawable> getImage(){
        return image;
    }

    public LiveData<String> getText() {
        return text;
    }
}