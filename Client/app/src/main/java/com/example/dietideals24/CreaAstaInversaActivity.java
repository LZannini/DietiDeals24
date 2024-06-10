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
import com.example.dietideals24.models.Asta;
import com.example.dietideals24.retrofit.RetrofitService;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreaAstaInversaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_asta_inversa);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Asta asta = (Asta) getIntent().getSerializableExtra("asta");

        DatePicker datePicker = findViewById(R.id.datePicker);
        TimePicker timePicker = findViewById(R.id.timePicker);
        EditText prezzoEditText = findViewById(R.id.prezzoEditText);
        Button createButton = findViewById(R.id.crea_button);
        ImageButton back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        timePicker.setIs24HourView(true);

        createButton.setOnClickListener(v -> {
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            String scadenza = String.format("%02d-%02d-%d %02d:%02d", day, month, year, hour, minute);
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

                RetrofitService retrofitService = new RetrofitService();
                ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);

                apiService.creaAstaInversa(astaI)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(CreaAstaInversaActivity.this, "Asta creata con successo!", Toast.LENGTH_SHORT).show();
                                    finish();
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
}

