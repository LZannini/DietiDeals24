package com.example.dietideals24;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dietideals24.dto.AstaDTO;

import java.util.ArrayList;
import java.util.List;

public class RisultatiRicercaActivity extends AppCompatActivity {
    private ListView listView;
    private TextView noResultsText;
    private ArrayAdapter<String> adapter;
    private List<AstaDTO> listaAste;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risultati_ricerca);

        listView = findViewById(R.id.risultati_list_view);
        noResultsText = findViewById(R.id.no_results_text);

        listaAste = (List<AstaDTO>)getIntent().getSerializableExtra("listaAste");

        if(listaAste == null || listaAste.isEmpty())
            noResultsText.setVisibility(View.VISIBLE);
        else{
            noResultsText.setVisibility(View.GONE);
            List<String> nomiAste = new ArrayList<>();
            for(AstaDTO asta : listaAste){
                nomiAste.add(asta.getNome() + " - " + asta.getCategoria().toString());
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,nomiAste);
            listView.setAdapter(adapter);
        }
    }
}
