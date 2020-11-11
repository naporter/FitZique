package com.example.workoutapp.ui.logon;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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

import com.example.workoutapp.FirebaseRepository;
import com.example.workoutapp.LoginPageActivity;
import com.example.workoutapp.R;
import com.example.workoutapp.UserViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class RegisterFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private NavController navController;
    public EditText firstName, lastName, phoneNumber, password, email;
    private Button nextBtn;
    private UserViewModel userViewModel;


    public RegisterFragment() {
        // Required empty public constructor

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
        view.findViewById(R.id.nextBtn).setOnClickListener(this);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        email = view.findViewById(R.id.email);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        password = view.findViewById(R.id.password);
        nextBtn = view.findViewById(R.id.nextBtn);

        firstName.addTextChangedListener(this);
        lastName.addTextChangedListener(this);
        phoneNumber.addTextChangedListener(this);
        password.addTextChangedListener(this);
        email.addTextChangedListener(this);

        nextBtn.setClickable(false);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getRegisterUserLiveData().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    String message = "User created successully";
                    System.out.println(message);
                    Snackbar snackbar = Snackbar.make(requireActivity().findViewById(R.id.registerLayout), message, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    Bundle bundle = new Bundle(); // bundles the users info from registerFragment to use in Demographics fragment
                    bundle.putString("firstName", firstName.getText().toString());
                    bundle.putString("lastName", lastName.getText().toString());
                    bundle.putString("phoneNumber", phoneNumber.getText().toString());
                    bundle.putString("email", email.getText().toString());
                    bundle.putString("password", password.getText().toString());
                    userViewModel.getRegisterUserLiveData().removeObservers(getViewLifecycleOwner());
                    navController.navigate(R.id.action_registerFragment_to_demographicsFragment, bundle);
                } else {
                    String message = "A user with that email already exists.";
                    System.out.println(message);
                    Snackbar snackbar = Snackbar.make(requireActivity().findViewById(R.id.registerLayout), message, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goBack:
                navController.navigateUp();
                break;
            case R.id.nextBtn:
                ((LoginPageActivity) getActivity()).register(this.email.getText().toString(), this.password.getText().toString());
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
        if (!TextUtils.isEmpty(firstName.getText().toString()) && !TextUtils.isEmpty(lastName.getText().toString()) && !TextUtils.isEmpty(phoneNumber.getText().toString())
                && !TextUtils.isEmpty(password.getText().toString())) {
            nextBtn.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.ripple_effect));
            nextBtn.setClickable(true);

        } else {
            nextBtn.setBackgroundColor(getResources().getColor(R.color.cardview_shadow_start_color));
            nextBtn.setClickable(false);
        }
    }
}