package com.something.groceryapp.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.something.groceryapp.R;
import com.something.groceryapp.activity.ConfirmationActivity;
import com.something.groceryapp.activity.LoginActivity;
import com.something.groceryapp.activity.SplashActivity;
import com.something.groceryapp.adapter.CartAdapter;
import com.something.groceryapp.model.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    RecyclerView cartRecyclerView;
    List<Cart> cartList;
    LinearLayout placeOrderLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);
        cartRecyclerView = root.findViewById(R.id.cartListRecyclerView);
        cartRecyclerView.setAdapter(new CartAdapter(getContext(),cartList));
        placeOrderLayout = root.findViewById(R.id.place_order_layout);
        placeOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConfirmationActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartList = new ArrayList<>();
        populateList();
    }

    private void populateList() {
        cartList.add(new Cart("Milk 1L","Rs 60.00","Qty. 10 \n 600.00"));
        cartList.add(new Cart("Milk 1L","Rs 60.00","Qty. 10 \n 600.00"));
        cartList.add(new Cart("Milk 1L","Rs 60.00","Qty. 10 \n 600.00"));

    }
}