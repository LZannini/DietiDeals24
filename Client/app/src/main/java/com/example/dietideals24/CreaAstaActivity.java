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
import android.graphics.BitmapFactory;
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

import com.example.dietideals24.enums.Categoria;
import com.example.dietideals24.enums.TipoUtente;
import com.example.dietideals24.models.Asta;
import com.example.dietideals24.models.Utente;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CreaAstaActivity extends AppCompatActivity {

    private Asta asta;
    private ImageView fotoProdotto;
    private EditText nomeProdotto;
    private EditText descrizioneProdotto;
    private AutoCompleteTextView categoriaProdotto;
    private Button btnAvanti;
    private byte[] imageBytes;
    private ImageButton back_button, home_button;
    private Utente utente;
    private boolean fromHome, modificaAvvenuta;
    private List<Asta> listaAste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_asta);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        utente = (Utente) getIntent().getSerializableExtra("utente");
        modificaAvvenuta = getIntent().getBooleanExtra("modificaAvvenuta", false);
        fromHome = getIntent().getBooleanExtra("fromHome", true);
        listaAste = (List<Asta>) getIntent().getSerializableExtra("listaAste");
        asta = (Asta) getIntent().getSerializableExtra("asta");

        fotoProdotto = findViewById(R.id.aggiungi_immagine);
        nomeProdotto = findViewById(R.id.nome_prodotto);
        descrizioneProdotto = findViewById(R.id.descrizione);
        categoriaProdotto = findViewById(R.id.auto_complete_txt);
        btnAvanti = findViewById(R.id.avanti_button);
        back_button = findViewById(R.id.back_button);
        home_button = findViewById(R.id.home_button);

        if(!fromHome) {
            home_button.setVisibility(View.VISIBLE);
        }

        if (asta != null) {
            byte[] fotoBytes = asta.getFoto();
            if (fotoBytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(fotoBytes, 0, fotoBytes.length);
                fotoProdotto.setImageBitmap(bitmap);
            }
            nomeProdotto.setText(asta.getNome());
            descrizioneProdotto.setText(asta.getDescrizione());
            categoriaProdotto.setText(asta.getCategoria().toString().toUpperCase());
        }
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
                if(fromHome) {
                    openActivityHome(utente);
                    finish();
                } else {
                    openActivityAsteCreate();
                }
            }
        });

        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityHome(utente);
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

                if(nomeP.length() == 0 || categoriaP.length() == 0) {
                    builder.setMessage("Bisogna riempire obbligatoriamente i campi nome e categoria!")
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
                        asta = new Asta(utente.getId(), nomeP, descrizioneP, categoria, imageBytes);
                        openActivityTipoAsta(asta, utente.getTipo(), utente);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openActivityHome(utente);
        finish();
    }

    private void openActivityHome(Utente utente) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("utente", utente);
        startActivity(intent);
    }

    public void openActivityTipoAsta(Asta asta, TipoUtente tipoUtente, Utente utente) {
        Intent intentR = new Intent(this, TipoAstaActivity.class);
        intentR.putExtra("asta", asta);
        intentR.putExtra("tipoUtente", tipoUtente);
        intentR.putExtra("utente", utente);
        intentR.putExtra("fromHome", fromHome);
        startActivity(intentR);
    }

    private void openActivityAsteCreate() {
        Intent intent = new Intent(this, AsteCreateActivity.class);
        intent.putExtra("utente", utente);
        intent.putExtra("utente_home", utente);
        intent.putExtra("modificaAvvenuta", modificaAvvenuta);
        if(listaAste != null) {
            intent.putExtra("listaAste", (Serializable) listaAste);
        } else {
            intent.putExtra("listaAste", new ArrayList<>());
        }
        startActivity(intent);
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}