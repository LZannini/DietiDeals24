package com.example.dietideals24;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dietideals24.api.ApiService;
import com.example.dietideals24.dto.UtenteDTO;
import com.example.dietideals24.enums.TipoUtente;
import com.example.dietideals24.retrofit.RetrofitService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrazioneActivity extends AppCompatActivity {

    private Button btnR;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confPasswordEditText;
    private CheckBox tipoCompratore;
    private CheckBox tipoVenditore;
    private List<TipoUtente> selezioneAccount = new ArrayList<>();
    private TextView buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        usernameEditText = findViewById(R.id.username_input);
        emailEditText = findViewById(R.id.email_input);
        passwordEditText = findViewById(R.id.password_input);
        confPasswordEditText = findViewById(R.id.conferma_password_input);
        tipoCompratore = findViewById(R.id.checkbox_compratore);
        tipoVenditore = findViewById(R.id.checkbox_venditore);

        tipoCompratore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selezioneAccount.add(TipoUtente.COMPRATORE);
                }
            }
        });

        tipoVenditore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selezioneAccount.add(TipoUtente.VENDITORE);
                }
            }
        });



        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        buttonLogin = (TextView) findViewById(R.id.textView_accedi);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityLogin();
            }
        });

        btnR = (Button) findViewById(R.id.registrati_button);

        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);
        btnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confPass = confPasswordEditText.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrazioneActivity.this);

                if (username.length() == 0 || email.length() == 0 || password.length() == 0 || confPass.length() == 0 || selezioneAccount.size() == 0) {
                    builder.setMessage("Bisogna riempire tutti i campi!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    usernameEditText.setText("");
                    emailEditText.setText("");
                    passwordEditText.setText("");
                    confPasswordEditText.setText("");
                    return;
                }

                if (password.equals(confPass)) {
                    try {
                        UtenteDTO utente = new UtenteDTO();
                        utente.setUsername(username);
                        utente.setEmail(email);
                        utente.setPassword(password);

                        if (selezioneAccount.size() == 1) {
                            TipoUtente tipoUtente = selezioneAccount.get(0);
                            utente.setTipo(tipoUtente);
                        } else {
                            utente.setTipo(TipoUtente.COMPLETO);
                        }

                        apiService.registraUtente(utente)
                                .enqueue(new Callback<UtenteDTO>() {
                                    @Override
                                    public void onResponse(Call<UtenteDTO> call, Response<UtenteDTO> response) {
                                        Toast.makeText(RegistrazioneActivity.this, "Registrazione effettuata con successo!", Toast.LENGTH_SHORT);
                                    }

                                    @Override
                                    public void onFailure(Call<UtenteDTO> call, Throwable t) {
                                        Toast.makeText(RegistrazioneActivity.this, "Errore durante la registrazione!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                }
            }
        });

    }

    public void openActivityLogin(){
        Intent intentR = new Intent(this, LoginActivity.class);
        startActivity(intentR);
    }
}