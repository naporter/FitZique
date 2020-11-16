package com.example.workoutapp.ui.logon;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.workoutapp.activities.LoginPageActivity;
import com.example.workoutapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;

import static java.lang.Double.isNaN;

public class DemographicsFragment extends Fragment implements View.OnClickListener, TextWatcher, TabLayout.OnTabSelectedListener {

    private NavController navController;
    private EditText weight, height, waist, neck, hips, birthday;
    private TextInputLayout hipsContainer;
    private TabLayout gender;
    private Button signUpBtn;
    private String firstName, lastName, phoneNumber, email, password;

    public DemographicsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firstName = (String) getArguments().get("firstName");
        lastName = (String) getArguments().get("lastName");
        phoneNumber = (String) getArguments().get("phoneNumber");
        email = (String) getArguments().get("email");
        password = (String) getArguments().get("password");

        navController = Navigation.findNavController(view);
        gender = view.findViewById(R.id.gender);
        birthday = view.findViewById(R.id.birthdayInit);
        weight = view.findViewById(R.id.weightInit);
        height = view.findViewById(R.id.heightInit);
        waist = view.findViewById(R.id.waistInit);
        neck = view.findViewById(R.id.neckInit);
        hips = view.findViewById(R.id.hipsInit);
        signUpBtn = view.findViewById(R.id.signUpBtn);
        hipsContainer = view.findViewById(R.id.hipsContainer);

        view.findViewById(R.id.goBack).setOnClickListener(this);
        view.findViewById(R.id.signUpBtn).setOnClickListener(this);
        gender.addOnTabSelectedListener(this);

        birthday.addTextChangedListener(this);
        weight.addTextChangedListener(this);
        height.addTextChangedListener(this);
        waist.addTextChangedListener(this);
        neck.addTextChangedListener(this);
        hips.addTextChangedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_demographics, container, false);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (birthday.getText().length() == 10) {
            if (birthday.getText().hashCode() == s.hashCode()) {
                String birthdayText = birthday.getText().toString();
                if (birthdayText.charAt(2) != '/' && birthdayText.charAt(4) != '/') {
                    birthday.setTextColor(Color.RED);
                } else {
                    birthday.setTextColor(Color.BLACK);
                }
            }
        } else {
            birthday.setTextColor(Color.RED);
        }
        if (!TextUtils.isEmpty(weight.getText().toString()) && !TextUtils.isEmpty(height.getText().toString()) && !TextUtils.isEmpty(waist.getText().toString())
                && !TextUtils.isEmpty(neck.getText().toString()) && !TextUtils.isEmpty(birthday.getText().toString()) && !(hipsContainer.getVisibility() == View.VISIBLE && TextUtils.isEmpty(hips.getText().toString()))) {
            signUpBtn.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.ripple_effect));
            signUpBtn.setClickable(true);
        } else {
            signUpBtn.setBackgroundColor(getResources().getColor(R.color.cardview_shadow_start_color));
            signUpBtn.setClickable(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 1) {
            hipsContainer.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(hips.getText().toString())) {
                signUpBtn.setBackgroundColor(getResources().getColor(R.color.cardview_shadow_start_color));
                signUpBtn.setClickable(false);
            }
        } else {
            hipsContainer.setVisibility(View.GONE);
            hips.getText().clear();
            if (!TextUtils.isEmpty(weight.getText().toString()) && !TextUtils.isEmpty(height.getText().toString()) && !TextUtils.isEmpty(waist.getText().toString())
                    && !TextUtils.isEmpty(neck.getText().toString()) && !TextUtils.isEmpty(birthday.getText().toString())) {
                signUpBtn.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.ripple_effect));
                signUpBtn.setClickable(true);
            }
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goBack:
                navController.navigateUp();
                break;
            case R.id.signUpBtn:
                if (gender.getSelectedTabPosition() == 0) { //hip size is not used when calculating a males body fat percentage
                    hips.setText("0");
                }
                String gender = this.gender.getTabAt(this.gender.getSelectedTabPosition()).getText().toString();
                String birthday = this.birthday.getText().toString();
                int waistSize = Integer.parseInt(waist.getText().toString());
                int weight = Integer.parseInt(this.weight.getText().toString());
                int height = Integer.parseInt(this.height.getText().toString());
                int neckSize = Integer.parseInt(this.neck.getText().toString());;
                int hipSize = Integer.parseInt(this.hips.getText().toString());;

                DecimalFormat df = new DecimalFormat("####0.00");
                double bodyFat;
                if(gender.equals("Female")){
                    bodyFat = 163.205 * Math.log10(waistSize + hipSize - neckSize) - 97.684 * Math.log10(height) - 78.387;
                }else{
                    bodyFat = 86.010 * Math.log10(waistSize - neckSize) - 70.041 * Math.log10(height) + 36.76;
                }
                if (bodyFat < 0.1 || isNaN(bodyFat)){
                    bodyFat = 0.1;
                }
                bodyFat = Double.parseDouble(df.format(bodyFat));
                ((LoginPageActivity)requireActivity()).register(email, password, phoneNumber, birthday, firstName, lastName, gender, bodyFat, weight, height, neckSize, waistSize, hipSize);
                break;
        }
    }
}