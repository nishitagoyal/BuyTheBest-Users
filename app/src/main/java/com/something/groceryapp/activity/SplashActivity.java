package com.something.groceryapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.something.groceryapp.R;
import com.something.groceryapp.model.Shared;

public class SplashActivity extends AppCompatActivity {

    private Handler mWaitHandler = new Handler();
    Shared shared;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        shared = new Shared(getApplicationContext());
        if(shared.isFirstTimeLaunched())
        {
            mWaitHandler.postDelayed(new Runnable() {
                @Override
                public void run() {         //Go to next page i.e, start the main activity.
                    try {
//                        shared.setFirstTimeLaunched(false);
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } catch (Exception ignored) {
                        ignored.printStackTrace(); } }
            }, 3000);  // Give a 3 seconds delay.

        }
        else
        {
            mWaitHandler.postDelayed(new Runnable() {
                @Override
                public void run() {         //Go to next page i.e, start the main activity.
                    try {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } catch (Exception ignored) {
                        ignored.printStackTrace(); } }
            }, 3000);  // Give a 3 seconds delay.

        }
}
}