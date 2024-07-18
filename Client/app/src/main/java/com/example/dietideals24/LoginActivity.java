package com.example.dietideals24;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dietideals24.api.ApiService;
import com.example.dietideals24.dto.UtenteDTO;
import com.example.dietideals24.enums.TipoUtente;
import com.example.dietideals24.models.Utente;
import com.example.dietideals24.retrofit.RetrofitService;
import com.example.dietideals24.security.JwtAuthenticationResponse;
import com.example.dietideals24.security.TokenManager;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button btnL;
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView buttonRegistrazione;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email_input);
        passwordEditText = findViewById(R.id.password_input);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        buttonRegistrazione = (TextView) findViewById(R.id.textView_registrati);
        buttonRegistrazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityRegistrazione();
            }
        });
        btnL = (Button) findViewById(R.id.login_button);

        apiService = RetrofitService.getInstance(this).getRetrofit(this).create(ApiService.class);
        btnL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                if (email.length() == 0 || password.length() == 0) {
                    builder.setMessage("Bisogna riempire tutti i campi!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    emailEditText.setText("");
                    passwordEditText.setText("");
                    return;
                }

                TokenManager tokenManager = new TokenManager(LoginActivity.this);

                UtenteDTO utente = new UtenteDTO();
                utente.setEmail(email);
                utente.setPassword(password);

                apiService.loginUtente(utente)
                        .enqueue(new Callback<JwtAuthenticationResponse>() {
                            @Override
                            public void onResponse(Call<JwtAuthenticationResponse> call, Response<JwtAuthenticationResponse> response) {
                                if(response.isSuccessful()) {
                                    JwtAuthenticationResponse authResponse = response.body();
                                    if (authResponse != null && authResponse.getAccessToken() != null) {
                                        tokenManager.saveToken(authResponse.getAccessToken());

                                        RetrofitService.resetInstance();
                                        apiService = RetrofitService.getInstance(LoginActivity.this).getRetrofit(LoginActivity.this).create(ApiService.class);

                                        recuperaDatiUtente();
                                    }else {
                                        Toast.makeText(LoginActivity.this, "Token non valido ricevuto", Toast.LENGTH_SHORT).show();
                                        }
                                } else if(response.code() == 401) {
                                    Toast.makeText(LoginActivity.this, "Email e/o password non corretti, riprova!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<JwtAuthenticationResponse> call, Throwable t) {
                                Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
                            }
                        });
            }
        });

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void openActivityRegistrazione(){
        Intent intentR = new Intent(this, RegistrazioneActivity.class);
        startActivity(intentR);
    }

    public void openActivityHome(Utente utente) {
        Intent intentR = new Intent(this, HomeActivity.class);
        intentR.putExtra("utente", utente);
        startActivity(intentR);
    }

    private void recuperaDatiUtente() {
        TokenManager tokenManager = new TokenManager(LoginActivity.this);
        int userId = tokenManager.getUserIdFromToken();

        if (userId != -1) {

            apiService.recuperaUtente(userId)
                    .enqueue(new Callback<UtenteDTO>() {
                        @Override
                        public void onResponse(Call<UtenteDTO> call, Response<UtenteDTO> response) {

                            if(response.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login effettuato con successo", Toast.LENGTH_SHORT).show();
                                UtenteDTO utenteDTO = response.body();
                                Utente utente_intent = creaUtente(utenteDTO);
                                openActivityHome(utente_intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Errore nel recupero dei dati utente", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UtenteDTO> call, Throwable t) {
                            Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, "Errore nel recupero dei dati utente", t);
                        }
                    });
        } else {
            Toast.makeText(LoginActivity.this, "Errore nell'estrazione dell'ID utente dal token", Toast.LENGTH_SHORT).show();
        }
    }

    private Utente creaUtente(UtenteDTO utenteDTO) {
        Utente utente = new Utente();
        utente.setId(utenteDTO.getId());
        utente.setUsername(utenteDTO.getUsername());
        utente.setEmail(utenteDTO.getEmail());
        utente.setPassword(utenteDTO.getPassword());
        utente.setBiografia(utenteDTO.getBiografia());
        utente.setSitoweb(utenteDTO.getSitoweb());
        utente.setPaese(utenteDTO.getPaese());
        utente.setTipo(utenteDTO.getTipo());
        utente.setAvatar(utenteDTO.getAvatar());
        return utente;
    }
}