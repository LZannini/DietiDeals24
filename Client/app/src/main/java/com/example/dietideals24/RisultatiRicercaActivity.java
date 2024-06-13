package com.example.dietideals24;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietideals24.dto.AstaDTO;
import com.example.dietideals24.dto.UtenteDTO;
import com.example.dietideals24.models.Asta;

import java.util.List;

public class RisultatiRicercaActivity extends AppCompatActivity implements AuctionAdapter.OnAstaListener{

    private AstaDTO astaSelezionata;
    private UtenteDTO utente_home;
    private String criterioRicerca;

    private List<AstaDTO> listaAste;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risultati_ricerca);

        TextView noResultsText = findViewById(R.id.no_results_text);
        ImageButton back_button = findViewById(R.id.back_button);

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

        listaAste = (List<AstaDTO>) getIntent().getSerializableExtra("listaAste");
        AuctionAdapter adapter = new AuctionAdapter(listaAste,this);
        recyclerView.setAdapter(adapter);

        if(listaAste == null || listaAste.isEmpty()) {
            noResultsText.setVisibility(View.VISIBLE);
            risultatiRicerca.setText(" ");
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
        intent.putExtra("asta", astaSelezionata);
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
        openActivityDettagliAsta();
        finish();
    }
}
