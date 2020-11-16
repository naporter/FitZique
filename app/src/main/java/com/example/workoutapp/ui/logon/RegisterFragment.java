package com.example.workoutapp.ui.logon;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.workoutapp.activities.LoginPageActivity;
import com.example.workoutapp.R;
import com.example.workoutapp.database.FirebaseAccessor;


public class RegisterFragment extends Fragment implements View.OnClickListener, TextWatcher{

    private NavController navController;
    public EditText firstName, lastName, phoneNumber, password, email;
    private Button nextBtn;

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
            case R.id.nextBtn:
                final FirebaseAccessor firebaseAccessor = ((LoginPageActivity) requireActivity()).alreadyUser(this.email.getText().toString());
                firebaseAccessor.getIsRegistered().observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        firebaseAccessor.getIsRegistered().removeObserver(this);
                        if(aBoolean){
                            Toast.makeText(getContext(), "Email available.", Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle(); // bundles the users info from registerFragment to use in Demographics fragment
                            bundle.putString("firstName", firstName.getText().toString());
                            bundle.putString("lastName", lastName.getText().toString());
                            bundle.putString("phoneNumber", phoneNumber.getText().toString());
                            bundle.putString("email", email.getText().toString());
                            bundle.putString("password", password.getText().toString());
                            navController.navigate(R.id.action_registerFragment_to_demographicsFragment, bundle);
                        }
                        else{
                            Toast.makeText(getContext(), "Email unavailable. Use another email or go back to sign in.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
            nextBtn.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.ripple_effect));
            nextBtn.setClickable(true);

        }else {
            nextBtn.setBackgroundColor(getResources().getColor(R.color.cardview_shadow_start_color));
            nextBtn.setClickable(false);
        }
    }

}