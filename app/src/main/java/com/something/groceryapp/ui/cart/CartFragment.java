package com.something.groceryapp.ui.cart;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.something.groceryapp.R;
import com.something.groceryapp.activity.ConfirmationActivity;
import com.something.groceryapp.activity.ItemActivity;
import com.something.groceryapp.activity.LoginActivity;
import com.something.groceryapp.activity.SplashActivity;
import com.something.groceryapp.adapter.CartAdapter;
import com.something.groceryapp.adapter.CategoriesAdapter;
import com.something.groceryapp.model.Cart;
import com.something.groceryapp.model.Categories;
import com.something.groceryapp.model.OrderPlaced;
import com.something.groceryapp.model.Shared;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment {

    RecyclerView cartRecyclerView;
    List<Cart> cartList;
    LinearLayout placeOrderLayout;
    ImageView emptyCartImage;
    CardView placeOrderCard;
    DatabaseReference databaseReference, allOrdersReference;
    Shared shared;
    TextView totalPriceText;
    int total;
    String orderAddress, orderedItems;
    DisplayMetrics metrics;
    ProgressBar progressBar;
    int width, height;
    String user_key;

    public CartFragment() {}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);
        cartRecyclerView = root.findViewById(R.id.cartListRecyclerView);
        cartRecyclerView.setAdapter(new CartAdapter(getContext(),cartList,CartFragment.this));
        placeOrderLayout = root.findViewById(R.id.place_order_layout);
        emptyCartImage = root.findViewById(R.id.empty_cart);
        placeOrderCard = root.findViewById(R.id.place_order_card);
        totalPriceText = root.findViewById(R.id.total_text);
        progressBar = root.findViewById(R.id.cart_progress);
        placeOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });
        total = 0;
        populateList();
        return root;

    }

    private void showConfirmationDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.confirm_address_dialog);
        dialog.getWindow().setLayout((6 * width)/7, LinearLayout.LayoutParams.WRAP_CONTENT);
        Button confirmButton = (Button) dialog.findViewById(R.id.confirmButton);
        Button editButton = (Button) dialog.findViewById(R.id.editButton);
        ImageView cancelDialogIV = dialog.findViewById(R.id.order_placed_cancel);
        TextInputEditText orderAddressET = dialog.findViewById(R.id.order_address_et);
        TextInputEditText orderItemsET = dialog.findViewById(R.id.order_items_et);
        orderAddressET.setText(orderAddress);
        orderItemsET.setText(orderedItems);
        orderAddressET.setEnabled(false);
        orderItemsET.setEnabled(false);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!orderAddressET.getText().toString().isEmpty())
                    orderAddress.equals(orderAddressET.getText().toString());
                else
                    orderAddressET.setError(getString(R.string.field_required));

                if(!orderAddressET.getText().toString().isEmpty() && !orderItemsET.getText().toString().isEmpty())
                {
                    orderAddress = orderAddressET.getText().toString();
                    orderedItems = orderItemsET.getText().toString();
                    uploadOrderToFirebase(orderAddress, orderedItems);
                    dialog.dismiss();
                }
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderAddressET.setEnabled(true);
            }
        });
        cancelDialogIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Order Cancelled!", Toast.LENGTH_LONG).show();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void uploadOrderToFirebase(String orderAddress, String orderedItems) {
        String order_key = databaseReference.push().getKey();
        String all_order_key = allOrdersReference.push().getKey();
        OrderPlaced orderPlaced = new OrderPlaced(orderAddress,orderedItems,order_key,"PENDING",currentDate());
        databaseReference.child("orders_placed").child(order_key).setValue(orderPlaced).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    databaseReference.child("add_to_cart").removeValue();
                    OrderPlaced allOrderPlaced = new OrderPlaced(orderAddress,orderedItems,order_key,"PENDING",currentDate(),all_order_key,user_key);
                    allOrdersReference.child(all_order_key).setValue(allOrderPlaced).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            //do something
                        }
                    });
                    Intent intent = new Intent(getActivity(),ConfirmationActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Failed to placed order. Please try again later.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartList = new ArrayList<>();
        shared = new Shared(getContext());
        user_key = shared.getUserKeyShared();
        orderAddress = shared.getUserNameShared() + "\n" + shared.getUserPhoneShared() + "\n" + shared.getUserAddressShared();
        orderedItems = "";
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user_key);
        allOrdersReference = FirebaseDatabase.getInstance().getReference("orders");
        metrics = getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;

    }

    private void populateList() {
        orderedItems = "Ordered Items: \n\n";
        cartList.clear();
        databaseReference.child("add_to_cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren())
                {
                    Cart cartItems = ds.getValue(Cart.class);
                    total = total + cartItems.getItemTotalPrice();
                    cartList.add(cartItems);
                    int price = Integer.parseInt(cartItems.getItemPrice().substring(4,cartItems.getItemPrice().length()));
                    String total_price = "Rs. " + String.valueOf(price * cartItems.getItemQty());
                    orderedItems += "\u25CF " + cartItems.getItemName() + " - " + cartItems.getItemQty() + "*" + String.valueOf(price) + " - " + total_price
                            + "\n";
                }
                orderedItems += "\nTotal is - " + "Rs. " + String.valueOf(total);
                if(cartList.size()>0)
                {
                    placeOrderCard.setVisibility(View.VISIBLE);
                    emptyCartImage.setVisibility(View.GONE);
                }
                else
                {
                    placeOrderCard.setVisibility(View.GONE);
                    emptyCartImage.setVisibility(View.VISIBLE);
                }
                totalPriceText.setText("Total" + "\n" + "Rs. " + String.valueOf(total));
                cartRecyclerView.setAdapter(new CartAdapter(getContext(),cartList,CartFragment.this));
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public String currentDate() {
        Locale.setDefault(Locale.ENGLISH);
        DateFormat date = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());
        // you can get seconds by adding  "...:ss" to it
        Date todayDate = new Date();
        return date.format(todayDate);
    }

    public void removeItem(String item_cart_key){
        databaseReference.child("add_to_cart").child(item_cart_key).removeValue();
        Toast.makeText(getContext(),"Item removed successfully!",Toast.LENGTH_SHORT).show();
        total = 0;
        populateList();
    }

}