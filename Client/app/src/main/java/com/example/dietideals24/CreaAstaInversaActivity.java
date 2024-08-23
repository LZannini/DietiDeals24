package com.example.dietideals24;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dietideals24.api.ApiService;
import com.example.dietideals24.dto.Asta_InversaDTO;
import com.example.dietideals24.dto.Asta_SilenziosaDTO;
import com.example.dietideals24.dto.UtenteDTO;
import com.example.dietideals24.enums.TipoUtente;
import com.example.dietideals24.models.Asta;
import com.example.dietideals24.models.Utente;
import com.example.dietideals24.retrofit.RetrofitService;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreaAstaInversaActivity extends AppCompatActivity {

    private Utente utente;
    private Asta asta;
    private boolean fromHome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_asta_inversa);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        fromHome = getIntent().getBooleanExtra("fromHome", true);
        asta = (Asta) getIntent().getSerializableExtra("asta");
        utente = (Utente) getIntent().getSerializableExtra("utente");


        DatePicker datePicker = findViewById(R.id.datePicker);
        TimePicker timePicker = findViewById(R.id.timePicker);
        EditText prezzoEditText = findViewById(R.id.prezzoEditText);
        Button createButton = findViewById(R.id.crea_button);
        ImageButton back_button = findViewById(R.id.back_button);
        ImageButton home_button = findViewById(R.id.home_button);

        String valFormattato = NumberFormat.getCurrencyInstance(Locale.ITALY).format(1.0);
        prezzoEditText.setHint(valFormattato);
        prezzoEditText.setText("");
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityTipoAsta(utente, asta);
                finish();
            }
        });

        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityHome(utente);
                finish();
            }
        });

        timePicker.setIs24HourView(true);

        createButton.setOnClickListener(v -> {
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1;
            int year = datePicker.getYear();
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            String scadenza = String.format("%d-%02d-%02d %02d:%02d:00", year, month, day, hour, minute);
            Calendar currentTime = Calendar.getInstance();
            Calendar scadenzaTime = Calendar.getInstance();
            scadenzaTime.set(year, month, day, hour, minute);
            Float prezzo = Float.parseFloat(prezzoEditText.getText().toString());

            if (currentTime.after(scadenzaTime)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreaAstaInversaActivity.this);

                builder.setMessage("Errore, la data di scadenza non è valida.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return;
            }
            else{
                Asta_InversaDTO astaI = new Asta_InversaDTO();
                astaI.setIdCreatore(asta.getId_creatore());
                astaI.setNome(asta.getNome());
                astaI.setDescrizione(asta.getDescrizione());
                astaI.setFoto(asta.getFoto());
                astaI.setCategoria(asta.getCategoria());
                astaI.setScadenza(scadenza);
                astaI.setPrezzo(prezzo);

                ApiService apiService = RetrofitService.getRetrofit(this).create(ApiService.class);

                apiService.creaAstaInversa(astaI)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(CreaAstaInversaActivity.this, "Asta creata con successo!", Toast.LENGTH_SHORT).show();
                                    openActivityHome(utente);
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(CreaAstaInversaActivity.this, "Errore durante la creazione dell'asta!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openActivityTipoAsta(utente, asta);
        finish();
    }

    private void openActivityTipoAsta(Utente utente, Asta asta) {
        Intent intent = new Intent(this, TipoAstaActivity.class);
        intent.putExtra("utente", utente);
        intent.putExtra("tipoUtente", utente.getTipo());
        intent.putExtra("asta", asta);
        intent.putExtra("fromHome", fromHome);
        startActivity(intent);
    }

    private void openActivityHome(Utente utente) {
        Intent intentH = new Intent(this, HomeActivity.class);
        intentH.putExtra("utente", utente);
        startActivity(intentH);
    }
}

