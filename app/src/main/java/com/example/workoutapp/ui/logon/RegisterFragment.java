package com.example.workoutapp.ui.logon;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.workoutapp.LoginPageActivity;
import com.example.workoutapp.R;
import com.example.workoutapp.MainActivity;


public class RegisterFragment extends Fragment implements View.OnClickListener, TextWatcher{

    private NavController navController;
    private EditText firstName, lastName, phoneNumber, password, email;
    private Button signUpBtn;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        view.findViewById(R.id.goBack).setOnClickListener(this);
        view.findViewById(R.id.signUpBtn).setOnClickListener(this);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        email = view.findViewById(R.id.email);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        password = view.findViewById(R.id.password);
        signUpBtn = view.findViewById(R.id.signUpBtn);
        firstName.addTextChangedListener(this);
        lastName.addTextChangedListener(this);
        phoneNumber.addTextChangedListener(this);
        password.addTextChangedListener(this);
        email.addTextChangedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.goBack:
                navController.navigateUp();
                break;
            case R.id.signUpBtn:
                ((LoginPageActivity)getActivity()).createAccount(email.getText().toString(), firstName.getText().toString(), lastName.getText().toString(), phoneNumber.getText().toString(), password.getText().toString());

                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(firstName.getText().toString()) && !TextUtils.isEmpty(lastName.getText().toString())&& !TextUtils.isEmpty(phoneNumber.getText().toString())
                && !TextUtils.isEmpty(password.getText().toString())) {
            signUpBtn.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ripple_effect));
            signUpBtn.setClickable(true);

        }else {
            signUpBtn.setBackgroundColor(getResources().getColor(R.color.cardview_shadow_start_color));
            signUpBtn.setClickable(false);
        }
    }
}