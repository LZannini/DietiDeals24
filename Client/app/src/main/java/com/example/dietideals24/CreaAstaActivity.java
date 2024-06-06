package com.example.dietideals24;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dietideals24.dto.UtenteDTO;
import com.example.dietideals24.enums.Categoria;
import com.example.dietideals24.enums.TipoUtente;
import com.example.dietideals24.models.Asta;
import com.example.dietideals24.models.Utente;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CreaAstaActivity extends AppCompatActivity {

    private ImageView fotoProdotto;
    private EditText nomeProdotto;
    private EditText descrizioneProdotto;
    private AutoCompleteTextView categoriaProdotto;
    private Button btnAvanti;
    private byte[] imageBytes;
    private ImageButton back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_asta);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Utente utente = (Utente) getIntent().getSerializableExtra("utente");


        fotoProdotto = findViewById(R.id.aggiungi_immagine);
        nomeProdotto = findViewById(R.id.nome_prodotto);
        descrizioneProdotto = findViewById(R.id.descrizione);
        categoriaProdotto = findViewById(R.id.categoria);
        btnAvanti = findViewById(R.id.avanti_button);
        back_button = findViewById(R.id.back_button);


        Categoria[] categorieDisponibili = Categoria.values();
        String[] nomiCategorie = new String[categorieDisponibili.length];
        for (int i = 0; i < categorieDisponibili.length; i++) {
            nomiCategorie[i] = categorieDisponibili[i].name();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, nomiCategorie);
        categoriaProdotto.setAdapter(adapter);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fotoProdotto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(CreaAstaActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        btnAvanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomeP = nomeProdotto.getText().toString();
                String descrizioneP = descrizioneProdotto.getText().toString();
                String categoriaP = categoriaProdotto.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(CreaAstaActivity.this);

                if(nomeP.length() == 0 || descrizioneP.length() == 0 || categoriaP.length() == 0) {
                    builder.setMessage("Bisogna riempire tutti i campi!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    nomeProdotto.setText("");
                    descrizioneProdotto.setText("");
                    categoriaProdotto.setText("");
                } else {
                    try {
                        Categoria categoria = Categoria.valueOf(categoriaP.toUpperCase());
                        if (imageBytes == null) {
                            Toast.makeText(CreaAstaActivity.this, "Aggiungi un'immagine del prodotto", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Asta asta = new Asta(utente.getId(), nomeP, descrizioneP, categoria, imageBytes);
                        openActivityTipoAsta(asta, utente.getTipo());
                    } catch (IllegalArgumentException e) {
                        Toast.makeText(CreaAstaActivity.this, "Categoria non valida, inserisci una categoria valida", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            fotoProdotto.setImageURI(uri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                imageBytes = convertBitmapToByteArray(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void openActivityTipoAsta(Asta asta, TipoUtente tipoUtente) {
        Intent intentR = new Intent(this, TipoAstaActivity.class);
        intentR.putExtra("asta", asta);
        intentR.putExtra("tipoUtente", tipoUtente);
        startActivity(intentR);
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}