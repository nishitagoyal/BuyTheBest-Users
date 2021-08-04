package com.something.groceryapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.something.groceryapp.R;
import com.something.groceryapp.adapter.ItemsAdapter;
import com.something.groceryapp.adapter.OrdersPlacedAdapter;
import com.something.groceryapp.model.GroceryItem;
import com.something.groceryapp.model.OrderPlaced;
import com.something.groceryapp.model.Shared;

import java.util.ArrayList;
import java.util.List;

public class OrdersPlacedActivity extends AppCompatActivity {

    RecyclerView orderPlacedRecyclerView;
    DatabaseReference orderPlaceRef;
    FirebaseDatabase rootnode;
    Shared shared;
    String user_key;
    List<OrderPlaced> orderPlacedList;
    List<OrderPlaced> reverseOrderPlacedList;
    ProgressBar progressBar;
    ImageView emptyCartImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_placed);
        initViews();
    }

    private void initViews() {
        orderPlacedRecyclerView = findViewById(R.id.ordersListRecyclerView);
        rootnode = FirebaseDatabase.getInstance();
        shared = new Shared(OrdersPlacedActivity.this);
        user_key = shared.getUserKeyShared();
        orderPlaceRef = rootnode.getReference("users").child(user_key).child("orders_placed");
        orderPlacedList = new ArrayList<>();
        reverseOrderPlacedList = new ArrayList<>();
        progressBar = findViewById(R.id.orders_progress);
        emptyCartImage = findViewById(R.id.empty_orders);
        populateList();
    }

    private void populateList() {
        orderPlaceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren())
                {
                    OrderPlaced orderPlaced = ds.getValue(OrderPlaced.class);
                    orderPlacedList.add(orderPlaced);
                }
                if(orderPlacedList.isEmpty())
                    emptyCartImage.setVisibility(View.VISIBLE);
                for(int i=orderPlacedList.size()-1; i>=0; i--)
                {
                    reverseOrderPlacedList.add(orderPlacedList.get(i));
                }
                orderPlacedRecyclerView.setAdapter(new OrdersPlacedAdapter(OrdersPlacedActivity.this, reverseOrderPlacedList));
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrdersPlacedActivity.this, "Failed to fetch cart details. Please try again later.", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OrdersPlacedActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}