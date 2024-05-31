package com.example.dietideals24;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dietideals24.adapters.NotificaAdapter;
import com.example.dietideals24.api.ApiService;
import com.example.dietideals24.dto.NotificaDTO;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifica);

        utente = (Utente) getIntent().getSerializableExtra("utente");

        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        listView = findViewById(R.id.notifiche_list_view);

        listaNotifiche = (List<NotificaDTO>) getIntent().getSerializableExtra("listaNotifiche");

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
}

