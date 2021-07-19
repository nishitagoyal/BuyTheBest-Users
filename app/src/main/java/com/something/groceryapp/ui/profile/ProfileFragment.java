package com.something.groceryapp.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.something.groceryapp.R;
import com.something.groceryapp.activity.LoginActivity;
import com.something.groceryapp.activity.MainActivity;
import com.something.groceryapp.model.Shared;

public class ProfileFragment extends Fragment {

    LinearLayout orderLayout,logoutLayout;
    Shared shared;
    TextView userNameTV, userAddTV, userPhoneTV;
    String userName, userAdd, userPhone;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        orderLayout = root.findViewById(R.id.orders_layout);
        logoutLayout = root.findViewById(R.id.logout_layout);
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

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shared = new Shared(getContext());
        userAdd = shared.getUserAddressShared();
        userName = shared.getUserNameShared();
        userPhone = shared.getUserPhoneShared();
    }
}