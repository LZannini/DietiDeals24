package com.example.dietideals24;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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
    private ImageButton back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risultati_ricerca);

        listView = findViewById(R.id.risultati_list_view);
        noResultsText = findViewById(R.id.no_results_text);
        back_button = findViewById(R.id.back_button);

        String criterioRicerca = getIntent().getStringExtra("criterioRicerca");

        TextView risultatiRicerca = findViewById(R.id.risultati_title);

        risultatiRicerca.setText("Risultati Per "+ criterioRicerca);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listaAste = (List<AstaDTO>)getIntent().getSerializableExtra("listaAste");

        if(listaAste == null || listaAste.isEmpty())
            noResultsText.setVisibility(View.VISIBLE);
        else{
            noResultsText.setVisibility(View.GONE);
            List<String> nomiAste = new ArrayList<>();
            for(AstaDTO asta : listaAste){
                nomiAste.add(asta.getNome() + " - " + asta.getCategoria().toString() + " - " + asta.getDescrizione());
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,nomiAste);
            listView.setAdapter(adapter);
        }
    }
}
