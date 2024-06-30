package com.example.dietideals24;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dietideals24.api.ApiService;
import com.example.dietideals24.dto.Asta_InversaDTO;
import com.example.dietideals24.dto.Asta_RibassoDTO;
import com.example.dietideals24.dto.Asta_SilenziosaDTO;
import com.example.dietideals24.dto.UtenteDTO;
import com.example.dietideals24.models.Asta;
import com.example.dietideals24.models.Asta_Inversa;
import com.example.dietideals24.models.Asta_Ribasso;
import com.example.dietideals24.models.Asta_Silenziosa;
import com.example.dietideals24.models.Utente;
import com.example.dietideals24.retrofit.RetrofitService;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DettagliAstaActivity extends AppCompatActivity {

    private EditText etTitle, etDescription, etOffer;
    private TextView tvPrice, tvCategoryValue, tvCreatorValue, tvPriceValue, tvDecrementValue, tvTimerValue;
    private ImageView ivTypeValue;
    private Button btnSubmitOffer;
    private ImageView ivFoto;
    private ImageButton btnBack;
    private Bitmap bitmap;
    private Asta asta;
    private List<Asta> listaAste;
    private String criterioRicerca;
    private Utente utente;
    private boolean fromAsteCreate;
    private boolean fromDettagli;
    private boolean modificaAvvenuta;
    private Utente utenteProfilo;
    private Utente utenteCreatore;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final int POLLING_INTERVAL = 1000;
    private final int TIMER_INTERVAL = 1000;
    private Runnable pollingRunnable;
    private Runnable timerRunnable;
    private long timerValue;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_asta);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        modificaAvvenuta = getIntent().getBooleanExtra("modificaAvvenuta", false);
        fromDettagli = getIntent().getBooleanExtra("fromDettagli", false);
        asta = (Asta) getIntent().getSerializableExtra("asta");
        listaAste = (List<Asta>) getIntent().getSerializableExtra("listaAste");
        if (getIntent().getStringExtra("criterioRicerca") != null)
            criterioRicerca = getIntent().getStringExtra("criterioRicerca");
        utente = (Utente) getIntent().getSerializableExtra("utente");
        utenteProfilo = (Utente) getIntent().getSerializableExtra("utenteProfilo");
        fromAsteCreate = getIntent().getBooleanExtra("fromAsteCreate", false);
        utenteCreatore = (Utente) getIntent().getSerializableExtra("utenteCreatore");

        ivFoto = findViewById(R.id.imageView_Foto);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        tvCategoryValue = findViewById(R.id.tvCategoryValue);
        ivTypeValue = findViewById(R.id.ivTypeValue);
        tvCreatorValue = findViewById(R.id.tvCreatorValue);
        tvPriceValue = findViewById(R.id.tvPriceValue);
        tvPrice = findViewById(R.id.tvPrice);
        tvDecrementValue = findViewById(R.id.tvDecrementValue);
        tvTimerValue = findViewById(R.id.tvTimerValue);
        etOffer = findViewById(R.id.etOffer);
        btnSubmitOffer = findViewById(R.id.btnSubmitOffer);
        btnBack = findViewById(R.id.back_button);

        if (fromAsteCreate) {
            btnSubmitOffer.setEnabled(false);
            btnSubmitOffer.setVisibility(View.INVISIBLE);
            etOffer.setEnabled(false);
            etOffer.setVisibility(View.INVISIBLE);
            tvPrice.setVisibility(View.INVISIBLE);
        }

        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);

        byte[] fotoBytes = asta.getFoto();
        if (fotoBytes != null) {
            bitmap = BitmapFactory.decodeByteArray(fotoBytes, 0, fotoBytes.length);
            ivFoto.setImageBitmap(bitmap);
        }

        etTitle.setText(asta.getNome());
        etDescription.setText(asta.getDescrizione());
        tvCategoryValue.setText(asta.getCategoria().toString());
        if(fromAsteCreate)
            tvCreatorValue.setText(utenteProfilo.getUsername());
        else
            tvCreatorValue.setText(utenteCreatore.getUsername());

        if (asta instanceof Asta_Ribasso) {
            ivTypeValue.setImageResource(R.drawable.ribasso);
            apiService.recuperaDettagliAstaRibasso(asta.getId())
                    .enqueue(new Callback<Asta_RibassoDTO>() {
                        @Override
                        public void onResponse(Call<Asta_RibassoDTO> call, Response<Asta_RibassoDTO> response) {
                            Asta_RibassoDTO astaRicevuta = response.body();

                            tvPriceValue.setText(NumberFormat.getCurrencyInstance(Locale.ITALY).format(astaRicevuta.getPrezzo()));
                            tvDecrementValue.setText(NumberFormat.getCurrencyInstance(Locale.ITALY).format(astaRicevuta.getDecremento()));
                            tvTimerValue.setText(astaRicevuta.getTimer());

                            setupPolling();
                            handler.post(pollingRunnable);
                            handler.post(timerRunnable);
                        }

                        @Override
                        public void onFailure(Call<Asta_RibassoDTO> call, Throwable t) {

                        }
                    });
        } else if (asta instanceof Asta_Silenziosa) {
            ivTypeValue.setImageResource(R.drawable.silenziosa);
            apiService.recuperaDettagliAstaSilenziosa(asta.getId())
                    .enqueue(new Callback<Asta_SilenziosaDTO>() {
                        @Override
                        public void onResponse(Call<Asta_SilenziosaDTO> call, Response<Asta_SilenziosaDTO> response) {
                            Asta_SilenziosaDTO astaRicevuta = response.body();

                            tvTimerValue.setText(astaRicevuta.getScadenza());
                        }

                        @Override
                        public void onFailure(Call<Asta_SilenziosaDTO> call, Throwable t) {

                        }
                    });
        } else if (asta instanceof Asta_Inversa) {
            ivTypeValue.setImageResource(R.drawable.inversa);
            apiService.recuperaDettagliAstaInversa(asta.getId())
                    .enqueue(new Callback<Asta_InversaDTO>() {
                        @Override
                        public void onResponse(Call<Asta_InversaDTO> call, Response<Asta_InversaDTO> response) {
                            Asta_InversaDTO astaRicevuta = response.body();

                            tvPriceValue.setText(NumberFormat.getCurrencyInstance(Locale.ITALY).format(astaRicevuta.getPrezzo()));
                            tvTimerValue.setText(astaRicevuta.getScadenza());
                        }

                        @Override
                        public void onFailure(Call<Asta_InversaDTO> call, Throwable t) {

                        }
                    });
        }

        adjustEditTextWidth(etTitle);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromAsteCreate)
                    openActivityAsteCreate();
                else
                    openActivityRisultatiRicerca();
                finish();
            }
        });

        if(asta.getId_creatore() != utente.getId() && !fromAsteCreate) {
            tvCreatorValue.setTypeface(tvCreatorValue.getTypeface(), Typeface.BOLD);
            tvCreatorValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openActivityProfilo();
                }
            });
        }

        btnSubmitOffer.setOnClickListener(v -> {
            String offer = etOffer.getText().toString();
        });
    }

    @Override
    public void onBackPressed() {
        if (fromAsteCreate)
            openActivityAsteCreate();
        else
            openActivityRisultatiRicerca();
        finish();
    }

    private void openActivityRisultatiRicerca() {
        Intent intent = new Intent(this, RisultatiRicercaActivity.class);
        intent.putExtra("listaAste", (Serializable) listaAste);
        intent.putExtra("criterioRicerca", criterioRicerca);
        intent.putExtra("utente", utente);
        startActivity(intent);
    }

    private void openActivityAsteCreate() {
        Intent intent = new Intent(this, AsteCreateActivity.class);
        intent.putExtra("listaAste", (Serializable) listaAste);
        intent.putExtra("utente_home", utente);
        intent.putExtra("utente", utenteProfilo);
        intent.putExtra("utenteCreatore", utenteCreatore);
        intent.putExtra("fromDettagli", fromDettagli);
        intent.putExtra("modificaAvvenuta", modificaAvvenuta);
        startActivity(intent);
    }

    private void openActivityProfilo() {
        Intent intentR = new Intent(this, ProfiloActivity.class);
        intentR.putExtra("utente_home", utente);
        intentR.putExtra("utente", utenteCreatore);
        intentR.putExtra("fromDettagli", true);
        startActivity(intentR);
    }

    private void adjustEditTextWidth(EditText editText) {
        String text = editText.getText().toString();
        Paint paint = editText.getPaint();
        float textWidth = paint.measureText(text);

        int padding = editText.getPaddingLeft() + editText.getPaddingRight();
        editText.setWidth((int) (textWidth + padding));
    }

    private void setupPolling() {
        pollingRunnable = new Runnable() {
            @Override
            public void run() {
                aggiornaDettagliAsta();
                handler.postDelayed(this, POLLING_INTERVAL);
            }
        };

        timerRunnable = new Runnable() {
            @Override
            public void run() {
                updateTimer();
                handler.postDelayed(this, TIMER_INTERVAL);
            }
        };
    }

    private void aggiornaDettagliAsta() {
        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);

        apiService.recuperaDettagliAstaRibasso(asta.getId())
                .enqueue(new Callback<Asta_RibassoDTO>() {
                    @Override
                    public void onResponse(Call<Asta_RibassoDTO> call, Response<Asta_RibassoDTO> response) {
                        Asta_RibassoDTO asta = response.body();
                        updateUI(asta);
                    }

                    @Override
                    public void onFailure(Call<Asta_RibassoDTO> call, Throwable t) {
                    }
                });
    }

    private void updateUI(Asta_RibassoDTO asta) {
        if (asta != null) {
            tvPriceValue.setText(NumberFormat.getCurrencyInstance(Locale.ITALY).format(asta.getPrezzo()));
            tvDecrementValue.setText(NumberFormat.getCurrencyInstance(Locale.ITALY).format(asta.getDecremento()));
            timerValue = convertToMilliseconds(asta.getTimer());
        }
    }

    private long convertToMilliseconds(String timer) {
        String[] parts = timer.split(":");
        long hours = Long.parseLong(parts[0]);
        long minutes = Long.parseLong(parts[1]);
        long seconds = Long.parseLong(parts[2]);
        return (hours * 3600 + minutes * 60 + seconds) * 1000;
    }

    private void updateTimer() {
        if (timerValue > 0) {
            timerValue -= 1000;
            tvTimerValue.setText(formatMilliseconds(timerValue));
        } else {
            aggiornaDettagliAsta();
        }
    }

    private String formatMilliseconds(long milliseconds) {
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60)) % 60;
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(pollingRunnable);
        handler.removeCallbacks(timerRunnable);
    }

}

