package com.example.workoutapp.ui.profile;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.workoutapp.MainActivity;
import com.example.workoutapp.R;
import com.example.workoutapp.UserViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener{

    private EditText height, hipSize, neckSize, waistSize, weight;
    private TextInputLayout hipsContainer;
    private TextView bodyFatPercent;
    private Button recalcBtn;
    private UserViewModel userViewModel;

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
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        height = view.findViewById(R.id.height);
        hipSize = view.findViewById(R.id.hipSize);
        neckSize = view.findViewById(R.id.neckSize);
        waistSize = view.findViewById(R.id.waistSize);
        weight = view.findViewById(R.id.weight);
        hipsContainer = view.findViewById(R.id.hipsContainer);
        bodyFatPercent = view.findViewById(R.id.bodyFatPercent);
        recalcBtn = view.findViewById(R.id.recalcBtn);
        recalcBtn.setOnClickListener(this);

        userViewModel.getMutableBodyFat().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                bodyFatPercent.setText(String.valueOf(aDouble));
            }
        });

        height.setText(String.valueOf(userViewModel.getMutableHeight().getValue()));
        hipSize.setText(String.valueOf(userViewModel.getMutableHipSize().getValue()));
        neckSize.setText(String.valueOf(userViewModel.getMutableNeckSize().getValue()));
        waistSize.setText(String.valueOf(userViewModel.getMutableWaistSize().getValue()));
        weight.setText(String.valueOf(userViewModel.getMutableWeight().getValue()));

        if(userViewModel.getMutableGender().equals("Female")){
            hipsContainer.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onClick(View v) { // TODO: shouldn't update them all, just update the ones that changed.
        userViewModel.updateMeasurement("height", Integer.parseInt(height.getText().toString()));
        userViewModel.updateMeasurement("hipSize", Integer.parseInt(hipSize.getText().toString()));
        userViewModel.updateMeasurement("neckSize", Integer.parseInt(neckSize.getText().toString()));
        userViewModel.updateMeasurement("waistSize", Integer.parseInt(waistSize.getText().toString()));
        userViewModel.updateMeasurement("weight", Integer.parseInt(weight.getText().toString()));
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) { //consider using this method to track if a measurement has changed and hasnt been updated

    }
}