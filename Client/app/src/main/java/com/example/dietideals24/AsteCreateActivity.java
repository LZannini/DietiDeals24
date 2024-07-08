package com.example.dietideals24;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietideals24.adapters.AuctionAdapter;
import com.example.dietideals24.models.Asta;
import com.example.dietideals24.models.Utente;
import com.google.android.material.button.MaterialButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AsteCreateActivity extends AppCompatActivity implements AuctionAdapter.OnAstaListener {

    private Utente utente;
    private Utente utente_home;
    private List<Asta> listaAste;
    private List<Asta> aste_attive;
    private List<Asta> aste_concluse;
    private TextView noAuctionsText, yourAuctionsText;
    private RecyclerView recyclerView;
    private ImageButton back_button;
    private MaterialButton btnAttive, btnConcluse;
    private LinearLayout layout_attributi;
    private Asta astaSelezionata;
    private Boolean fromDettagli;
    private Boolean modificaAvvenuta;
    private boolean attiva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aste_create);

        modificaAvvenuta = getIntent().getBooleanExtra("modificaAvvenuta", false);
        fromDettagli = getIntent().getBooleanExtra("fromDettagli", false);
        utente = (Utente) getIntent().getSerializableExtra("utente");
        listaAste = (List<Asta>) getIntent().getSerializableExtra("listaAste");
        utente_home = (Utente) getIntent().getSerializableExtra("utente_home");

        aste_attive = new ArrayList<>();
        aste_concluse = new ArrayList<>();

        for(Asta a : listaAste) {
            if(a.getStato().toString().equals("ATTIVA")) {
                aste_attive.add(a);
            } else {
                aste_concluse.add(a);
            }
        }

        noAuctionsText = findViewById(R.id.no_auctions_text);
        back_button = findViewById(R.id.back_button);
        btnAttive = findViewById(R.id.btn_aste_attive);
        btnConcluse = findViewById(R.id.btn_aste_concluse);
        layout_attributi = findViewById(R.id.layout_attributi);
        recyclerView = findViewById(R.id.risultati_recycler_view);
        ImageButton home_button = findViewById(R.id.home_button);

        attiva = true;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AuctionAdapter adapter = new AuctionAdapter(aste_attive,this, true);
        recyclerView.setAdapter(adapter);

        btnAttive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attiva = true;
                btnAttive.setBackgroundColor(Color.parseColor("#FF0000"));
                btnConcluse.setBackgroundColor(Color.parseColor("#0E4273"));
                adapter.setAste(aste_attive, attiva);
                adapter.notifyDataSetChanged();
            }
        });

        btnConcluse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attiva = false;
                noAuctionsText.setVisibility(View.GONE);
                btnConcluse.setBackgroundColor(Color.parseColor("#FF0000"));
                btnAttive.setBackgroundColor(Color.parseColor("#0E4273"));
                adapter.setAste(aste_concluse, attiva);
                adapter.notifyDataSetChanged();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityProfilo();
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

        if(attiva && (aste_attive == null || aste_attive.isEmpty())) {
            noAuctionsText.setVisibility(View.VISIBLE);
            int childCount = layout_attributi.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = layout_attributi.getChildAt(i);
                child.setVisibility(View.INVISIBLE);
            }
        }else {
            noAuctionsText.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openActivityProfilo();
        finish();
    }

    public void openActivityHome(Utente utente) {
        Intent intentR = new Intent(this, HomeActivity.class);
        intentR.putExtra("utente", utente_home);
        startActivity(intentR);
    }

    private void openActivityProfilo() {
        Intent intentP = new Intent(this, ProfiloActivity.class);
        intentP.putExtra("utente", utente);
        intentP.putExtra("utente_home", utente_home);
        intentP.putExtra("fromDettagli", fromDettagli);
        intentP.putExtra("modificaAvvenuta", modificaAvvenuta);
        intentP.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intentP);
    }

    private void openActivityDettagliAsta() {
        Intent intent = new Intent(this, DettagliAstaActivity.class);
        intent.putExtra("listaAste", (Serializable) listaAste);
        intent.putExtra("utenteCreatore", utente);
        intent.putExtra("utente", utente_home);
        intent.putExtra("utenteProfilo", utente_home);
        intent.putExtra("asta", astaSelezionata);
        intent.putExtra("fromAsteCreate", true);
        intent.putExtra("fromDettagli", fromDettagli);
        intent.putExtra("modificaAvvenuta", modificaAvvenuta);
        startActivity(intent);
    }


    @Override
    public void onAstaClick(int position, boolean isAttive) {
        if (isAttive) {
            astaSelezionata = aste_attive.get(position);
            openActivityDettagliAsta();
        }
    }
}

