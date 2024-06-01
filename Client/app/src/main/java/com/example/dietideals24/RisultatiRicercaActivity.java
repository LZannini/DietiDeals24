package com.example.dietideals24;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietideals24.dto.AstaDTO;
import com.example.dietideals24.models.Asta;

import java.util.ArrayList;
import java.util.List;

public class RisultatiRicercaActivity extends AppCompatActivity implements AuctionAdapter.OnAstaListener{

    private List<AstaDTO> listaAste;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risultati_ricerca);

        TextView noResultsText = findViewById(R.id.no_results_text);
        ImageButton back_button = findViewById(R.id.back_button);



        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        String criterioRicerca = getIntent().getStringExtra("criterioRicerca");

        TextView risultatiRicerca = findViewById(R.id.risultati_title);

        risultatiRicerca.setText("Risultati Per "+ criterioRicerca);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.risultati_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listaAste = (List<AstaDTO>) getIntent().getSerializableExtra("listaAste");
        AuctionAdapter adapter = new AuctionAdapter(listaAste,this);
        recyclerView.setAdapter(adapter);

        if(listaAste == null || listaAste.isEmpty())
            noResultsText.setVisibility(View.VISIBLE);
        else
            noResultsText.setVisibility(View.GONE);
    }

    @Override
    public void onAstaClick(int position) {
     AstaDTO asta = listaAste.get(position);

    }
}
