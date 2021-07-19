package com.something.groceryapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.something.groceryapp.R;
import com.something.groceryapp.adapter.CategoriesAdapter;
import com.something.groceryapp.adapter.ItemsAdapter;
import com.something.groceryapp.model.GroceryItem;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class ItemActivity extends AppCompatActivity {

    List<GroceryItem> groceryItems;
    ItemsAdapter itemsAdapter;
    RecyclerView itemRecyclerView;
    FirebaseDatabase rootnode;
    DatabaseReference reference, itemReference;
    String categoryName,categoryKey;
    TextView txt_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        categoryName = intent.getStringExtra("categoryName");
        categoryKey = intent.getStringExtra("categoryKey");
        txt_item = findViewById(R.id.txt_heading);
        txt_item.setText(categoryName);
//        actionBar.setTitle(categoryName);
        initViews();
    }

    private void initViews() {
        itemRecyclerView = findViewById(R.id.itemListRecyclerView);
        groceryItems = new ArrayList<>();
        itemsAdapter = new ItemsAdapter(ItemActivity.this,groceryItems, ItemActivity.this);
        itemRecyclerView.setAdapter(itemsAdapter);
        rootnode = FirebaseDatabase.getInstance();
        reference = rootnode.getReference("categories");
        populateList();

    }

    private void populateList() {

        itemReference = reference.child(categoryKey).child("items");
        itemReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren())
                {
                    GroceryItem groceryItem = ds.getValue(GroceryItem.class);
                    groceryItems.add(groceryItem);
                }
                itemRecyclerView.setAdapter(new ItemsAdapter(ItemActivity.this, groceryItems,ItemActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(submitButton.getWindowToken(), 0);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}