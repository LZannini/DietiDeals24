package com.example.dietideals24;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dietideals24.api.ApiService;
import com.example.dietideals24.dto.UtenteDTO;
import com.example.dietideals24.enums.TipoUtente;
import com.example.dietideals24.models.Utente;
import com.example.dietideals24.retrofit.RetrofitService;
import com.example.dietideals24.security.JwtAuthenticationResponse;
import com.example.dietideals24.security.TokenManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private Button btnL;
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView buttonRegistrazione;
    private ImageView buttonGoogle;

    private ApiService apiService;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email_input);
        passwordEditText = findViewById(R.id.password_input);

        tokenManager = new TokenManager(this);
        tokenManager.deleteToken();

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        buttonRegistrazione = (TextView) findViewById(R.id.textView_registrati);
        buttonRegistrazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityRegistrazione();
            }
        });
        btnL = (Button) findViewById(R.id.login_button);
        buttonGoogle = (ImageView) findViewById(R.id.google_button);

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

                tokenManager = new TokenManager(LoginActivity.this);

                UtenteDTO utente = new UtenteDTO();
                utente.setEmail(email);
                utente.setPassword(password);

                apiService.loginUtente(utente)
                        .enqueue(new Callback<JwtAuthenticationResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<JwtAuthenticationResponse> call, @NonNull Response<JwtAuthenticationResponse> response) {
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
                            public void onFailure(@NonNull Call<JwtAuthenticationResponse> call, @NonNull Throwable t) {
                                Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
                            }
                        });
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accediConGoogle();
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

    public void openActivitySceltaAccount(Utente utente) {
        Intent intentR = new Intent(this, SceltaAccountActivity.class);
        intentR.putExtra("utente", utente);
        intentR.putExtra("fromLogin", true);
        Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, "ciao");
        intentR.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentR);
    }

    private void recuperaDatiUtente() {
        TokenManager tokenManager = new TokenManager(LoginActivity.this);
        int userId = tokenManager.getUserIdFromToken();

        if (userId != -1) {

            apiService.recuperaUtente(userId)
                    .enqueue(new Callback<UtenteDTO>() {
                        @Override
                        public void onResponse(@NonNull Call<UtenteDTO> call, @NonNull Response<UtenteDTO> response) {

                            if(response.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login effettuato con successo", Toast.LENGTH_SHORT).show();
                                UtenteDTO utenteDTO = response.body();
                                Utente utente_intent = creaUtente(utenteDTO);
                                if (utente_intent.getTipo().toString().equals(TipoUtente.NESSUNO.toString())) {
                                    openActivitySceltaAccount(utente_intent);
                                } else {
                                    openActivityHome(utente_intent);
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Errore nel recupero dei dati utente", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<UtenteDTO> call, @NonNull Throwable t) {
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
        utente.setAvatar(utenteDTO.getAvatar());
        utente.setTipo(utenteDTO.getTipo());

        return utente;
    }

    private void accediConGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    String idToken = account.getIdToken();
                    impostaToken(idToken);
                }
            } catch (ApiException e) {
                Log.w("LoginActivity", "signInResult:failed code=" + e.getStatusCode());
                Toast.makeText(this, "Sign-in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void impostaToken(String idToken) {
        apiService.loginGoogle(idToken).enqueue(new Callback<JwtAuthenticationResponse>() {
            @Override
            public void onResponse(@NonNull Call<JwtAuthenticationResponse> call, @NonNull Response<JwtAuthenticationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JwtAuthenticationResponse authResponse = response.body();
                    TokenManager tokenManager = new TokenManager(LoginActivity.this);
                    tokenManager.saveToken(authResponse.getAccessToken());
                    RetrofitService.resetInstance();
                    apiService = RetrofitService.getInstance(LoginActivity.this).getRetrofit(LoginActivity.this).create(ApiService.class);

                    recuperaDatiUtente();
                } else {
                    Toast.makeText(LoginActivity.this, "Login with Google failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JwtAuthenticationResponse> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }


}