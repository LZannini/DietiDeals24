package com.example.dietideals24;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dietideals24.api.ApiService;
import com.example.dietideals24.dto.UtenteDTO;
import com.example.dietideals24.enums.TipoUtente;
import com.example.dietideals24.models.Utente;
import com.example.dietideals24.retrofit.RetrofitService;
import retrofit2.Callback;

import retrofit2.Call;
import retrofit2.Response;

public class SceltaAccountActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private Utente User;
    private UtenteDTO currentUser;
    private LinearLayout venditoreButt;
    private LinearLayout compratoreButt;
    private LinearLayout completoButt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scelta_account);

        User = (Utente) getIntent().getSerializableExtra("utente");

        venditoreButt = findViewById(R.id.button_vendi);
        compratoreButt = findViewById(R.id.button_compra);
        completoButt = findViewById(R.id.button_completo);
        ImageButton back_button = findViewById(R.id.back_button);


        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityProfilo(User);
                finish();
            }
        });


        venditoreButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change(TipoUtente.VENDITORE);
            }
        });

        compratoreButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change(TipoUtente.COMPRATORE);
            }
        });

        completoButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change(TipoUtente.COMPLETO);
            }
        });

        AggiornaButton();
    }

    @Override
    public void onResume(){
        super.onResume();
        AggiornaButton();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openActivityProfilo(User);
        finish();
    }

    public void openActivityProfilo(Utente utente) {
        Intent intentP = new Intent(this, ProfiloActivity.class);
        intentP.putExtra("utente", utente);
        intentP.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentP);
    }

    private void AggiornaButton(){

        venditoreButt = findViewById(R.id.button_vendi);
        compratoreButt = findViewById(R.id.button_compra);
        completoButt = findViewById(R.id.button_completo);

        venditoreButt.setEnabled(true);
        compratoreButt.setEnabled(true);
        completoButt.setEnabled(true);
        venditoreButt.setAlpha(1.0f);
        compratoreButt.setAlpha(1.0f);
        completoButt.setAlpha(1.0f);

        if(User != null){
            switch (User.getTipo()){
                case VENDITORE:
                    venditoreButt.setEnabled(false);
                    venditoreButt.setAlpha(0.5f);
                    break;
                case COMPRATORE:
                    compratoreButt.setEnabled(false);
                    compratoreButt.setAlpha(0.5f);
                    break;
                case COMPLETO:
                    completoButt.setEnabled(false);
                    completoButt.setAlpha(0.5f);
            }
        }
    }

    private void change(TipoUtente nuovoTipo) {
        builder = new AlertDialog.Builder(SceltaAccountActivity.this);
        builder.setMessage("Sei sicuro di voler cambiare il tuo Tipo di Account?")
                .setCancelable(true)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        User.setTipo(nuovoTipo);
                        currentUser = ConverteDTO(User);
                        aggiornaTipoAccount(currentUser);
                        User = creaUtente(currentUser);
                        AggiornaButton();
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

    private UtenteDTO ConverteDTO(Utente ut){
        UtenteDTO utente = new UtenteDTO();
        utente.setId(ut.getId());
        utente.setUsername(ut.getUsername());
        utente.setEmail(ut.getEmail());
        utente.setPassword(ut.getPassword());
        utente.setBiografia(ut.getBiografia());
        utente.setSitoweb(ut.getSitoweb());
        utente.setPaese(ut.getPaese());
        utente.setTipo(ut.getTipo());
        utente.setAvatar(ut.getAvatar());

        return utente;
    }

    private Utente creaUtente(UtenteDTO u) {
        Utente utente = new Utente();
        utente.setId(u.getId());
        utente.setUsername(u.getUsername());
        utente.setEmail(u.getEmail());
        utente.setPassword(u.getPassword());
        utente.setBiografia(u.getBiografia());
        utente.setSitoweb(u.getSitoweb());
        utente.setPaese(u.getPaese());
        utente.setTipo(u.getTipo());
        utente.setAvatar(u.getAvatar());
        return utente;
    }

    private void aggiornaTipoAccount(UtenteDTO utente) {

        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);
        Call<UtenteDTO> call = apiService.aggiornaUtente(utente);

        call.enqueue(new Callback<UtenteDTO>() {
            @Override
            public void onResponse(@NonNull Call<UtenteDTO> call, @NonNull Response<UtenteDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SceltaAccountActivity.this, "Tipo di account aggiornato con successo", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SceltaAccountActivity.this, "Errore nell'aggiornamento del tipo di account", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UtenteDTO> call, @NonNull Throwable t) {
                // Gestisci errore di connessione
                Toast.makeText(SceltaAccountActivity.this, "Errore di connessione", Toast.LENGTH_SHORT).show();
            }
        });
    }
}