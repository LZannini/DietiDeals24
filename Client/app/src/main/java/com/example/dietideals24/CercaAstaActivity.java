package com.example.dietideals24;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dietideals24.api.ApiService;
import com.example.dietideals24.dto.AstaDTO;
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
    private Button vaiButton;
    String[] items = {"Nome", "Categoria"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerca_asta);

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
        vaiButton = findViewById(R.id.vai_button);

        cercaAstaInput = findViewById(R.id.cerca_asta_input);

        autoCompleteTxt = findViewById(R.id.auto_complete_txt);

        adapterItems = new ArrayAdapter<String>(this, R.layout.activity_list_item,items);

        autoCompleteTxt.setAdapter(adapterItems);

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

                if(query.isEmpty())
                    Toast.makeText(CercaAstaActivity.this,"Inserisci un termine di ricerca",Toast.LENGTH_SHORT).show();
                else
                    cercaAsta(apiService,filtro,query);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

    }

    private void cercaAsta(ApiService apiService,String filtro, String query) {
    Call<List<AstaDTO>> call;
        if (filtro.isEmpty() || filtro.equals("Nome"))
            call = apiService.cercaPerParolaChiave(query);
         else if (filtro.equals("Categoria"))
            call = apiService.cercaPerCategoria(query); // Assumendo che query sia il nome della categoria
         else
            call = apiService.cercaPerParolaChiaveAndCategoria(query, filtro); // Assumendo che filtro sia la categoria


        call.enqueue(new Callback<List<AstaDTO>>() {
            @Override
            public void onResponse(Call<List<AstaDTO>> call, Response<List<AstaDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<AstaDTO> aste = response.body();
                    Intent intent = new Intent(CercaAstaActivity.this, RisultatiRicercaActivity.class);
                    intent.putExtra("listaAste", (Serializable) aste);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(CercaAstaActivity.this, RisultatiRicercaActivity.class);
                    intent.putExtra("listaAste", new ArrayList<AstaDTO>());
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<List<AstaDTO>> call, Throwable t) {
                Toast.makeText(CercaAstaActivity.this, "Errore nella chiamata di rete", Toast.LENGTH_SHORT).show();
                Logger.getLogger(CercaAstaActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
            }
        });
=======
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
>>>>>>> b93a37852ba927f9049e7a3707ca702d6aa6d91a
=======
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
>>>>>>> b93a37852ba927f9049e7a3707ca702d6aa6d91a
=======
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
>>>>>>> b93a37852ba927f9049e7a3707ca702d6aa6d91a
    }
}