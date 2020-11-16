package com.example.workoutapp.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.MainActivity;
import com.example.workoutapp.R;
import com.example.workoutapp.UserViewModel;
import com.example.workoutapp.databinding.FragmentProfileBinding;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.util.Objects;

import static java.lang.Double.isNaN;

public class ProfileFragment extends Fragment implements View.OnFocusChangeListener, FriendRecyclerViewAdapter.FriendViewHolder.OnClickListener{

    private EditText height, hipSize, neckSize, waistSize, weight, newFriendPhoneNumber;
    private TextInputLayout hipsContainer;
    private TextView bodyFatPercent;
    private ImageButton addFriendBtn, add;
    private FriendRecyclerViewAdapter friendRecyclerViewAdapter;
    private TableRow addFriendRow;
    private InputMethodManager imm;
    private UserViewModel userViewModel;
    private FragmentProfileBinding fragmentProfileBinding;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        this.getParentFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        friendRecyclerViewAdapter = new FriendRecyclerViewAdapter(requireActivity(), Objects.requireNonNull(userViewModel.getUser().getValue()).getFriends(), this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        fragmentProfileBinding.setUser(userViewModel.getUser().getValue());
        fragmentProfileBinding.setProfile(this);
        fragmentProfileBinding.friendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentProfileBinding.friendRecyclerView.setAdapter(friendRecyclerViewAdapter);
        fragmentProfileBinding.setLifecycleOwner(requireActivity());
        fragmentProfileBinding.executePendingBindings();

        add = fragmentProfileBinding.getRoot().findViewById(R.id.add);
        height = fragmentProfileBinding.getRoot().findViewById(R.id.height);
        weight = fragmentProfileBinding.getRoot().findViewById(R.id.weight);
        hipSize = fragmentProfileBinding.getRoot().findViewById(R.id.hipSize);
        neckSize = fragmentProfileBinding.getRoot().findViewById(R.id.neckSize);
        waistSize = fragmentProfileBinding.getRoot().findViewById(R.id.waistSize);
        addFriendRow = fragmentProfileBinding.getRoot().findViewById(R.id.addFriendRow);
        addFriendBtn = fragmentProfileBinding.getRoot().findViewById(R.id.addFriendBtn);
        hipsContainer = fragmentProfileBinding.getRoot().findViewById(R.id.hipsContainer);
        bodyFatPercent = fragmentProfileBinding.getRoot().findViewById(R.id.bodyFatPercent);
        newFriendPhoneNumber = fragmentProfileBinding.getRoot().findViewById(R.id.newFriendPhoneNumber);


        ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            private final ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark, getActivity().getTheme()));

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder confirmDelete = new AlertDialog.Builder(getContext());
                confirmDelete.setMessage("Are you sure you want to remove " + userViewModel.getUser().getValue().getFriends().get(viewHolder.getAdapterPosition()).getFirstName() + " as a friend?");
                confirmDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity) requireActivity()).removeFriend(userViewModel.getUser().getValue().getFriends().get(viewHolder.getAdapterPosition()).getUid());
                        userViewModel.getUser().getValue().getFriends().remove(viewHolder.getAdapterPosition());
                        friendRecyclerViewAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
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
        itemTouchHelper.attachToRecyclerView(fragmentProfileBinding.friendRecyclerView);

        if(userViewModel.getUser().getValue().getGender().equals("Female")){
            hipsContainer.setVisibility(View.VISIBLE);
        }
        return fragmentProfileBinding.getRoot();
    }

    public void recalculateBodyFat(){
        if(userViewModel.getUser().getValue().getWaistSize() != (Integer.parseInt(waistSize.getText().toString()))){
            ((MainActivity) requireActivity()).updateMeasurement("waistSize", Integer.parseInt(waistSize.getText().toString()));
        }
        if(userViewModel.getUser().getValue().getHeight() != (Integer.parseInt(height.getText().toString()))){
            ((MainActivity) requireActivity()).updateMeasurement("height", Integer.parseInt(height.getText().toString()));
        }
        if(userViewModel.getUser().getValue().getNeckSize() != (Integer.parseInt(neckSize.getText().toString()))){
            ((MainActivity) requireActivity()).updateMeasurement("neckSize", Integer.parseInt(neckSize.getText().toString()));
        }
        if(userViewModel.getUser().getValue().getWeight() != (Integer.parseInt(weight.getText().toString()))){
            ((MainActivity) requireActivity()).updateMeasurement("weight", Integer.parseInt(weight.getText().toString()));
        }
        if(userViewModel.getUser().getValue().getHipSize() != (Integer.parseInt(hipSize.getText().toString()))){
            ((MainActivity) requireActivity()).updateMeasurement("hipSize", Integer.parseInt(hipSize.getText().toString()));
        }
        imm.hideSoftInputFromWindow(addFriendBtn.getWindowToken(), 0);
        updateBodyFat();
    }

    public void updateBodyFat(){
        int waistSize = Integer.parseInt(this.waistSize.getText().toString());
        int hipSize = Integer.parseInt(this.hipSize.getText().toString());
        int neckSize = Integer.parseInt(this.neckSize.getText().toString());
        int height = Integer.parseInt(this.height.getText().toString());
        DecimalFormat df = new DecimalFormat("####0.00");
        double bodyFat;
        if(userViewModel.getUser().getValue().getGender().equals("Female")){
            bodyFat = 163.205 * Math.log10(waistSize + hipSize - neckSize) - 97.684 * Math.log10(height) + 36.76;
        }else{
            bodyFat = 86.010 * Math.log10(waistSize - neckSize) - 70.041 * Math.log10(height) + 36.76;
        }
        if (bodyFat < 0.1 || isNaN(bodyFat)){
            bodyFat = 0.1;
        }
        bodyFat = Double.parseDouble(df.format(bodyFat));
        ((MainActivity) requireActivity()).updateBodyFat(bodyFat);
    }

    public void setAddFriendVisibility(){
        if(addFriendRow.getVisibility() == View.VISIBLE){
            addFriendBtn.animate().rotation(0).start();
            addFriendRow.setVisibility(View.GONE);
        }else {
            addFriendBtn.animate().rotation(45).start();
            addFriendRow.setVisibility(View.VISIBLE);
        }
    }

    public void addFriend(){
        if(!TextUtils.isEmpty(newFriendPhoneNumber.getText().toString())){
            ((MainActivity) requireActivity()).addFriend(newFriendPhoneNumber.getText().toString());
            newFriendPhoneNumber.getText().clear();
            addFriendRow.setVisibility(View.GONE);
            addFriendBtn.animate().rotation(0).start();
            imm.hideSoftInputFromWindow(addFriendBtn.getWindowToken(), 0);
        }
        fragmentProfileBinding.executePendingBindings();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) { //consider using this method to track if a measurement has changed and hasnt been updated
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}