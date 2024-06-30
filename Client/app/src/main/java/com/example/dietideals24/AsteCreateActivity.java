package com.example.dietideals24;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietideals24.api.ApiService;
import com.example.dietideals24.dto.AstaDTO;
import com.example.dietideals24.dto.UtenteDTO;
import com.example.dietideals24.models.Asta;
import com.example.dietideals24.models.Utente;
import com.example.dietideals24.retrofit.RetrofitService;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AsteCreateActivity extends AppCompatActivity implements AuctionAdapter.OnAstaListener {

    private Utente utenteCreatore;
    private Utente utente;
    private List<Asta> listaAste;
    private TextView noAuctionsText, yourAuctionsText;
    private RecyclerView recyclerView;
    private ImageButton back_button;
    private LinearLayout layout_attributi;
    private Asta astaSelezionata;
    private Boolean fromDettagli;
    private Boolean modificaAvvenuta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aste_create);

        modificaAvvenuta = getIntent().getBooleanExtra("modificaAvvenuta", false);
        fromDettagli = getIntent().getBooleanExtra("fromDettagli", false);
        utenteCreatore = (Utente) getIntent().getSerializableExtra("utenteCreatore");
        utente = (Utente) getIntent().getSerializableExtra("utente");
        listaAste = (List<Asta>) getIntent().getSerializableExtra("listaAste");

        noAuctionsText = findViewById(R.id.no_auctions_text);
        back_button = findViewById(R.id.back_button);
        layout_attributi = findViewById(R.id.layout_attributi);
        yourAuctionsText = findViewById(R.id.your_auctions_title);
        recyclerView = findViewById(R.id.risultati_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AuctionAdapter adapter = new AuctionAdapter(listaAste,this);
        recyclerView.setAdapter(adapter);

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

        if(listaAste == null || listaAste.isEmpty()) {
            noAuctionsText.setVisibility(View.VISIBLE);
            yourAuctionsText.setText(" ");
            int childCount = layout_attributi.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = layout_attributi.getChildAt(i);
                child.setVisibility(View.INVISIBLE);
            }
        }else {
            noAuctionsText.setVisibility(View.GONE);
            if (!fromDettagli)
                yourAuctionsText.setText("Le tue aste");
            else
                yourAuctionsText.setText("Aste di " + utente.getUsername());
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openActivityProfilo();
        finish();
    }

    private void openActivityProfilo() {
        Intent intentP = new Intent(this, ProfiloActivity.class);
        intentP.putExtra("utente", utente);
        intentP.putExtra("fromDettagli", fromDettagli);
        intentP.putExtra("modificaAvvenuta", modificaAvvenuta);
        startActivity(intentP);
    }

    private void openActivityDettagliAsta() {
        Intent intent = new Intent(this, DettagliAstaActivity.class);
        intent.putExtra("listaAste", (Serializable) listaAste);
        intent.putExtra("utente", utente);
        intent.putExtra("utenteProfilo", utente);
        intent.putExtra("asta", astaSelezionata);
        intent.putExtra("fromAsteCreate", true);
        intent.putExtra("fromDettagli", fromDettagli);
        intent.putExtra("utenteCreatore", utenteCreatore);
        intent.putExtra("modificaAvvenuta", modificaAvvenuta);
        startActivity(intent);
    }

    @Override
    public void onAstaClick(int position) {
        astaSelezionata = listaAste.get(position);
        openActivityDettagliAsta();
    }
}

