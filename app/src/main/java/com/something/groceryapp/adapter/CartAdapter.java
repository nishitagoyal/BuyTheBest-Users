package com.something.groceryapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.something.groceryapp.R;
import com.something.groceryapp.model.Cart;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context mContext;
    List<Cart> cartList;
//    Integer[] items = new Integer[]{1,2,3,4};
//    ArrayAdapter<Integer> adapter;

    public CartAdapter(Context mContext, List<Cart> cartList) {
        this.mContext = mContext;
        this.cartList = cartList;
//        adapter = new ArrayAdapter<Integer>(mContext,android.R.layout.simple_spinner_item, items);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
        holder.cartItemDetails.setText(cartList.get(position).getItemDetail() + "\n" + cartList.get(position).getItemPrice());
        holder.cartTotalPriceDetails.setText(cartList.get(position).getItemTotalPrice());
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        private TextView cartItemDetails;
        private TextView cartTotalPriceDetails;
//        private Spinner quantitySpinner;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cartItemDetails = itemView.findViewById(R.id.item_detail_text);
            cartTotalPriceDetails = itemView.findViewById(R.id.cart_total_text);
//            quantitySpinner = itemView.findViewById(R.id.quantity_spinner);
//            quantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    //do something
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
        }
    }
}
