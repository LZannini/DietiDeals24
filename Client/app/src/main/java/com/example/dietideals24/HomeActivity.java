package com.example.dietideals24;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dietideals24.api.ApiService;
import com.example.dietideals24.dto.NotificaDTO;
import com.example.dietideals24.dto.UtenteDTO;
import com.example.dietideals24.models.Utente;

import com.example.dietideals24.retrofit.RetrofitService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout buttonCrea;
    private LinearLayout buttonCerca;
    private LinearLayout buttonProfilo;
    private Button buttonNotifica;
    private LinearLayout buttonDisconnetti;
    private AlertDialog.Builder builder;
    private Utente utente;
    private UtenteDTO utenteDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        utenteDTO = (UtenteDTO) getIntent().getSerializableExtra("utente");
        utente = creaUtenteLoggato(utenteDTO);

        buttonCrea = findViewById(R.id.button_crea);
        buttonCerca = findViewById(R.id.button_cerca);
        buttonProfilo = findViewById(R.id.button_profilo);
        buttonNotifica = findViewById(R.id.button_notifica);
        buttonDisconnetti = findViewById(R.id.button_disconnetti);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        buttonCrea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityCreaAsta(utente);
            }
        });

        buttonCerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityCercaAsta(utenteDTO);
            }
        });

        buttonProfilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityProfilo(utente);
            }
        });


        RetrofitService retrofitService = new RetrofitService();

        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);
        buttonNotifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperaNotifiche(apiService);
            }
        });

        buttonDisconnetti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disconnect();
            }
        });
    }

    private void recuperaNotifiche(ApiService apiService) {
        Call<List<NotificaDTO>> call;

        call = apiService.mostraNotifiche(utente.getId());

        call.enqueue(new Callback<List<NotificaDTO>>() {
            @Override
            public void onResponse(Call<List<NotificaDTO>> call, Response<List<NotificaDTO>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<NotificaDTO> notifiche = response.body();
                    Intent intent = new Intent(HomeActivity.this, NotificaActivity.class);
                    intent.putExtra("listaNotifiche", (Serializable) notifiche);
                    intent.putExtra("utente", utente);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(HomeActivity.this, NotificaActivity.class);
                    intent.putExtra("listaNotifiche", new ArrayList<NotificaDTO>());
                    intent.putExtra("utente", utente);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<List<NotificaDTO>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Errore durante il caricamento delle notifiche, riprova!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //
    }

    public Utente creaUtenteLoggato(UtenteDTO utenteDTO) {
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

    private void disconnect() {
        builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setMessage("Sei sicuro di voler uscire?")
                .setCancelable(true)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openActivityLogin();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }

    private void openActivityCreaAsta(Utente u) {
        Intent intentR = new Intent(this, CreaAstaActivity.class);
        intentR.putExtra("utente", u);
        startActivity(intentR);
    }

    private void openActivityCercaAsta(UtenteDTO utenteDTO) {
        Intent intentR = new Intent(this, CercaAstaActivity.class);
        intentR.putExtra("utente", utenteDTO);
        startActivity(intentR);
    }

    private void openActivityLogin() {
        Intent intentL = new Intent(this, LoginActivity.class);
        startActivity(intentL);
    }

    private void openActivityProfilo(Utente utente) {
        Intent intentR = new Intent(this, ProfiloActivity.class);
        intentR.putExtra("utente", utente);
        startActivity(intentR);
    }

}
