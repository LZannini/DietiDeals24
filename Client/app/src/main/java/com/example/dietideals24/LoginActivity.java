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
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

    private Button btnL;
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView buttonRegistrazione;

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

        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);
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

                UtenteDTO utente = new UtenteDTO();
                utente.setEmail(email);
                utente.setPassword(password);

                apiService.loginUtente(utente)
                        .enqueue(new Callback<UtenteDTO>() {
                            @Override
                            public void onResponse(Call<UtenteDTO> call, Response<UtenteDTO> response) {
                                Toast.makeText(LoginActivity.this, "Login effettuato con successo!", Toast.LENGTH_SHORT);
                                UtenteDTO utenteDTO = response.body();
                                if(utente.getTipo() == TipoUtente.COMPLETO) {
                                    openActivitySceltaAccount();
                                } else {
                                    openActivityHome(utenteDTO);
                                }
                            }

                            @Override
                            public void onFailure(Call<UtenteDTO> call, Throwable t) {
                                Toast.makeText(LoginActivity.this, "Email e/o password non corretti, riprova!", Toast.LENGTH_SHORT);
                                Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
                            }
                        });
            }
        });

    }

    public void openActivityRegistrazione(){
        Intent intentR = new Intent(this, RegistrazioneActivity.class);
        startActivity(intentR);
    }

    public void openActivitySceltaAccount() {
        Intent intentR = new Intent(this, SceltaAccountActivity.class);
        startActivity(intentR);
    }

    public void openActivityHome(UtenteDTO utente) {
        Intent intentR = new Intent(this, HomeActivity.class);
        intentR.putExtra("utente", utente);
        startActivity(intentR);
    }
}