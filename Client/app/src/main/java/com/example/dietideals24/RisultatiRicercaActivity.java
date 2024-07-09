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

import com.example.dietideals24.adapters.AuctionAdapter;
import com.example.dietideals24.api.ApiService;
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

public class RisultatiRicercaActivity extends AppCompatActivity implements AuctionAdapter.OnAstaListener {

    private Asta astaSelezionata;
    private Utente utenteCreatore;
    private Utente utente;
    private String criterioRicerca;
    private List<Asta> listaAste;
    private LinearLayout layout_attributi;
    private boolean fromHome;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risultati_ricerca);

        TextView noResultsText = findViewById(R.id.no_results_text);
        ImageButton back_button = findViewById(R.id.back_button);
        layout_attributi = findViewById(R.id.layout_attributi);
        ImageButton home_button = findViewById(R.id.home_button);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        fromHome = getIntent().getBooleanExtra("fromHome", true);
        utente = (Utente) getIntent().getSerializableExtra("utente");
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

        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityHome(utente);
                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.risultati_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listaAste = (List<Asta>) getIntent().getSerializableExtra("listaAste");
        AuctionAdapter adapter = new AuctionAdapter(listaAste,this, true, false);
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
        intent.putExtra("utente", utente);
        intent.putExtra("asta", astaSelezionata);
        intent.putExtra("fromAsteCreate", false);
        intent.putExtra("utenteCreatore", utenteCreatore);
        intent.putExtra("fromHome", fromHome);
        startActivity(intent);
    }

    public void openActivityHome(Utente utente) {
        Intent intentR = new Intent(this, HomeActivity.class);
        intentR.putExtra("utente", utente);
        startActivity(intentR);
    }

    private void openActivityCercaAsta() {
        Intent intent = new Intent(this, CercaAstaActivity.class);
        intent.putExtra("utente", utente);
        intent.putExtra("fromHome", fromHome);
        startActivity(intent);
    }
    @Override
    public void onAstaClick(int position, boolean isAttive) {
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
                    utenteCreatore = creaCreatoreAsta(user);
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

    public Utente creaCreatoreAsta(UtenteDTO utenteDTO) {
        Utente u = new Utente();
        u.setId(utenteDTO.getId());
        u.setUsername(utenteDTO.getUsername());
        u.setEmail(utenteDTO.getEmail());
        u.setPassword(utenteDTO.getPassword());
        u.setTipo(utenteDTO.getTipo());
        if (utenteDTO.getAvatar() != null) u.setAvatar(utenteDTO.getAvatar());
        if (utenteDTO.getBiografia() != null) u.setBiografia(utenteDTO.getBiografia());
        if (utenteDTO.getPaese() != null) u.setPaese(utenteDTO.getPaese());
        if (utenteDTO.getSitoweb() != null) u.setSitoweb(utenteDTO.getSitoweb());
        return u;
    }
}
