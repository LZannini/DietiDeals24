package com.example.dietideals24;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dietideals24.api.ApiService;
import com.example.dietideals24.dto.Asta_RibassoDTO;
import com.example.dietideals24.dto.UtenteDTO;
import com.example.dietideals24.models.Asta;
import com.example.dietideals24.models.Utente;
import com.example.dietideals24.retrofit.RetrofitService;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreaAstaRibassoActivity extends AppCompatActivity {

    private EditText prezzoIniziale;
    private ImageButton decrPrezzoIniziale;
    private ImageButton incrPrezzoIniziale;
    private Utente utente;
    private Asta asta;

    private EditText prezzoMinimo;
    private ImageButton decrPrezzoMinimo;
    private ImageButton incrPrezzoMinimo;

    private EditText decrementoPrezzo;
    private ImageButton decrDecremento;
    private ImageButton incrDecremento;

    private TextView oreTextView, minutiTextView, secondiTextView;
    private ImageButton decrOre, incrOre;
    private ImageButton decrMinuti, incrMinuti;
    private ImageButton decrSecondi, incrSecondi;

    private ImageButton back_button;
    private Button creaButton;
    private boolean fromHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_asta_ribasso);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        fromHome = getIntent().getBooleanExtra("fromHome", true);
        Asta asta = (Asta) getIntent().getSerializableExtra("asta");
        utente = (Utente) getIntent().getSerializableExtra("utente");


        prezzoIniziale = findViewById(R.id.prezzo_iniziale);
        decrPrezzoIniziale = findViewById(R.id.decr_prezzo_iniziale);
        incrPrezzoIniziale = findViewById(R.id.incr_prezzo_iniziale);
        prezzoMinimo = findViewById(R.id.prezzo_minimo);
        decrPrezzoMinimo = findViewById(R.id.decr_prezzo_minimo);
        incrPrezzoMinimo = findViewById(R.id.incr_prezzo_minimo);
        decrementoPrezzo = findViewById(R.id.decremento);
        decrDecremento = findViewById(R.id.decr_decremento);
        incrDecremento = findViewById(R.id.incr_decremento);

        oreTextView = findViewById(R.id.ore_text_view);
        decrOre = findViewById(R.id.decr_ore);
        incrOre = findViewById(R.id.incr_ore);
        minutiTextView = findViewById(R.id.minuti_text_view);
        decrMinuti = findViewById(R.id.decr_minuti);
        incrMinuti = findViewById(R.id.incr_minuti);
        secondiTextView = findViewById(R.id.secondi_text_view);
        decrSecondi = findViewById(R.id.decr_secondi);
        incrSecondi = findViewById(R.id.incr_secondi);

        back_button = findViewById(R.id.back_button);
        creaButton = findViewById(R.id.crea_button);
        ImageButton home_button = findViewById(R.id.home_button);

        valoreInizialeFormattato(prezzoIniziale, 1.00f);
        valoreInizialeFormattato(prezzoMinimo, 1.00f);
        valoreInizialeFormattato(decrementoPrezzo, 1.00f);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityTipoAsta(utente, asta);
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

        decrPrezzoIniziale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float prezzoStandardIniziale = getValueFromEditText(prezzoIniziale);
                if (prezzoStandardIniziale > 1.00f) {
                    prezzoStandardIniziale -= 1.00f;
                    valoreInizialeFormattato(prezzoIniziale, prezzoStandardIniziale);
                }
            }
        });
        incrPrezzoIniziale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float prezzoStandardIniziale = getValueFromEditText(prezzoIniziale);
                prezzoStandardIniziale += 1.00f;
                valoreInizialeFormattato(prezzoIniziale, prezzoStandardIniziale);
            }
        });

        decrPrezzoMinimo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float prezzoStandardMinimo = getValueFromEditText(prezzoMinimo);
                if (prezzoStandardMinimo > 1.00f) {
                    prezzoStandardMinimo -= 1.00f;
                    valoreInizialeFormattato(prezzoMinimo, prezzoStandardMinimo);
                }
            }
        });
        incrPrezzoMinimo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float prezzoStandardMinimo = getValueFromEditText(prezzoMinimo);
                prezzoStandardMinimo += 1.00f;
                valoreInizialeFormattato(prezzoMinimo, prezzoStandardMinimo);
            }
        });

        decrDecremento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float decrementoStandard = getValueFromEditText(decrementoPrezzo);
                if (decrementoStandard > 1.00f) {
                    decrementoStandard -= 1.00f;
                    valoreInizialeFormattato(decrementoPrezzo, decrementoStandard);
                }
            }
        });
        incrDecremento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float decrementoStandard = getValueFromEditText(decrementoPrezzo);
                decrementoStandard += 1.00f;
                valoreInizialeFormattato(decrementoPrezzo, decrementoStandard);
            }
        });

        incrOre.setOnClickListener(v -> aggiornaTimer(oreTextView, 1));
        decrOre.setOnClickListener(v -> aggiornaTimer(oreTextView, -1));
        incrMinuti.setOnClickListener(v -> aggiornaTimer(minutiTextView, 1));
        decrMinuti.setOnClickListener(v -> aggiornaTimer(minutiTextView, -1));
        incrSecondi.setOnClickListener(v -> aggiornaTimer(secondiTextView, 1));
        decrSecondi.setOnClickListener(v -> aggiornaTimer(secondiTextView, -1));

        creaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = String.format("%02d:%02d:%02d",
                        Integer.parseInt(oreTextView.getText().toString()),
                        Integer.parseInt(minutiTextView.getText().toString()),
                        Integer.parseInt(secondiTextView.getText().toString()));
                float valIniziale = getValueFromEditText(prezzoIniziale);
                float valMinimo = getValueFromEditText(prezzoMinimo);
                float valDecremento = getValueFromEditText(decrementoPrezzo);

                if(valDecremento < valIniziale && valMinimo < valIniziale) {
                    Asta_RibassoDTO astaR = new Asta_RibassoDTO();
                    astaR.setIdCreatore(asta.getId_creatore());
                    astaR.setNome(asta.getNome());
                    astaR.setDescrizione(asta.getDescrizione());
                    astaR.setFoto(asta.getFoto());
                    astaR.setPrezzo(valIniziale);
                    astaR.setCategoria(asta.getCategoria());
                    astaR.setDecremento(valDecremento);
                    astaR.setMinimo(valMinimo);
                    astaR.setTimer(time);

                    RetrofitService retrofitService = new RetrofitService();
                    ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);

                    apiService.creaAstaAlRibasso(astaR)
                            .enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(CreaAstaRibassoActivity.this, "Asta creata con successo!", Toast.LENGTH_SHORT).show();
                                        openActivityHome(utente);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(CreaAstaRibassoActivity.this, "Errore durante la creazione dell'asta!", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreaAstaRibassoActivity.this);

                    builder.setMessage("Errore, importi non validi.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }

    private float getValueFromEditText(EditText editText) {
        String cleanString = editText.getText().toString().replaceAll("[€,.\\s]", "").trim();
        float value;
        try {
            value = Float.parseFloat(cleanString);
        } catch (NumberFormatException e) {
            value = 0.00f;
        }
        return value / 100;
    }

    private void valoreInizialeFormattato(EditText editText, float val) {
        String valFormattato = NumberFormat.getCurrencyInstance(Locale.ITALY).format(val);
        editText.setText(valFormattato);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openActivityTipoAsta(utente, asta);
        finish();
    }

    private void openActivityTipoAsta(Utente utente, Asta asta) {
        Intent intent = new Intent(this, TipoAstaActivity.class);
        intent.putExtra("utente", utente);
        intent.putExtra("tipoUtente", utente.getTipo());
        intent.putExtra("asta", asta);
        intent.putExtra("fromHome", fromHome);
        startActivity(intent);
    }

    private void aggiornaTimer(TextView textView, int val) {
        int value = Integer.parseInt(textView.getText().toString()) + val;
        textView.setText(String.format("%02d", Math.max(0, value)));
    }

    private void openActivityHome(Utente utente) {
        Intent intentH = new Intent(this, HomeActivity.class);
        intentH.putExtra("utente", utente);
        startActivity(intentH);
    }
}
