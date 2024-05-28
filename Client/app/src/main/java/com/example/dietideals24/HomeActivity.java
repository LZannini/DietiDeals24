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
import android.widget.LinearLayout;

import com.example.dietideals24.dto.UtenteDTO;
import com.example.dietideals24.models.Utente;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout buttonCrea;
    private LinearLayout buttonCerca;
    private LinearLayout buttonProfilo;
    private LinearLayout buttonDisconnetti;
    private AlertDialog.Builder builder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        UtenteDTO utenteDTO = (UtenteDTO) getIntent().getSerializableExtra("utente");
        Utente utente = creaUtenteLoggato(utenteDTO);


        buttonCrea = findViewById(R.id.button_crea);
        buttonCerca = findViewById(R.id.button_cerca);
        buttonProfilo = findViewById(R.id.button_profilo);
        buttonDisconnetti = findViewById(R.id.button_disconnetti);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();



        buttonCrea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityCreaAsta();
            }
        });

        buttonCerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityCercaAsta();
            }
        });

        buttonProfilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityProfilo(utente);
            }
        });

        buttonDisconnetti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("Sei sicuro di voler uscire?")
                        .setCancelable(true)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Context ctx = getApplicationContext();
                                PackageManager pm = ctx.getPackageManager();
                                Intent intent = pm.getLaunchIntentForPackage(ctx.getPackageName());
                                Intent mainIntent = Intent.makeRestartActivityTask(intent.getComponent());
                                ctx.startActivity(mainIntent);
                                Runtime.getRuntime().exit(0);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        })
                        .show();
            }
        });
    }

    public Utente creaUtenteLoggato(UtenteDTO utenteDTO) {
        Utente u = new Utente();
        u.setId(utenteDTO.getId());
        u.setUsername(utenteDTO.getUsername());
        u.setEmail(utenteDTO.getEmail());
        u.setPassword(utenteDTO.getPassword());
        u.setTipo(utenteDTO.getTipo());
        if(utenteDTO.getAvatar() != null) u.setAvatar(utenteDTO.getAvatar());
        if(utenteDTO.getBiografia() != null) u.setBiografia(utenteDTO.getBiografia());
        if(utenteDTO.getPaese() != null) u.setPaese(utenteDTO.getPaese());
        if(utenteDTO.getSitoweb() != null) u.setSitoweb(utenteDTO.getSitoweb());
        return u;
    }

    public void openActivityCreaAsta() {
        Intent intentR = new Intent(this, CreaAstaActivity.class);
        startActivity(intentR);
    }

    public void openActivityCercaAsta() {
        Intent intentR = new Intent(this, CercaAstaActivity.class);
        startActivity(intentR);
    }

    public void openActivityProfilo(Utente utente) {
        Intent intentR = new Intent(this, ProfiloActivity.class);
        intentR.putExtra("utente", utente);
        startActivity(intentR);
    }

}