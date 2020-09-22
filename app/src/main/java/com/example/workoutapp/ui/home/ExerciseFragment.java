package com.example.workoutapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.workoutapp.R;

import static com.example.workoutapp.ui.home.LegsFragment.button;

import org.w3c.dom.Text;


public class ExerciseFragment extends Fragment{

    public ExerciseFragment() {
        // Required empty public constructor
    }

    public static ExerciseFragment newInstance(String param1, String param2) {
        ExerciseFragment fragment = new ExerciseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_exercise, container, false); //pass the correct layout name for the fragment

        TextView text = (TextView) view.findViewById(R.id.descriptionText);
        switch (button){
            case "frontSquatsBtn":
                button = "frontSquatsBtn";
                text.setText("Set a barbell on a power rack at about shoulder height. Grab the " +
                        "power with an overhand grip at shoulder width and raise your elbows until " +
                        "your upper arms are parallel to the floor. Take the bar out of the rack " +
                        "and let it rest on your fingertips. Your elbows should be all the way up " +
                        "throughout the movement. Step back and set your feet at shoulder width " +
                        "with toes turned out slightly. Squat as low as you can without losing " +
                        "the arch in your lower back.");
                break;
            default:
                break;
        }
        return view;
    }

}