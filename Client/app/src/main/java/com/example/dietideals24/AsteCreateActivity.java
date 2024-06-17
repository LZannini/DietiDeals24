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

    private UtenteDTO utente_home;
    private String nomeCreatore;
    private Utente utente;
    private List<AstaDTO> listaAste;
    private TextView noAuctionsText, yourAuctionsText;
    private RecyclerView recyclerView;
    private ImageButton back_button;
    private LinearLayout layout_attributi;
    private AstaDTO astaSelezionata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aste_create);

        utente = (Utente) getIntent().getSerializableExtra("utente");
        utente_home = (UtenteDTO) getIntent().getSerializableExtra("utente_home");
        listaAste = (List<AstaDTO>) getIntent().getSerializableExtra("listaAste");

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
        }else
            noAuctionsText.setVisibility(View.GONE);

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
        startActivity(intentP);
    }

    private void openActivityDettagliAsta() {
        Intent intent = new Intent(this, DettagliAstaActivity.class);
        intent.putExtra("listaAste", (Serializable) listaAste);
        intent.putExtra("utente", utente_home);
        intent.putExtra("utenteProfilo", utente);
        intent.putExtra("asta", astaSelezionata);
        intent.putExtra("fromAsteCreate", true);
        intent.putExtra("username", nomeCreatore);
        startActivity(intent);
    }

    @Override
    public void onAstaClick(int position) {
        astaSelezionata = listaAste.get(position);
        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);
        recuperaUtenteById(apiService);
    }

    private void recuperaUtenteById(ApiService apiService) {
        Call<UtenteDTO> call;
        call = apiService.recuperaUtente(astaSelezionata.getId_creatore());
        call.enqueue(new Callback<UtenteDTO>() {
            @Override
            public void onResponse(Call<UtenteDTO> call, Response<UtenteDTO> response) {
                UtenteDTO user = response.body();
                if (user != null) {
                    nomeCreatore = user.getUsername();
                    openActivityDettagliAsta();
                    finish();
                } else {
                    Toast.makeText(AsteCreateActivity.this, "Utente non trovato", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UtenteDTO> call, Throwable t) {
                Toast.makeText(AsteCreateActivity.this, "Errore di Connessione", Toast.LENGTH_SHORT).show();
                Logger.getLogger(AsteCreateActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
            }
        });
    }
}

