package com.example.workoutapp.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.workoutapp.Friend;
import com.example.workoutapp.R;
import com.example.workoutapp.UserViewModel;
import com.example.workoutapp.ui.workouts.RecyclerViewAdapter;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, FriendRecyclerViewAdapter.FriendViewHolder.OnClickListener{

    private EditText height, hipSize, neckSize, waistSize, weight, newFriendPhoneNumber;
    private TextInputLayout hipsContainer;
    private TextView bodyFatPercent;
    private Button recalcBtn;
    private ImageButton addFriendBtn, add;
    private UserViewModel userViewModel;
    private RecyclerView friendRecyclerView;
    private FriendRecyclerViewAdapter friendRecyclerViewAdapter;
    private TableRow addFriendRow;
    private InputMethodManager imm;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        friendRecyclerViewAdapter = new FriendRecyclerViewAdapter(getContext(), userViewModel.getFriends().getValue(), this);
        this.getParentFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        friendRecyclerView = view.findViewById(R.id.friendRecyclerView);
        imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        friendRecyclerView.setAdapter(friendRecyclerViewAdapter);
        friendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addFriendRow = view.findViewById(R.id.addFriendRow);
        height = view.findViewById(R.id.height);
        hipSize = view.findViewById(R.id.hipSize);
        neckSize = view.findViewById(R.id.neckSize);
        waistSize = view.findViewById(R.id.waistSize);
        weight = view.findViewById(R.id.weight);
        newFriendPhoneNumber = view.findViewById(R.id.newFriendPhoneNumber);
        hipsContainer = view.findViewById(R.id.hipsContainer);
        bodyFatPercent = view.findViewById(R.id.bodyFatPercent);
        recalcBtn = view.findViewById(R.id.recalcBtn);
        addFriendBtn = view.findViewById(R.id.addFriendBtn);
        add = view.findViewById(R.id.add);

        recalcBtn.setOnClickListener(this);
        addFriendBtn.setOnClickListener(this);
        add.setOnClickListener(this);

        userViewModel.getMutableBodyFat().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                bodyFatPercent.setText(String.valueOf(aDouble));
            }
        });

        userViewModel.getFriends().observe(getViewLifecycleOwner(), new Observer<ArrayList<Friend>>() {
            @Override
            public void onChanged(ArrayList<Friend> friend) {
                System.out.println(friend);
                friendRecyclerViewAdapter.notifyDataSetChanged();
            }
        });

        ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            private final ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark, getActivity().getTheme()));

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder confirmDelete = new AlertDialog.Builder(getContext());
                confirmDelete.setMessage("Are you sure you want to remove " + userViewModel.getFriends().getValue().get(viewHolder.getAdapterPosition()).getFirstName() + " as a friend?");
                confirmDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userViewModel.removeFriend(viewHolder.getAdapterPosition());
                    }
                });
                confirmDelete.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        friendRecyclerViewAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
                });
                AlertDialog alertDialog = confirmDelete.create();
                alertDialog.show();
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;

                if (dX > 0) {
                    colorDrawable.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX), itemView.getBottom());
                } else if (dX < 0) {
                    colorDrawable.setBounds(itemView.getRight() + ((int) dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
                } else {
                    colorDrawable.setBounds(0, 0, 0, 0);
                }
                colorDrawable.draw(c);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallback);
        itemTouchHelper.attachToRecyclerView(friendRecyclerView);

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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.recalcBtn:
                // TODO: shouldn't update them all, just update the ones that changed.
                userViewModel.updateMeasurement("height", Integer.parseInt(height.getText().toString()));
                userViewModel.updateMeasurement("hipSize", Integer.parseInt(hipSize.getText().toString()));
                userViewModel.updateMeasurement("neckSize", Integer.parseInt(neckSize.getText().toString()));
                userViewModel.updateMeasurement("waistSize", Integer.parseInt(waistSize.getText().toString()));
                userViewModel.updateMeasurement("weight", Integer.parseInt(weight.getText().toString()));
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
            case R.id.addFriendBtn:
                if(addFriendRow.getVisibility() == View.VISIBLE){
                    addFriendBtn.animate().rotation(0).start();
                    addFriendRow.setVisibility(View.GONE);
                }else {
                    addFriendBtn.animate().rotation(45).start();
                    addFriendRow.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.add:
                if(!TextUtils.isEmpty(newFriendPhoneNumber.getText().toString())){
                    userViewModel.addFriend(newFriendPhoneNumber.getText().toString());
                    newFriendPhoneNumber.getText().clear();
                    addFriendRow.setVisibility(View.GONE);
                    addFriendBtn.animate().rotation(0).start();
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) { //consider using this method to track if a measurement has changed and hasnt been updated
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onClickListener(int position, String friendFirstName, String friendLastName) {

    }
}