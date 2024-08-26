package com.example.dietideals24;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dietideals24.adapters.AuctionAdapter;
import com.example.dietideals24.api.ApiService;
import com.example.dietideals24.dto.OffertaDTO;
import com.example.dietideals24.enums.StatoOfferta;
import com.example.dietideals24.models.Asta;
import com.example.dietideals24.models.Offerta;
import com.example.dietideals24.models.Utente;
import com.example.dietideals24.retrofit.RetrofitService;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferteFatteActivity extends AppCompatActivity implements AuctionAdapter.OnAstaListener {

    private Utente utente;
    private List<Asta> listaAste;
    private List<Asta> aste_attive, aste_vinte, aste_rifiutate, aste_perse;
    private TextView noAuctionsText;
    private RecyclerView recyclerView;
    private ImageButton back_button;
    private MaterialButton btnAttive, btnVinte, btnRifiutate, btnPerse;
    private Button btnCrea;
    private FirebaseAnalytics mfBanalytics;
    private LinearLayout layout_attributi;
    private Asta astaSelezionata;
    private Boolean fromDettagli;
    private Boolean modificaAvvenuta;
    private boolean attiva, rifiutata;
    List<Offerta> offerte = new ArrayList<>();
    AuctionAdapter adapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offerte_fatte);

        mfBanalytics = FirebaseAnalytics.getInstance(this);
        logActivityEvent("OfferteFatteActivity");

        utente = (Utente) getIntent().getSerializableExtra("utente_home");
        listaAste = (List<Asta>) getIntent().getSerializableExtra("listaAste");
        modificaAvvenuta = getIntent().getBooleanExtra("modificaAvvenuta", false);


        aste_attive = new ArrayList<>();
        aste_vinte = new ArrayList<>();
        aste_rifiutate = new ArrayList<>();
        aste_perse = new ArrayList<>();
        attiva = true;
        rifiutata = false;


        ApiService apiService = RetrofitService.getRetrofit(this).create(ApiService.class);


        apiService.recuperaOffertePerUtente(utente.getId())
                .enqueue(new Callback<List<OffertaDTO>>() {
                    @Override
                    public void onResponse(Call<List<OffertaDTO>> call, Response<List<OffertaDTO>> response) {
                        if(response.isSuccessful() && response.body() != null) {
                            List<OffertaDTO> offerteResponse = response.body();
                            List<Offerta> offerteList = creaListModelloOfferta(offerteResponse);
                            for(Offerta o : offerteList) {
                                offerte.add(o);
                            }

                            for(Asta a : listaAste) {
                                if(a.getStato().toString().equals("ATTIVA")) {
                                    for(Offerta o : offerte) {
                                        if(o.getId_asta() == a.getId()) {
                                            if(o.getStato().toString().equals("ATTESA")) {
                                                aste_attive.add(a);
                                            } else if(o.getStato().toString().equals("RIFIUTATA")) {
                                                aste_rifiutate.add(a);
                                            } else {
                                                aste_perse.add(a);
                                            }
                                            break;
                                        }
                                    }
                                } else if(a.getStato().toString().equals("VENDUTA") && utente.getId() == a.getVincitore()) {
                                    aste_vinte.add(a);
                                } else {
                                    aste_perse.add(a);
                                }
                            }
                        }
                        if(attiva && (aste_attive == null || aste_attive.isEmpty())) {
                            noAuctionsText.setVisibility(View.VISIBLE);
                            btnCrea.setVisibility(View.VISIBLE);
                            int childCount = layout_attributi.getChildCount();
                            for (int i = 0; i < childCount; i++) {
                                View child = layout_attributi.getChildAt(i);
                                child.setVisibility(View.INVISIBLE);
                            }
                        }else {
                            noAuctionsText.setVisibility(View.GONE);
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(OfferteFatteActivity.this));
                        adapter = new AuctionAdapter(aste_attive,OfferteFatteActivity.this, true, false);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<List<OffertaDTO>> call, Throwable t) {
                        Toast.makeText(OfferteFatteActivity.this, "Errore di Connessione", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(OfferteFatteActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
                    }
                });

        noAuctionsText = findViewById(R.id.no_auctions_text);
        back_button = findViewById(R.id.back_button);
        btnCrea = findViewById(R.id.cerca_button);
        btnAttive = findViewById(R.id.btn_aste_attive);
        btnVinte = findViewById(R.id.btn_aste_vinte);
        btnRifiutate = findViewById(R.id.btn_aste_rifiutate);
        btnPerse = findViewById(R.id.btn_aste_perse);
        layout_attributi = findViewById(R.id.layout_attributi);
        recyclerView = findViewById(R.id.risultati_recycler_view);
        ImageButton home_button = findViewById(R.id.home_button);

        btnAttive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attiva = true;
                rifiutata = false;
                btnAttive.setBackgroundColor(Color.parseColor("#FF0000"));
                btnVinte.setBackgroundColor(Color.parseColor("#0E4273"));
                btnRifiutate.setBackgroundColor(Color.parseColor("#0E5273"));;
                btnPerse.setBackgroundColor(Color.parseColor("#0E4273"));
                adapter.setAste(aste_attive, attiva);
                adapter.notifyDataSetChanged();
                if(aste_attive == null || aste_attive.isEmpty()) {
                    noAuctionsText.setVisibility(View.VISIBLE);
                    btnCrea.setVisibility(View.VISIBLE);
                    int childCount = layout_attributi.getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = layout_attributi.getChildAt(i);
                        child.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        btnVinte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attiva = false;
                rifiutata = false;
                noAuctionsText.setVisibility(View.GONE);
                btnCrea.setVisibility(View.GONE);
                int childCount = layout_attributi.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = layout_attributi.getChildAt(i);
                    child.setVisibility(View.VISIBLE);
                }
                btnVinte.setBackgroundColor(Color.parseColor("#FF0000"));
                btnAttive.setBackgroundColor(Color.parseColor("#0E4273"));
                btnRifiutate.setBackgroundColor(Color.parseColor("#0E5273"));;
                btnPerse.setBackgroundColor(Color.parseColor("#0E4273"));
                adapter.setAste(aste_vinte, attiva);
                adapter.notifyDataSetChanged();
            }
        });

        btnRifiutate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attiva = false;
                rifiutata = true;
                noAuctionsText.setVisibility(View.GONE);
                btnCrea.setVisibility(View.GONE);
                int childCount = layout_attributi.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = layout_attributi.getChildAt(i);
                    child.setVisibility(View.VISIBLE);
                }
                btnRifiutate.setBackgroundColor(Color.parseColor("#FF0000"));
                btnAttive.setBackgroundColor(Color.parseColor("#0E4273"));
                btnVinte.setBackgroundColor(Color.parseColor("#0E5273"));;
                btnPerse.setBackgroundColor(Color.parseColor("#0E4273"));
                adapter.setAste(aste_rifiutate, rifiutata);
                adapter.notifyDataSetChanged();
            }
        });

        btnPerse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attiva = false;
                rifiutata = false;
                noAuctionsText.setVisibility(View.GONE);
                btnCrea.setVisibility(View.GONE);
                int childCount = layout_attributi.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = layout_attributi.getChildAt(i);
                    child.setVisibility(View.VISIBLE);
                }
                btnPerse.setBackgroundColor(Color.parseColor("#FF0000"));
                btnAttive.setBackgroundColor(Color.parseColor("#0E4273"));
                btnRifiutate.setBackgroundColor(Color.parseColor("#0E5273"));;
                btnVinte.setBackgroundColor(Color.parseColor("#0E4273"));
                adapter.setAste(aste_perse, attiva);
                adapter.notifyDataSetChanged();
            }
        });

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

        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityHome(utente);
                finish();
            }
        });

        btnCrea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityCercaAsta();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openActivityProfilo();
        finish();
    }

    public void openActivityHome(Utente utente) {
        Intent intentR = new Intent(this, HomeActivity.class);
        intentR.putExtra("utente", utente);
        startActivity(intentR);
    }

    private void openActivityProfilo() {
        Intent intentP = new Intent(this, ProfiloActivity.class);
        intentP.putExtra("utente", utente);
        intentP.putExtra("utente_home", utente);
        intentP.putExtra("fromDettagli", fromDettagli);
        intentP.putExtra("modificaAvvenuta", modificaAvvenuta);
        intentP.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intentP);
    }

    private void openActivityCercaAsta() {
        Intent intentR = new Intent(this, CercaAstaActivity.class);
        intentR.putExtra("utente", utente);
        intentR.putExtra("fromHome", false);
        intentR.putExtra("listaAste", (Serializable) listaAste);
        intentR.putExtra("modificaAvvenuta", modificaAvvenuta);
        startActivity(intentR);
    }

    public List<Offerta> creaListModelloOfferta(List<OffertaDTO> listaDto) {
        List<Offerta> offerteList = new ArrayList<>();
        for (OffertaDTO dto : listaDto) {
            Offerta offerta = new Offerta();
            offerta.setId(dto.getId());
            offerta.setId_asta(dto.getId_asta());
            offerta.setData(dto.getData());
            offerta.setId_utente(dto.getId_utente());
            offerta.setValore(dto.getValore());
            offerta.setOfferente(dto.getOfferente());
            offerta.setStato(dto.getStato());
            offerteList.add(offerta);
        }
        return offerteList;
    }

    @Override
    public void onAstaClick(int position, boolean isAttive) {
        if (isAttive) {
            if(rifiutata) {
                astaSelezionata = aste_rifiutate.get(position);
                aste_rifiutate.remove(position);
            } else {
                astaSelezionata = aste_attive.get(position);
            }
            showNewOfferDialog(astaSelezionata);
        }
    }

    private void showNewOfferDialog(Asta asta) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_nuova_offerta, null);
        builder.setView(dialogView);

        EditText editTextOfferAmount = dialogView.findViewById(R.id.editTextOfferAmount);
        Button buttonSubmitOffer = dialogView.findViewById(R.id.buttonSubmitOffer);
        Button buttonCancelOffer = dialogView.findViewById(R.id.buttonCancelOffer);

        AlertDialog dialog = builder.create();

        buttonSubmitOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String importoStr = editTextOfferAmount.getText().toString();
                if (!importoStr.isEmpty()) {
                    float importo = Float.parseFloat(importoStr);
                    submitNewOffer(asta, importo);
                    dialog.dismiss();
                } else {
                    Toast.makeText(OfferteFatteActivity.this, "Inserisci un importo valido", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonCancelOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                aste_rifiutate.add(asta);
            }
        });

        dialog.show();
    }


    private void submitNewOffer(Asta asta, float importo) {
        ApiService apiService = RetrofitService.getRetrofit(this).create(ApiService.class);

        OffertaDTO offerta = new OffertaDTO();
        offerta.setId_asta(asta.getId());
        offerta.setId_utente(utente.getId());
        offerta.setValore(importo);
        offerta.setStato(StatoOfferta.ATTESA);

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateTimeString = currentDateTime.format(formatter);

        offerta.setData(dateTimeString);

        apiService.creaOfferta(offerta)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(OfferteFatteActivity.this, "Offerta presentata con successo!", Toast.LENGTH_SHORT).show();
                        if(rifiutata) {
                            aste_attive.add(asta);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(OfferteFatteActivity.this, "Errore durante la creazione dell'offerta!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void logActivityEvent(String activityName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, activityName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, activityName);
        mfBanalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }
}