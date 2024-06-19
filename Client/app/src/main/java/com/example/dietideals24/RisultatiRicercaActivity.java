package com.example.dietideals24;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.example.dietideals24.retrofit.RetrofitService;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RisultatiRicercaActivity extends AppCompatActivity implements AuctionAdapter.OnAstaListener {

    private Asta astaSelezionata;
    private String nomeCreatore;
    private UtenteDTO utente_home;
    private String criterioRicerca;
    private List<Asta> listaAste;
    private LinearLayout layout_attributi;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risultati_ricerca);

        TextView noResultsText = findViewById(R.id.no_results_text);
        ImageButton back_button = findViewById(R.id.back_button);
        layout_attributi = findViewById(R.id.layout_attributi);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        utente_home = (UtenteDTO) getIntent().getSerializableExtra("utente");
        if (getIntent().getStringExtra("criterioRicerca") != null)
            criterioRicerca = getIntent().getStringExtra("criterioRicerca");
        TextView risultatiRicerca = findViewById(R.id.risultati_title);

        if (criterioRicerca == null)
            risultatiRicerca.setText("Risultati");
        else
            risultatiRicerca.setText("Risultati per "+ criterioRicerca);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityCercaAsta();
                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.risultati_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listaAste = (List<Asta>) getIntent().getSerializableExtra("listaAste");
        AuctionAdapter adapter = new AuctionAdapter(listaAste,this);
        recyclerView.setAdapter(adapter);

        if(listaAste == null || listaAste.isEmpty()) {
            noResultsText.setVisibility(View.VISIBLE);
            risultatiRicerca.setText(" ");
            int childCount = layout_attributi.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = layout_attributi.getChildAt(i);
                child.setVisibility(View.INVISIBLE);
            }
        }else
            noResultsText.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openActivityCercaAsta();
        finish();
    }

    private void openActivityDettagliAsta() {
        Intent intent = new Intent(this, DettagliAstaActivity.class);
        intent.putExtra("listaAste", (Serializable) listaAste);
        intent.putExtra("criterioRicerca", criterioRicerca);
        intent.putExtra("utente", utente_home);
        intent.putExtra("asta", astaSelezionata);
        intent.putExtra("fromAsteCreate", false);
        intent.putExtra("username", nomeCreatore);
        startActivity(intent);
    }

    private void openActivityCercaAsta() {
        Intent intent = new Intent(this, CercaAstaActivity.class);
        intent.putExtra("utente", utente_home);
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
                    Toast.makeText(RisultatiRicercaActivity.this, "Utente non trovato", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UtenteDTO> call, Throwable t) {
                Toast.makeText(RisultatiRicercaActivity.this, "Errore di Connessione", Toast.LENGTH_SHORT).show();
                Logger.getLogger(RisultatiRicercaActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
            }
        });
    }
}
