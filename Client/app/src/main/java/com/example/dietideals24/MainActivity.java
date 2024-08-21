package com.example.dietideals24;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.dietideals24.retrofit.RetrofitService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                View splashScreen = findViewById(R.id.screenStart);
                splashScreen.animate()
                        .alpha(0f)
                        .setDuration(1000)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
            }
        }, 1500);
    }

    @Override
    public void onBackPressed() {
        //
    }
}