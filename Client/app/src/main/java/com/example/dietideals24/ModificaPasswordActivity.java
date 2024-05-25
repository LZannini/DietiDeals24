package com.example.dietideals24;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dietideals24.api.ApiService;
import com.example.dietideals24.dto.UtenteDTO;
import com.example.dietideals24.enums.TipoUtente;
import com.example.dietideals24.models.Utente;
import com.example.dietideals24.retrofit.RetrofitService;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModificaPasswordActivity extends AppCompatActivity {

    private EditText vecchiaPassword;
    private EditText nuovaPassword;
    private EditText confermaPassword;
    private Button salvaButton;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_password);

        int id_utente = (int) getIntent().getSerializableExtra("id");
        String pass_utente = (String) getIntent().getSerializableExtra("pass");

        vecchiaPassword = findViewById(R.id.vecchia_password);
        nuovaPassword = findViewById(R.id.nuova_password);
        confermaPassword = findViewById(R.id.conferma_password);
        salvaButton = findViewById(R.id.salva_button);
        backButton = findViewById(R.id.back_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(ModificaPasswordActivity.this);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        salvaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nuovaPassword.getText().toString().equals(confermaPassword.getText().toString()) && vecchiaPassword.getText().toString().equals(pass_utente)) {
                    try {
                        UtenteDTO utente = new UtenteDTO();
                        utente.setId(id_utente);
                        utente.setPassword(nuovaPassword.getText().toString());

                        RetrofitService retrofitService = new RetrofitService();
                        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);

                        apiService.modificaPassword(utente)
                                .enqueue(new Callback<UtenteDTO>() {
                                    @Override
                                    public void onResponse(Call<UtenteDTO> call, Response<UtenteDTO> response) {
                                        if(response.isSuccessful()) {
                                            UtenteDTO utenteAggiornato = response.body();
                                            Toast.makeText(ModificaPasswordActivity.this, "Password modificata con successo!", Toast.LENGTH_SHORT).show();
                                            openActivityHome(utenteAggiornato);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UtenteDTO> call, Throwable t) {
                                        Toast.makeText(ModificaPasswordActivity.this, "Errore durante la modifica della password, riprova!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else if(!nuovaPassword.getText().toString().equals(confermaPassword.getText().toString())){
                    builder.setMessage("Le password non corrispondono, riprova.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    vecchiaPassword.setText("");
                    nuovaPassword.setText("");
                    confermaPassword.setText("");
                } else if(!vecchiaPassword.getText().toString().equals(pass_utente)) {
                    builder.setMessage("Le password corrente inserita non è corretta, riprova.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    vecchiaPassword.setText("");
                    nuovaPassword.setText("");
                    confermaPassword.setText("");
                }
            }
        });
    }

    public void openActivityHome(UtenteDTO utente) {
        Intent intentR = new Intent(this, HomeActivity.class);
        intentR.putExtra("utente", utente);
        startActivity(intentR);
    }
}