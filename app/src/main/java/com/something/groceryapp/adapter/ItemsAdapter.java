package com.something.groceryapp.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.something.groceryapp.R;
import com.something.groceryapp.activity.ItemActivity;
import com.something.groceryapp.model.Categories;
import com.something.groceryapp.model.GroceryItem;
import com.something.groceryapp.model.Shared;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder> {

    Context mContext;
    List<GroceryItem> groceryItems;
    Shared shared;
    ItemActivity itemActivity;

    public ItemsAdapter(Context mContext, List<GroceryItem> groceryItems, ItemActivity itemActivity) {
        this.mContext = mContext;
        this.groceryItems = groceryItems;
        this.itemActivity = itemActivity;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root;
        root = LayoutInflater.from(mContext).inflate(R.layout.grocery_item_layout,parent,false);
        ItemsAdapter.ItemsViewHolder holder = new ItemsAdapter.ItemsViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {

        GroceryItem groceryItem = groceryItems.get(position);
        Uri itemUri = Uri.parse(groceryItem.getItemImage());
        Picasso.get().load(itemUri).into(holder.itemImage);
        shared = new Shared(mContext);
        holder.itemText.setText(groceryItem.getItemName() + " - " + groceryItem.getItemQty());
        holder.itemPrice.setText(groceryItem.getItemPrice());
        if(groceryItem.getItemAvailability().equalsIgnoreCase("Out of stock"))
        {
            holder.itemAvailibility.setVisibility(View.VISIBLE);
            holder.addButton.setEnabled(false);
        }
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = holder.itemText.getText().toString();
                String itemPrice = holder.itemPrice.getText().toString();
                int price = Integer.parseInt(itemPrice.substring(4,itemPrice.length()));
                int no_of_items = Integer.parseInt(holder.elegantNumberButton.getNumber());
                int itemTotalPrice = price*no_of_items;
                itemActivity.addToCart(itemName,itemPrice,no_of_items,itemTotalPrice);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

    public class ItemsViewHolder extends RecyclerView.ViewHolder
    {

        private ImageView itemImage;
        private TextView itemText;
        private TextView itemPrice;
        private Button addButton;
        private ElegantNumberButton elegantNumberButton;
        private ImageView itemAvailibility;

        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemText = itemView.findViewById(R.id.itemTitle);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            addButton = itemView.findViewById(R.id.item_add_cart_button);
            elegantNumberButton = itemView.findViewById(R.id.qty_elegant_number);
            itemAvailibility = itemView.findViewById(R.id.itemAvailabilityIV);
        }
    }
}
