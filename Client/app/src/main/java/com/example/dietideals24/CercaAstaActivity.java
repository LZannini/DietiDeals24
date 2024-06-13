package com.example.dietideals24;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.dietideals24.api.ApiService;
import com.example.dietideals24.dto.AstaDTO;
import com.example.dietideals24.dto.UtenteDTO;
import com.example.dietideals24.enums.Categoria;
import com.example.dietideals24.retrofit.RetrofitService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CercaAstaActivity extends AppCompatActivity {

    private EditText cercaAstaInput;
    private final Categoria[] items = Categoria.values();
    private AutoCompleteTextView autoCompleteTxt;
    private UtenteDTO utente_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerca_asta);

        Button vaiButton = findViewById(R.id.vai_button);

        cercaAstaInput = findViewById(R.id.cerca_asta_input);

        autoCompleteTxt = findViewById(R.id.auto_complete_txt);

        ArrayAdapter<Categoria> adapterItems = new ArrayAdapter<>(this, R.layout.activity_list_item, items);

        autoCompleteTxt.setAdapter(adapterItems);

        utente_home = (UtenteDTO) getIntent().getSerializableExtra("utente");

        ImageButton back_button = findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityHome(utente_home);
                finish();
            }
        });

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Filtro Selezionato: " + item, Toast.LENGTH_SHORT).show();
            }
        });


        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);

        vaiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filtro = autoCompleteTxt.getText().toString();
                String query = cercaAstaInput.getText().toString();
                cercaAsta(apiService,filtro,query);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openActivityHome(utente_home);
        finish();
    }

    private void openActivityHome(UtenteDTO utente) {
        Intent intentH = new Intent(this, HomeActivity.class);
        intentH.putExtra("utente", utente);
        startActivity(intentH);
    }

    private void cercaAsta(ApiService apiService, String filtro, String query) {
        Call<List<AstaDTO>> call;
        String searchCriteria = null;

        if (!query.isEmpty() && !filtro.isEmpty()) {
            call = apiService.cercaPerParolaChiaveAndCategoria(query, filtro.toUpperCase());
            searchCriteria = query + " in " + filtro;
        } else if (!query.isEmpty() && filtro.isEmpty()) {
            call = apiService.cercaPerParolaChiave(query);
            searchCriteria = query;
        } else if (query.isEmpty() && !filtro.isEmpty()) {
            call = apiService.cercaPerCategoria(filtro.toUpperCase());
            searchCriteria = filtro;
        }
        else
            call = apiService.cercaTutte();

        String finalSearchCriteria = searchCriteria;
        call.enqueue(new Callback<List<AstaDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<AstaDTO>> call, @NonNull Response<List<AstaDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<AstaDTO> aste = response.body();
                    Intent intent = new Intent(CercaAstaActivity.this, RisultatiRicercaActivity.class);
                    intent.putExtra("listaAste", (Serializable) aste);
                    intent.putExtra("criterioRicerca", finalSearchCriteria);
                    intent.putExtra("utente", utente_home);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(CercaAstaActivity.this, RisultatiRicercaActivity.class);
                    intent.putExtra("listaAste", new ArrayList<AstaDTO>());
                    intent.putExtra("criterioRicerca", finalSearchCriteria);
                    intent.putExtra("utente", utente_home);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<AstaDTO>> call, @NonNull Throwable t) {
                Toast.makeText(CercaAstaActivity.this, "Errore di Connessione", Toast.LENGTH_SHORT).show();
                Logger.getLogger(CercaAstaActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
            }
        });
    }

}