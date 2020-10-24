package com.example.workoutapp.ui.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.workoutapp.MainActivity;
import com.example.workoutapp.R;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    private EditText height, hipSize, neckSize, waistSize, weight;
    private TextInputLayout hipsContainer;
    private TextView bodyFatPercent;
    private Button recalcBtn;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        height = view.findViewById(R.id.height);
        hipSize = view.findViewById(R.id.hipSize);
        neckSize = view.findViewById(R.id.neckSize);
        waistSize = view.findViewById(R.id.waistSize);
        weight = view.findViewById(R.id.weight);
        hipsContainer = view.findViewById(R.id.hipsContainer);
        bodyFatPercent = view.findViewById(R.id.bodyFatPercent);
        recalcBtn = view.findViewById(R.id.recalcBtn);
        bodyFatPercent.setText(String.valueOf(MainActivity.thisUser.getBodyFatPercent()));
        recalcBtn.setOnClickListener(this);

        height.setText(String.valueOf(MainActivity.thisUser.getHeight()));
        hipSize.setText(String.valueOf(MainActivity.thisUser.getHipSize()));
        neckSize.setText(String.valueOf(MainActivity.thisUser.getNeckSize()));
        waistSize.setText(String.valueOf(MainActivity.thisUser.getWaistSize()));
        weight.setText(String.valueOf(MainActivity.thisUser.getWeight()));
        if(MainActivity.thisUser.getGender().equals("Female")){
            hipsContainer.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onClick(View v) { // TODO: shouldn't update them all, just update the ones that changed.
        MainActivity.updateMeasurement("height", Integer.parseInt(height.getText().toString()));
        MainActivity.updateMeasurement("hipSize", Integer.parseInt(hipSize.getText().toString()));
        MainActivity.updateMeasurement("neckSize", Integer.parseInt(neckSize.getText().toString()));
        MainActivity.updateMeasurement("waistSize", Integer.parseInt(waistSize.getText().toString()));
        MainActivity.updateMeasurement("weight", Integer.parseInt(weight.getText().toString()));
        MainActivity.initBodyFat();
        bodyFatPercent.setText(String.valueOf(MainActivity.thisUser.getBodyFatPercent()));
    }
}