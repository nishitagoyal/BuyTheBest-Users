package com.something.groceryapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.something.groceryapp.R;

public class ConfirmationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_success_layout); //activity_confirmation
    }
}