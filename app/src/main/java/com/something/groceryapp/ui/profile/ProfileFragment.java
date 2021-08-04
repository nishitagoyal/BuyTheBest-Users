package com.something.groceryapp.ui.profile;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.something.groceryapp.R;
import com.something.groceryapp.activity.LoginActivity;
import com.something.groceryapp.activity.MainActivity;
import com.something.groceryapp.activity.OrdersPlacedActivity;
import com.something.groceryapp.model.Shared;

public class ProfileFragment extends Fragment {

    LinearLayout orderLayout,logoutLayout, editProfileLayout;
    Shared shared;
    TextView userNameTV, userAddTV, userPhoneTV;
    String userName, userAdd, userPhone;
    DisplayMetrics metrics;
    int width, height;
    DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        orderLayout = root.findViewById(R.id.orders_layout);
        logoutLayout = root.findViewById(R.id.logout_layout);
        editProfileLayout = root.findViewById(R.id.editDetails_layout);
        userNameTV = root.findViewById(R.id.userNameTV);
        userAddTV = root.findViewById(R.id.userAddressTV);
        userPhoneTV = root.findViewById(R.id.userPhoneTV);
        userNameTV.setText(userName);
        userAddTV.setText(userAdd);
        userPhoneTV.setText(userPhone);
        orderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                Intent intent = new Intent(getActivity(), OrdersPlacedActivity.class);
                startActivity(intent);
            }
        });
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shared.setFirstTimeLaunched(true);
                Intent intent = new Intent (getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        editProfileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDetails();
            }
        });

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shared = new Shared(getContext());
        userAdd = shared.getUserAddressShared();
        userName = shared.getUserNameShared();
        userPhone = shared.getUserPhoneShared();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        metrics = getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;
    }

    private void editDetails()
    {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.edit_profile_dialog);
        dialog.setTitle("Edit Details");
        dialog.getWindow().setLayout((6 * width)/7, LinearLayout.LayoutParams.WRAP_CONTENT);
        Button button = (Button) dialog.findViewById(R.id.editDetailsButton);
        TextInputEditText editName = dialog.findViewById(R.id.name_editText);
        TextInputEditText editPhone = dialog.findViewById(R.id.phoneNum_editText);
        TextInputEditText editAddress = dialog.findViewById(R.id.address_editText);
        editName.setText(shared.getUserNameShared());
        editPhone.setText(shared.getUserPhoneShared());
        editAddress.setText(shared.getUserAddressShared());
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(!editName.getText().toString().isEmpty() && !editPhone.getText().toString().isEmpty() && !editAddress.getText().toString().isEmpty())
                {
                    databaseReference.child(shared.getUserKeyShared()).child("userName").setValue(editName.getText().toString());
                    databaseReference.child(shared.getUserKeyShared()).child("userPhoneno").setValue(editPhone.getText().toString());
                    databaseReference.child(shared.getUserKeyShared()).child("userAddress").setValue(editAddress.getText().toString());
                    shared.setUserNameShared(editName.getText().toString());
                    shared.setUserPhoneShared(editPhone.getText().toString());
                    shared.setUserAddressShared(editAddress.getText().toString());
                    userNameTV.setText(shared.getUserNameShared());
                    userAddTV.setText(shared.getUserAddressShared());
                    userPhoneTV.setText(shared.getUserPhoneShared());
                    dialog.dismiss();

                }
                if(editName.getText().toString().isEmpty())
                {
                    editName.setError(getString(R.string.field_required));
                }
                if(editPhone.getText().toString().isEmpty())
                {
                    editPhone.setError(getString(R.string.field_required));
                }
                if(editAddress.getText().toString().isEmpty())
                {
                    editAddress.setError(getString(R.string.field_required));
                }
            }
        });
        dialog.show();
    }
}