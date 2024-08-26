package com.example.dietideals24;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dietideals24.adapters.NotificaAdapter;
import com.example.dietideals24.api.ApiService;
import com.example.dietideals24.dto.AstaDTO;
import com.example.dietideals24.dto.Asta_InversaDTO;
import com.example.dietideals24.dto.Asta_RibassoDTO;
import com.example.dietideals24.dto.Asta_SilenziosaDTO;
import com.example.dietideals24.dto.NotificaDTO;
import com.example.dietideals24.dto.UtenteDTO;
import com.example.dietideals24.models.Asta;
import com.example.dietideals24.models.Asta_Inversa;
import com.example.dietideals24.models.Asta_Ribasso;
import com.example.dietideals24.models.Asta_Silenziosa;
import com.example.dietideals24.models.Notifica;
import com.example.dietideals24.models.Utente;
import com.example.dietideals24.retrofit.RetrofitService;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificaActivity extends AppCompatActivity implements NotificaAdapter.OnAstaClickListener {

    private ListView listView;
    private NotificaAdapter adapter;
    private List<NotificaDTO> listaNotifiche;
    private Utente utente;
    private Utente UtenteCreatore;
    private FirebaseAnalytics mfBanalytics;
    private Asta asta_ricevuta;
    private ImageButton back_button;
    private TextView noResultsText;
    private ApiService apiService;
    private Button btnSegnaTutte, btnRimuoviLette, btnRimuoviTutte;

    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifica);

        mfBanalytics = FirebaseAnalytics.getInstance(this);
        logActivityEvent("NotificaActivity");

        utente = (Utente) getIntent().getSerializableExtra("utente");
        asta_ricevuta = (Asta) getIntent().getSerializableExtra("asta_ricevuta");

        noResultsText = findViewById(R.id.no_results_text);

        back_button = findViewById(R.id.back_button);
        btnSegnaTutte = findViewById(R.id.btnSegna);
        btnRimuoviLette = findViewById(R.id.btnRmvRead);
        btnRimuoviTutte = findViewById(R.id.btnRmvAll);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityHome(utente);
                finish();
            }
        });

        btnSegnaTutte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostraDialogoMarcaTutte();
            }
        });

        btnRimuoviLette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostraDialogoEliminaLette();
            }
        });

        btnRimuoviTutte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostraDialogoEliminaTutte();
            }
        });


        apiService = RetrofitService.getRetrofit(this).create(ApiService.class);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        listView = findViewById(R.id.notifiche_list_view);

        listaNotifiche = (List<NotificaDTO>) getIntent().getSerializableExtra("listaNotifiche");
        if (listaNotifiche == null || listaNotifiche.isEmpty()) {
            noResultsText.setVisibility(View.VISIBLE);
            btnSegnaTutte.setEnabled(false);
            btnRimuoviTutte.setEnabled(false);
            btnRimuoviLette.setEnabled(false);
        }

            adapter = new NotificaAdapter(this, listaNotifiche);
            adapter.setOnAstaClickListener(this);
            listView.setAdapter(adapter);

            for (NotificaDTO notifica : listaNotifiche) {
                if(notifica.getId_Asta()!=0)
                RecuperaAsta(notifica, apiService);
            }

    }

    private void mostraDialogoEliminaTutte() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Vuoi davvero eliminare tutte le notifiche?");
        builder.setPositiveButton("Elimina", (dialog, which) -> {
            svuotaNotifiche(apiService);
        });
        builder.setNegativeButton("Annulla", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void mostraDialogoEliminaLette() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Vuoi davvero eliminare tutte le notifiche lette?");
        builder.setPositiveButton("Elimina", (dialog, which) -> {
            rimuoviNotificheLette(apiService);
        });
        builder.setNegativeButton("Annulla", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void mostraDialogoMarcaTutte() {
        Call<Void> call = apiService.segnaTutteLeNotifiche(utente.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    for(NotificaDTO notifica : listaNotifiche) {
                        notifica.setLetta(true);
                    }
                    adapter.notifyDataSetChanged();
                    Toast.makeText(NotificaActivity.this, "Tutte le notifiche sono state segnate come lette", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NotificaActivity.this, "Errore durante la marcatura delle notifiche, riprova!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(NotificaActivity.this, "Errore durante la marcatura delle notifiche, riprova!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(NotificaActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
            }
        });
    }

    private void rimuoviNotificheLette(ApiService apiService) {
        Call<Void> call = apiService.rimuoviAllNotificheLette(utente.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listaNotifiche.removeIf(NotificaDTO::isLetta);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(NotificaActivity.this, "Tutte le notifiche lette sono state eliminate", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NotificaActivity.this, "Errore durante l'eliminazione delle notifiche lette, riprova!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(NotificaActivity.this, "Errore durante l'eliminazione delle notifiche lette, riprova!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(NotificaActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
            }
        });
    }

    private void svuotaNotifiche(ApiService apiService) {
        Call<Void> call = apiService.svuotaNotifiche(utente.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listaNotifiche.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(NotificaActivity.this, "Tutte le notifiche sono state eliminate", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NotificaActivity.this, "Errore durante l'eliminazione delle notifiche", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(NotificaActivity.this, "Errore durante l'eliminazione delle notifiche, riprova!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(NotificaActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
            }
        });
    }

    private void rimuoviSelezionata(NotificaDTO notifica, ApiService apiService) {
        if (notifica != null) {
            Call<Void> call = apiService.rimuoviNotifica(notifica.getId());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        adapter.removeNotifica(notifica);
                        Toast.makeText(NotificaActivity.this, "Notifica rimossa", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NotificaActivity.this, "Errore durante la rimozione della notifica", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(NotificaActivity.this, "Errore durante la rimozione della notifica, riprova!", Toast.LENGTH_SHORT).show();
                    Logger.getLogger(NotificaActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
                }
            });
        }
    }

    private void segnaComeLetta(NotificaDTO notifica, ApiService apiService) {
        if (notifica != null) {
            Call<Void> call = apiService.segnaNotifica(notifica.getId());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        notifica.setLetta(true);
                        adapter.updateNotifica(notifica);
                    } else {
                        Toast.makeText(NotificaActivity.this, "Errore durante la marcatura della notifica, riprova!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(NotificaActivity.this, "Errore durante la marcatura della notifica, riprova!", Toast.LENGTH_SHORT).show();
                    Logger.getLogger(NotificaActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
                }
            });
        }
    }

    private void RecuperaAsta(NotificaDTO notifica, ApiService apiService) {
            int id_asta = notifica.getId_Asta();
            apiService.recuperaAsta(id_asta).enqueue(new Callback<AstaDTO>() {
                @Override
                public void onResponse(Call<AstaDTO> call, Response<AstaDTO> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        AstaDTO astadto = response.body();
                        Asta asta = converteToModel(astadto);
                        asta_ricevuta = asta;
                        if (asta instanceof Asta_Inversa) {
                            apiService.recuperaDettagliAstaInversa(id_asta).enqueue(new Callback<Asta_InversaDTO>() {
                                @Override
                                public void onResponse(Call<Asta_InversaDTO> call, Response<Asta_InversaDTO> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        String nome_asta = asta.getNome();
                                        notifica.setNome_asta(nome_asta);
                                        asta_ricevuta = (Asta_Inversa) asta;
                                        adapter.notifyDataSetChanged();
                                    } else
                                        Toast.makeText(NotificaActivity.this, "Asta Inversa non trovata", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onFailure(Call<Asta_InversaDTO> call, Throwable t) {
                                    Toast.makeText(NotificaActivity.this, "Errore di Connessione", Toast.LENGTH_SHORT).show();
                                    Logger.getLogger(NotificaActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
                                }
                            });
                        } else if (asta instanceof Asta_Ribasso) {
                            apiService.recuperaDettagliAstaRibasso(id_asta).enqueue(new Callback<Asta_RibassoDTO>() {
                                @Override
                                public void onResponse(Call<Asta_RibassoDTO> call, Response<Asta_RibassoDTO> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        String nome_asta = asta.getNome();
                                        notifica.setNome_asta(nome_asta);
                                        asta_ricevuta = (Asta_Ribasso) asta;
                                        adapter.notifyDataSetChanged();
                                    } else
                                        Toast.makeText(NotificaActivity.this, "Asta Ribasso non trovata", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onFailure(Call<Asta_RibassoDTO> call, Throwable t) {
                                    Toast.makeText(NotificaActivity.this, "Errore di Connessione", Toast.LENGTH_SHORT).show();
                                    Logger.getLogger(NotificaActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
                                }
                            });
                        } else if (asta instanceof Asta_Silenziosa) {
                            apiService.recuperaDettagliAstaSilenziosa(id_asta).enqueue(new Callback<Asta_SilenziosaDTO>() {
                                @Override
                                public void onResponse(Call<Asta_SilenziosaDTO> call, Response<Asta_SilenziosaDTO> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        String nome_asta = asta.getNome();
                                        notifica.setNome_asta(nome_asta);
                                        asta_ricevuta = (Asta_Silenziosa) asta;
                                        adapter.notifyDataSetChanged();
                                    } else
                                        Toast.makeText(NotificaActivity.this, "Asta Silenziosa non trovata", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onFailure(Call<Asta_SilenziosaDTO> call, Throwable t) {
                                    Toast.makeText(NotificaActivity.this, "Errore di Connessione", Toast.LENGTH_SHORT).show();
                                    Logger.getLogger(NotificaActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
                                }
                            });
                        }
                    } else
                        Toast.makeText(NotificaActivity.this, "Asta non trovata", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<AstaDTO> call, Throwable t) {
                    Toast.makeText(NotificaActivity.this, "Errore di Connessione", Toast.LENGTH_SHORT).show();
                    Logger.getLogger(NotificaActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
                }
            });

    }

    private void recuperaUtenteCreatore(int id_creatore,ApiService apiService) {
        Call<UtenteDTO> call;
        call = apiService.recuperaUtente(id_creatore);
        call.enqueue(new Callback<UtenteDTO>() {
            @Override
            public void onResponse(Call<UtenteDTO> call, Response<UtenteDTO> response) {
                UtenteDTO user = response.body();
                if (user != null) {
                        UtenteCreatore = creaCreatoreAsta(user);
                        openActivityDettagliAsta();

                }else
                    Toast.makeText(NotificaActivity.this, "Utente non trovato", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<UtenteDTO> call, Throwable t) {
                Toast.makeText(NotificaActivity.this, "Errore di Connessione", Toast.LENGTH_SHORT).show();
                Logger.getLogger(Notifica.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
            }
        });
    }

    public Utente creaCreatoreAsta(UtenteDTO utenteDTO) {
        Utente u = new Utente();
        u.setId(utenteDTO.getId());
        u.setUsername(utenteDTO.getUsername());
        u.setEmail(utenteDTO.getEmail());
        u.setPassword(utenteDTO.getPassword());
        u.setTipo(utenteDTO.getTipo());
        if (utenteDTO.getAvatar() != null) u.setAvatar(utenteDTO.getAvatar());
        if (utenteDTO.getBiografia() != null) u.setBiografia(utenteDTO.getBiografia());
        if (utenteDTO.getPaese() != null) u.setPaese(utenteDTO.getPaese());
        if (utenteDTO.getSitoweb() != null) u.setSitoweb(utenteDTO.getSitoweb());
        return u;
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

    public Asta converteToModel(AstaDTO asta) {
        Asta a = new Asta();
        if (asta.getTipo().equals("RIBASSO"))
            a = creaModelloAstaR(asta);
        else if (asta.getTipo().equals("SILENZIOSA"))
            a = creaModelloAstaS(asta);
        else if (asta.getTipo().equals("INVERSA"))
            a = creaModelloAstaI(asta);

        return a;
    }

    @Override
    public void onAstaClicked(NotificaDTO notifica) {

        RecuperaAsta(notifica, apiService);

        if(!notifica.isLetta())
            segnaComeLetta(notifica,apiService);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (asta_ricevuta != null)
                    recuperaUtenteCreatore(asta_ricevuta.getId_creatore(),apiService);
                else
                    handler.postDelayed(this, 100);
            }
        }, 100);

    }

    @Override
    public void onNotificaClicked(NotificaDTO notifica){
        segnaComeLetta(notifica, apiService);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        if(notifica.getNome_asta() == null || notifica.getId_Asta() == 0)
        builder.setMessage(notifica.getTesto());
        else
            builder.setMessage(notifica.getTesto() + ' ' + notifica.getNome_asta());

        builder.setPositiveButton("Rimuovi", (dialog, which) -> {
            rimuoviSelezionata(notifica, apiService);
        });

        builder.setNegativeButton("Chiudi", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        if (!notifica.isLetta()) {
            notifica.setLetta(true);
            adapter.updateNotifica(notifica);
        }
    }



    private void openActivityDettagliAsta() {
            Intent intent = new Intent(this, DettagliAstaActivity.class);
            intent.putExtra("asta", asta_ricevuta);
            intent.putExtra("utenteCreatore",UtenteCreatore);
            intent.putExtra("utente", utente);
            intent.putExtra("fromNotifica",true);
            intent.putExtra("listaNotifiche",(Serializable) listaNotifiche);
            startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openActivityHome(utente);
        finish();
    }

    private void openActivityHome(Utente utente) {
        Intent intentH = new Intent(this, HomeActivity.class);
        intentH.putExtra("utente", utente);
        startActivity(intentH);
    }

    private void logActivityEvent(String activityName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, activityName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, activityName);
        mfBanalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }
}
