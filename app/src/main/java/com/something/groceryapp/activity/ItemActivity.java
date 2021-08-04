package com.something.groceryapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.something.groceryapp.R;
import com.something.groceryapp.adapter.CategoriesAdapter;
import com.something.groceryapp.adapter.ItemsAdapter;
import com.something.groceryapp.model.Cart;
import com.something.groceryapp.model.GroceryItem;
import com.something.groceryapp.model.Shared;
import com.something.groceryapp.model.Shared;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class ItemActivity extends AppCompatActivity {

    List<GroceryItem> groceryItems;
    ItemsAdapter itemsAdapter;
    RecyclerView itemRecyclerView;
    FirebaseDatabase rootnode;
    ImageView emptyCartImage;
    ProgressBar progressBar;
    DatabaseReference reference, itemReference, addToCartReference;
    String categoryName,categoryKey;
    Shared shared;
    TextView txt_item;
    SearchView searchView;
    CardView searchCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Intent intent = getIntent();
        categoryName = intent.getStringExtra("categoryName");
        categoryKey = intent.getStringExtra("categoryKey");
        txt_item = findViewById(R.id.txt_heading);
        txt_item.setText(categoryName);
        initViews();
    }

    private void initViews() {
        itemRecyclerView = findViewById(R.id.itemListRecyclerView);
        groceryItems = new ArrayList<>();
        itemsAdapter = new ItemsAdapter(ItemActivity.this,groceryItems, ItemActivity.this);
        itemRecyclerView.setAdapter(itemsAdapter);
        rootnode = FirebaseDatabase.getInstance();
        reference = rootnode.getReference("categories");
        addToCartReference = rootnode.getReference("users");
        shared = new Shared(ItemActivity.this);
        emptyCartImage = findViewById(R.id.empty_item);
        progressBar = findViewById(R.id.item_progress);
        searchView = findViewById(R.id.search_view);
        searchCardView = findViewById(R.id.search_cardView);
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
                if(groceryItems.isEmpty()) {
                    emptyCartImage.setVisibility(View.VISIBLE);
                    searchCardView.setVisibility(View.GONE);
                }

                itemRecyclerView.setAdapter(new ItemsAdapter(ItemActivity.this, groceryItems,ItemActivity.this));
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ItemActivity.this, "Failed. Please try again later.", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });

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

    public void addToCart(String itemName, String itemPrice, int no_of_items, int itemTotalPrice)
    {
        String user_key = shared.getUserKeyShared();
        String item_cart_key = addToCartReference.push().getKey();
        Cart cartHelper = new Cart(itemName,itemPrice,no_of_items,item_cart_key,itemTotalPrice);
        addToCartReference.child(user_key).child("add_to_cart").child(item_cart_key).setValue(cartHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ItemActivity.this,"Item added to cart",Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ItemActivity.this, "Failed to add item. Please try again later.", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        if(searchView!=null)
        {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return false;
                }
            });
        }
    }

    private void search(String str) {
        List<GroceryItem> itemSearchList = new ArrayList<>();
        for(GroceryItem itemHelperObject: groceryItems)
        {
            if(itemHelperObject.getItemName().toLowerCase().contains(str.toLowerCase()))
                itemSearchList.add(itemHelperObject);
        }
        itemRecyclerView.setAdapter(new ItemsAdapter(ItemActivity.this, itemSearchList,ItemActivity.this));


    }

}