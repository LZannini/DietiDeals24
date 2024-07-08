package com.example.dietideals24;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietideals24.adapters.OfferAdapter;
import com.example.dietideals24.api.ApiService;
import com.example.dietideals24.dto.Asta_InversaDTO;
import com.example.dietideals24.dto.Asta_RibassoDTO;
import com.example.dietideals24.dto.Asta_SilenziosaDTO;
import com.example.dietideals24.dto.OffertaDTO;
import com.example.dietideals24.enums.StatoOfferta;
import com.example.dietideals24.models.Asta;
import com.example.dietideals24.models.Asta_Inversa;
import com.example.dietideals24.models.Asta_Ribasso;
import com.example.dietideals24.models.Asta_Silenziosa;
import com.example.dietideals24.models.Offerta;
import com.example.dietideals24.models.Utente;
import com.example.dietideals24.retrofit.RetrofitService;

import java.io.Serializable;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DettagliAstaActivity extends AppCompatActivity implements OfferAdapter.OnOffertaListener {

    private LinearLayout userSection, creatorSection;
    private EditText etTitle, etDescription, etOffer;
    private TextView tvPrice, tvCategoryValue, tvCreatorValue, tvPriceValue, tvDecrementValue, tvTimerValue, tvLowestOffer, tvLowestOfferValue;
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
    private boolean isRibasso;
    private Utente utenteProfilo;
    private Utente utenteCreatore;
    List<Offerta> offerte = new ArrayList<>();

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

        isRibasso = false;
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

        userSection = findViewById(R.id.userSection);
        creatorSection = findViewById(R.id.creatorSection);
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
        tvLowestOffer = findViewById(R.id.tvLowestOffer);
        tvLowestOfferValue = findViewById(R.id.tvLowestOfferValue);
        etOffer = findViewById(R.id.etOffer);
        btnSubmitOffer = findViewById(R.id.btnSubmitOffer);
        btnBack = findViewById(R.id.back_button);
        ImageButton home_button = findViewById(R.id.home_button);

        if (asta.getId_creatore() == utente.getId()) {
            creatorSection.setVisibility(View.VISIBLE);
            userSection.setVisibility(View.GONE);
        } else {
            userSection.setVisibility(View.VISIBLE);
            creatorSection.setVisibility(View.GONE);
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
        if(fromAsteCreate && !fromDettagli)
            tvCreatorValue.setText(utenteProfilo.getUsername());
        else
            tvCreatorValue.setText(utenteCreatore.getUsername());

        if (asta instanceof Asta_Ribasso) {
            ivTypeValue.setImageResource(R.drawable.ribasso);
            isRibasso = true;
            apiService.recuperaDettagliAstaRibasso(asta.getId())
                    .enqueue(new Callback<Asta_RibassoDTO>() {
                        @Override
                        public void onResponse(Call<Asta_RibassoDTO> call, Response<Asta_RibassoDTO> response) {
                            Asta_RibassoDTO astaRicevuta = response.body();

                            tvLowestOffer.setVisibility(View.GONE);
                            tvLowestOfferValue.setVisibility(View.GONE);
                            tvPriceValue.setText(NumberFormat.getCurrencyInstance(Locale.ITALY).format(astaRicevuta.getPrezzo()));
                            tvDecrementValue.setText(NumberFormat.getCurrencyInstance(Locale.ITALY).format(astaRicevuta.getDecremento()));
                            tvTimerValue.setText(astaRicevuta.getTimer());

                            setupPolling();
                            handler.post(pollingRunnable);
                            handler.post(timerRunnable);

                            etOffer.setEnabled(false);
                            etOffer.setText(String.valueOf(astaRicevuta.getPrezzo()));
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

                            tvLowestOffer.setVisibility(View.GONE);
                            tvLowestOfferValue.setVisibility(View.GONE);

                            LocalDateTime scadenzaAsta = LocalDateTime.parse(astaRicevuta.getScadenza(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                            tvTimerValue.setText(calcolaTempoRimanente(scadenzaAsta));
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

                            if(astaRicevuta.getOffertaMinore() != null) {
                                tvLowestOfferValue.setText(NumberFormat.getCurrencyInstance(Locale.ITALY).format(astaRicevuta.getOffertaMinore()));
                            } else {
                                tvLowestOfferValue.setText("Ancora nessuna offerta per questa asta.");
                            }
                            tvPriceValue.setText(NumberFormat.getCurrencyInstance(Locale.ITALY).format(astaRicevuta.getPrezzo()));

                            LocalDateTime scadenzaAsta = LocalDateTime.parse(astaRicevuta.getScadenza(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            tvTimerValue.setText(calcolaTempoRimanente(scadenzaAsta));
                        }

                        @Override
                        public void onFailure(Call<Asta_InversaDTO> call, Throwable t) {

                        }
                    });
        }

        if (asta.getId_creatore() == utente.getId() && !isRibasso) {
            RecyclerView recyclerView = findViewById(R.id.recyclerViewOfferte);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setVisibility(View.VISIBLE);

            OfferAdapter adapter = new OfferAdapter(offerte, this);
            recyclerView.setAdapter(adapter);
            apiService.recuperaOffertePerId(asta.getId())
                    .enqueue(new Callback<List<OffertaDTO>>() {
                        @Override
                        public void onResponse(Call<List<OffertaDTO>> call, Response<List<OffertaDTO>> response) {
                            if(response.isSuccessful() && response.body() != null) {
                                List<OffertaDTO> offerteResponse = response.body();
                                List<Offerta> offerteList = creaListModelloOfferta(offerteResponse);
                                for(Offerta o : offerteList) {
                                    offerte.add(o);
                                    adapter.notifyDataSetChanged();
                                }

                            } else if(response.body() != null) {
                                userSection.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onFailure(Call<List<OffertaDTO>> call, Throwable t) {
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

        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityHome();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(DettagliAstaActivity.this);
            builder.setMessage("Sei sicuro di voler presentare l'offerta?")
                    .setCancelable(true)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            OffertaDTO offerta = new OffertaDTO();
                            offerta.setId_asta(asta.getId());
                            offerta.setId_utente(utente.getId());
                            offerta.setValore(Float.parseFloat(etOffer.getText().toString()));
                            offerta.setStato(StatoOfferta.ATTESA);

                            LocalDateTime currentDateTime = LocalDateTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            String dateTimeString = currentDateTime.format(formatter);


                            offerta.setData(dateTimeString);

                            apiService.creaOfferta(offerta)
                                            .enqueue(new Callback<Void>() {
                                                @Override
                                                public void onResponse(Call<Void> call, Response<Void> response) {
                                                    Toast.makeText(DettagliAstaActivity.this, "Offerta presentata con successo!", Toast.LENGTH_SHORT).show();
                                                    if (fromAsteCreate)
                                                        openActivityAsteCreate();
                                                    else
                                                        openActivityRisultatiRicerca();
                                                }

                                                @Override
                                                public void onFailure(Call<Void> call, Throwable t) {
                                                    Toast.makeText(DettagliAstaActivity.this, "Errore durante la creazione dell'offerta!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .show();
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

    public void openActivityHome() {
        Intent intentR = new Intent(this, HomeActivity.class);
        intentR.putExtra("utente", utente);
        startActivity(intentR);
    }

    private void openActivityAsteCreate() {
        Intent intent = new Intent(this, AsteCreateActivity.class);
        intent.putExtra("listaAste", (Serializable) listaAste);
        intent.putExtra("utente_home", utente);
        intent.putExtra("utente", utenteCreatore);
        intent.putExtra("utenteCreatore", utenteCreatore);
        intent.putExtra("fromDettagli", fromDettagli);
        intent.putExtra("modificaAvvenuta", modificaAvvenuta);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
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

    private String calcolaTempoRimanente(LocalDateTime endDateTime) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(endDateTime)) {
            return "Scaduta";
        }

        Period period = Period.between(now.toLocalDate(), endDateTime.toLocalDate());
        Duration duration = Duration.between(now.toLocalTime(), endDateTime.toLocalTime());

        if (period.getYears() > 0) {
            return period.getYears() + " anni, " + period.getMonths() + " mesi";
        } else if (period.getMonths() > 0) {
            return period.getMonths() + " mesi, " + period.getDays() + " giorni";
        } else if (period.getDays() > 0) {
            return period.getDays() + " giorni";
        } else if (duration.toHours() > 0) {
            return duration.toHours() + " ore";
        } else if (duration.toMinutes() > 0) {
            return duration.toMinutes() + " minuti";
        } else {
            return "meno di un minuto";
        }
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
            offerteList.add(offerta);
        }
        return offerteList;
    }

    @Override
    public void onAcceptClick(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Sei sicuro di voler accettare questa offerta?")
                .setCancelable(true)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RetrofitService retrofitService = new RetrofitService();
                        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);

                        apiService.accettaOfferta(offerte.get(position).getId())
                                .enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        Toast.makeText(DettagliAstaActivity.this, "Hai accettato l'offerta con successo!", Toast.LENGTH_SHORT).show();
                                        openActivityAsteCreate();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Logger.getLogger(DettagliAstaActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
                                    }
                                });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }


    @Override
    public void onRejectClick(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Sei sicuro di voler rifiutare questa offerta?")
                .setCancelable(true)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RetrofitService retrofitService = new RetrofitService();
                        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);

                        apiService.rifiutaOfferta(offerte.get(position).getId())
                                .enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        Toast.makeText(DettagliAstaActivity.this, "Hai rifiutato l'offerta con successo!", Toast.LENGTH_SHORT).show();
                                        openActivityAsteCreate();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Logger.getLogger(DettagliAstaActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
                                    }
                                });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
}

