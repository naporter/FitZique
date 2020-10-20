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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.workoutapp.LoginPageActivity;
import com.example.workoutapp.R;

import java.util.Objects;

public class LoginFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private EditText loginNumber;
    private EditText loginPassword;
    private TextView forgotPassword;
    private Button loginBtn;
    private LinearLayout layout;

    private NavController navController;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        view.findViewById(R.id.signUp).setOnClickListener(this);
        view.findViewById(R.id.loginBtn).setOnClickListener(this);
        view.findViewById(R.id.forgotPassword).setOnClickListener(this);
        loginPassword = view.findViewById(R.id.loginPassword);
        loginNumber = view.findViewById(R.id.email);
        loginBtn = view.findViewById(R.id.loginBtn);
        loginBtn.setClickable(false);
        layout = view.findViewById(R.id.layout);
        forgotPassword = view.findViewById(R.id.forgotPassword);
        loginNumber.addTextChangedListener(this);
        loginPassword.addTextChangedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signUp:
                navController.navigate(R.id.action_loginFragment_to_registerFragment);
                break;
            case R.id.loginBtn:
                String username = loginNumber.getText().toString();
                String password = loginPassword.getText().toString();
                ((LoginPageActivity)getActivity()).signIn(username, password);
                break;
            case R.id.forgotPassword:
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
        if (!TextUtils.isEmpty(loginNumber.getText().toString()) && !TextUtils.isEmpty(loginPassword.getText().toString())) {
            loginBtn.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.ripple_effect));
            loginBtn.setClickable(true);
        }else {
            loginBtn.setBackgroundColor(getResources().getColor(R.color.cardview_shadow_start_color));
            loginBtn.setClickable(false);
        }
    }
}