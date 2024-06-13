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

public class TipoAstaActivity extends AppCompatActivity {

    private LinearLayout buttonSilenziosa;
    private LinearLayout buttonRibasso;
    private LinearLayout buttonInversa;
    private ImageButton back_button;
    private Utente utente_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_asta);

        buttonSilenziosa = findViewById(R.id.button_silenziosa);
        buttonRibasso = findViewById(R.id.button_ribasso);
        buttonInversa = findViewById(R.id.button_inversa);
        back_button = findViewById(R.id.back_button);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        utente_intent = (Utente) getIntent().getSerializableExtra("utente");
        UtenteDTO utente = creaUtenteDTO(utente_intent);
        TipoUtente tipoUtente = (TipoUtente) getIntent().getSerializableExtra("tipoUtente");
        Asta asta = (Asta) getIntent().getSerializableExtra("asta");

        if(!tipoUtente.toString().equals("COMPLETO")) {
            configuraBottoni(tipoUtente.toString(), buttonInversa, buttonRibasso, buttonSilenziosa);
        }

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityCreaAsta(utente_intent);
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

    private void configuraBottoni(String tipoUtente, LinearLayout buttonInversa, LinearLayout buttonRibasso, LinearLayout buttonSilenziosa) {
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
        openActivityCreaAsta(utente_intent);
        finish();
    }

    private void disabilitaBottone(LinearLayout button) {
        button.setEnabled(false);
        button.setAlpha(0.5f);
    }

    private void openActivityCreaAsta(Utente utente) {
        Intent intent = new Intent(this, CreaAstaActivity.class);
        intent.putExtra("utente", utente);
        startActivity(intent);
    }

    public void openActivityAstaSilenziosa(Asta asta, UtenteDTO utente) {
        Intent intentR = new Intent(this, CreaAstaSilenziosaActivity.class);
        intentR.putExtra("asta", asta);
        intentR.putExtra("utente", utente);
        startActivity(intentR);
    }

    public void openActivityAstaRibasso(Asta asta, UtenteDTO utente) {
        Intent intentR = new Intent(this, CreaAstaRibassoActivity.class);
        intentR.putExtra("asta", asta);
        intentR.putExtra("utente", utente);
        startActivity(intentR);
    }

    public void openActivityAstaInversa(Asta asta, UtenteDTO utente) {
        Intent intentR = new Intent(this, CreaAstaInversaActivity.class);
        intentR.putExtra("asta", asta);
        intentR.putExtra("utente", utente);
        startActivity(intentR);
    }

    private UtenteDTO creaUtenteDTO(Utente u) {
        UtenteDTO utente = new UtenteDTO();
        utente.setId(u.getId());
        utente.setUsername(u.getUsername());
        utente.setEmail(u.getEmail());
        utente.setPassword(u.getPassword());
        utente.setBiografia(u.getBiografia());
        utente.setSitoweb(u.getSitoweb());
        utente.setPaese(u.getPaese());
        utente.setTipo(u.getTipo());
        utente.setAvatar(u.getAvatar());
        return utente;
    }
}