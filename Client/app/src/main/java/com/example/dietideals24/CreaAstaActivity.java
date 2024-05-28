package com.example.dietideals24;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class CreaAstaActivity extends AppCompatActivity {

    private LinearLayout buttonSilenziosa;
    private LinearLayout buttonRibasso;
    private LinearLayout buttonInversa;
    private ImageButton back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_asta);

        buttonSilenziosa = findViewById(R.id.button_silenziosa);
        buttonRibasso = findViewById(R.id.button_ribasso);
        buttonInversa = findViewById(R.id.button_inversa);
        back_button = findViewById(R.id.back_button);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonSilenziosa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAstaSilenziosa();
            }
        });


        buttonRibasso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAstaRibasso();
            }
        });


        buttonInversa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAstaInversa();
            }
        });
    }

    public void openActivityAstaSilenziosa() {
        Intent intentR = new Intent(this, CreaAstaActivity.class);
        startActivity(intentR);
    }

    public void openActivityAstaRibasso() {
        Intent intentR = new Intent(this, CreaAstaActivity.class);
        startActivity(intentR);
    }

    public void openActivityAstaInversa() {
        Intent intentR = new Intent(this, CreaAstaActivity.class);
        startActivity(intentR);
    }
}