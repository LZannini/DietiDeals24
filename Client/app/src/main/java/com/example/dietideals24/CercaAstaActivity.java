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
import com.example.dietideals24.models.Asta;
import com.example.dietideals24.models.Asta_Inversa;
import com.example.dietideals24.models.Asta_Ribasso;
import com.example.dietideals24.models.Asta_Silenziosa;
import com.example.dietideals24.models.Utente;
import com.example.dietideals24.retrofit.RetrofitService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CercaAstaActivity extends AppCompatActivity {

    private EditText cercaAstaInput;
    private final Categoria[] items = Categoria.values();
    private AutoCompleteTextView autoCompleteTxt;
    private Utente utente;
    private boolean fromHome;
    private boolean modificaAvvenuta;
    private List<Asta> listaAste;
    private ImageButton back_button, home_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerca_asta);

        Button vaiButton = findViewById(R.id.vai_button);

        cercaAstaInput = findViewById(R.id.cerca_asta_input);

        autoCompleteTxt = findViewById(R.id.auto_complete_txt);

        ArrayAdapter<Categoria> adapterItems = new ArrayAdapter<>(this, R.layout.activity_list_item, items);

        autoCompleteTxt.setAdapter(adapterItems);

        utente = (Utente) getIntent().getSerializableExtra("utente");

        fromHome = getIntent().getBooleanExtra("fromHome", true);

        listaAste = (List<Asta>) getIntent().getSerializableExtra("listaAste");

        modificaAvvenuta = getIntent().getBooleanExtra("modificaAvvenuta", false);

        back_button = findViewById(R.id.back_button);

        home_button = findViewById(R.id.home_button);

        if(!fromHome) {
            home_button.setVisibility(View.VISIBLE);
        }

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fromHome) {
                    openActivityHome(utente);
                } else {
                    openActivityOfferteFatte();
                }
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
        if(fromHome) {
            openActivityHome(utente);
        } else {
            openActivityOfferteFatte();
        }
        finish();
    }

    private void openActivityHome(Utente utente) {
        Intent intentH = new Intent(this, HomeActivity.class);
        intentH.putExtra("utente", utente);
        startActivity(intentH);
    }

    private void openActivityOfferteFatte() {
        Intent intent = new Intent(this, OfferteFatteActivity.class);
        intent.putExtra("utente", utente);
        intent.putExtra("utente_home", utente);
        intent.putExtra("modificaAvvenuta", modificaAvvenuta);
        if(listaAste != null) {
            intent.putExtra("listaAste", (Serializable) listaAste);
        } else {
            intent.putExtra("listaAste", new ArrayList<>());
        }
        startActivity(intent);
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
                    List<AstaDTO> asteResponse = response.body();
                    List<Asta> asteList = creaListaModelloAsta(asteResponse);
                    List<Asta> aste = new ArrayList<>();
                    for (Asta a : asteList) {
                        if (a.getId_creatore() != utente.getId())
                            aste.add(a);
                    }
                    Intent intent = new Intent(CercaAstaActivity.this, RisultatiRicercaActivity.class);
                    intent.putExtra("listaAste", (Serializable) aste);
                    intent.putExtra("criterioRicerca", finalSearchCriteria);
                    intent.putExtra("utente", utente);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(CercaAstaActivity.this, RisultatiRicercaActivity.class);
                    intent.putExtra("listaAste", new ArrayList<AstaDTO>());
                    intent.putExtra("criterioRicerca", finalSearchCriteria);
                    intent.putExtra("utente", utente);
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

    public Asta_Ribasso creaModelloAstaR(AstaDTO dto) {
        Asta_Ribasso asta = new Asta_Ribasso();
        asta.setId(dto.getID());
        asta.setId_creatore(dto.getId_creatore());
        asta.setCategoria(dto.getCategoria());
        asta.setFoto(dto.getFoto());
        asta.setNome(dto.getNome());
        asta.setDescrizione(dto.getDescrizione());
        asta.setStato(dto.getStato());

        return asta;
    }

    public Asta_Silenziosa creaModelloAstaS(AstaDTO dto) {
        Asta_Silenziosa asta = new Asta_Silenziosa();
        asta.setId(dto.getID());
        asta.setId_creatore(dto.getId_creatore());
        asta.setCategoria(dto.getCategoria());
        asta.setFoto(dto.getFoto());
        asta.setNome(dto.getNome());
        asta.setDescrizione(dto.getDescrizione());
        asta.setStato(dto.getStato());

        return asta;
    }

    public Asta_Inversa creaModelloAstaI(AstaDTO dto) {
        Asta_Inversa asta = new Asta_Inversa();
        asta.setId(dto.getID());
        asta.setId_creatore(dto.getId_creatore());
        asta.setCategoria(dto.getCategoria());
        asta.setFoto(dto.getFoto());
        asta.setNome(dto.getNome());
        asta.setDescrizione(dto.getDescrizione());
        asta.setStato(dto.getStato());

        return asta;
    }

    public List<Asta> creaListaModelloAsta(List<AstaDTO> listaDto) {
        List<Asta> asteList = new ArrayList<>();
        for (AstaDTO dto : listaDto) {
            if (dto.getTipo().equals("RIBASSO") && !utente.getTipo().toString().equals("VENDITORE")) {
                asteList.add(creaModelloAstaR(dto));
            } else if (dto.getTipo().equals("SILENZIOSA") && !utente.getTipo().toString().equals("VENDITORE")) {
                asteList.add(creaModelloAstaS(dto));
            } else if (dto.getTipo().equals("INVERSA") && !utente.getTipo().toString().equals("COMPRATORE")) {
                asteList.add(creaModelloAstaI(dto));
            }
        }
        return asteList;
    }

}