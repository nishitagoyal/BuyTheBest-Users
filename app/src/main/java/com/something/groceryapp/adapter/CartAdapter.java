package com.something.groceryapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.something.groceryapp.R;
import com.something.groceryapp.model.Cart;
import com.something.groceryapp.ui.cart.CartFragment;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context mContext;
    List<Cart> cartList;
    CartFragment cartFragment;

    public CartAdapter(Context mContext, List<Cart> cartList, CartFragment cartFragment) {
        this.mContext = mContext;
        this.cartList = cartList;
        this.cartFragment = cartFragment;

    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root;
        root = LayoutInflater.from(mContext).inflate(R.layout.addcart_item_layout,parent,false);
        CartAdapter.CartViewHolder holder = new CartAdapter.CartViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cartItem = cartList.get(position);
        holder.cartItemDetails.setText(cartItem.getItemName() + "\n"  + cartItem.getItemPrice());
        holder.cartTotalPriceDetails.setText("Qty: " + String.valueOf(cartItem.getItemQty()) + "\n" + "Rs. " + cartItem.getItemTotalPrice());
        holder.remove_cart_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartFragment.removeItem(cartItem.getItem_cart_key());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        private TextView cartItemDetails;
        private TextView cartTotalPriceDetails;
        private ImageView remove_cart_iv;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cartItemDetails = itemView.findViewById(R.id.item_detail_text);
            cartTotalPriceDetails = itemView.findViewById(R.id.cart_total_text);
            remove_cart_iv = itemView.findViewById(R.id.cart_item_cancel);
        }
    }
}
