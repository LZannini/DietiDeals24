package com.example.dietideals24;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.dietideals24.adapters.NotificaAdapter;
import com.example.dietideals24.api.ApiService;
import com.example.dietideals24.dto.NotificaDTO;
import com.example.dietideals24.dto.UtenteDTO;
import com.example.dietideals24.models.Utente;
import com.example.dietideals24.retrofit.RetrofitService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificaActivity extends AppCompatActivity {

    private ListView listView;
    private NotificaAdapter adapter;
    private List<NotificaDTO> listaNotifiche;
    private Utente utente;
    private ImageButton back_button;
    private TextView noResultsText;
    private ApiService apiService;
    private Button btnSegnaTutte, btnRimuoviLette, btnRimuoviTutte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifica);

        utente = (Utente) getIntent().getSerializableExtra("utente");
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

        RetrofitService retrofitService = new RetrofitService();
        apiService = retrofitService.getRetrofit().create(ApiService.class);

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
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            NotificaDTO notifica = listaNotifiche.get(position);
            segnaComeLetta(notifica, apiService);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage(notifica.getTesto());

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
        });
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
                    for (NotificaDTO notifica : listaNotifiche) {
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
}

