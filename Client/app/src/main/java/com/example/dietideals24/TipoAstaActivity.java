package com.example.dietideals24;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dietideals24.dto.UtenteDTO;
import com.example.dietideals24.enums.TipoUtente;
import com.example.dietideals24.models.Asta;
import com.example.dietideals24.models.Asta_Ribasso;
import com.example.dietideals24.models.Utente;
import com.google.firebase.analytics.FirebaseAnalytics;

public class TipoAstaActivity extends AppCompatActivity {

    private LinearLayout buttonSilenziosa;
    private LinearLayout buttonRibasso;
    private LinearLayout buttonInversa;
    private ImageButton back_button;
    private Utente utente;
    private Asta asta;
    private boolean fromHome;
    private FirebaseAnalytics mfBanalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_asta);

        mfBanalytics = FirebaseAnalytics.getInstance(this);
        logActivityEvent("TipoAstaActivity");

        buttonSilenziosa = findViewById(R.id.button_silenziosa);
        buttonRibasso = findViewById(R.id.button_ribasso);
        buttonInversa = findViewById(R.id.button_inversa);
        back_button = findViewById(R.id.back_button);
        ImageButton home_button = findViewById(R.id.home_button);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        fromHome = getIntent().getBooleanExtra("fromHome", true);
        utente = (Utente) getIntent().getSerializableExtra("utente");
        TipoUtente tipoUtente = (TipoUtente) getIntent().getSerializableExtra("tipoUtente");
        asta = (Asta) getIntent().getSerializableExtra("asta");

        if(!tipoUtente.toString().equals("COMPLETO")) {
            configuraBottoni(tipoUtente.toString(), buttonInversa, buttonRibasso, buttonSilenziosa);
        }

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityCreaAsta(asta, utente);
                finish();
            }
        });

        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityHome(utente);
                finish();
            }
        });

        buttonSilenziosa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAstaSilenziosa(asta, utente);
            }
        });


        buttonRibasso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAstaRibasso(asta, utente);
            }
        });


        buttonInversa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAstaInversa(asta, utente);
            }
        });
    }

    public void configuraBottoni(String tipoUtente, LinearLayout buttonInversa, LinearLayout buttonRibasso, LinearLayout buttonSilenziosa) {
        switch(tipoUtente) {
            case "VENDITORE":
                disabilitaBottone(buttonInversa);
                break;
            case "COMPRATORE":
                disabilitaBottone(buttonRibasso);
                disabilitaBottone(buttonSilenziosa);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openActivityCreaAsta(asta, utente);
        finish();
    }

    private void disabilitaBottone(LinearLayout button) {
        button.setEnabled(false);
        button.setAlpha(0.5f);
    }

    private void openActivityCreaAsta(Asta asta, Utente utente) {
        Intent intent = new Intent(this, CreaAstaActivity.class);
        intent.putExtra("asta", asta);
        intent.putExtra("tipoUtente", utente.getTipo());
        intent.putExtra("utente", utente);
        intent.putExtra("fromHome", fromHome);
        startActivity(intent);
    }

    public void openActivityHome(Utente utente) {
        Intent intentR = new Intent(this, HomeActivity.class);
        intentR.putExtra("utente", utente);
        startActivity(intentR);
    }

    public void openActivityAstaSilenziosa(Asta asta, Utente utente) {
        Intent intentR = new Intent(this, CreaAstaSilenziosaActivity.class);
        intentR.putExtra("asta", asta);
        intentR.putExtra("tipoUtente", utente.getTipo());
        intentR.putExtra("utente", utente);
        intentR.putExtra("fromHome", fromHome);
        startActivity(intentR);
    }

    public void openActivityAstaRibasso(Asta asta, Utente utente) {
        Intent intentR = new Intent(this, CreaAstaRibassoActivity.class);
        intentR.putExtra("asta", asta);
        intentR.putExtra("tipoUtente", utente.getTipo());
        intentR.putExtra("utente", utente);
        intentR.putExtra("fromHome", fromHome);
        startActivity(intentR);
    }

    public void openActivityAstaInversa(Asta asta, Utente utente) {
        Intent intentR = new Intent(this, CreaAstaInversaActivity.class);
        intentR.putExtra("asta", asta);
        intentR.putExtra("utente", utente);
        intentR.putExtra("fromHome", fromHome);
        startActivity(intentR);
    }

    private void logActivityEvent(String activityName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, activityName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, activityName);
        mfBanalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }
}