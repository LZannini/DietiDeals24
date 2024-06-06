package com.example.dietideals24;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.dietideals24.api.ApiService;
import com.example.dietideals24.dto.UtenteDTO;
import com.example.dietideals24.models.Utente;
import com.example.dietideals24.retrofit.RetrofitService;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfiloActivity extends AppCompatActivity {

    private ImageView menuButton;
    private ImageView avatarSelector;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText bioEditText;
    private EditText webSiteEditText;
    private EditText countryEditText;
    private LinearLayout pulsantiAste;
    private Button buttonSalva;
    private Utente utenteOriginale;
    private UtenteDTO utenteModificato;
    private Boolean info_mod = false;
    private byte[] imageBytes;

    private ImageButton back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        utenteOriginale = (Utente) getIntent().getSerializableExtra("utente");

        menuButton = findViewById(R.id.icona_menu);
        avatarSelector = findViewById(R.id.foto_profilo);
        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        bioEditText = findViewById(R.id.shortBio);
        webSiteEditText = findViewById(R.id.sito);
        countryEditText = findViewById(R.id.paese);
        pulsantiAste = findViewById(R.id.pulsanti_aste);
        buttonSalva = findViewById(R.id.salva_button);
        back_button = findViewById(R.id.back_button);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        if(utenteOriginale.getAvatar() != null) {
            Bitmap avatarBitmap = BitmapFactory.decodeByteArray(utenteOriginale.getAvatar(), 0, utenteOriginale.getAvatar().length);
            avatarSelector.setImageBitmap(avatarBitmap);
        }
        usernameEditText.setText(utenteOriginale.getUsername());
        emailEditText.setText(utenteOriginale.getEmail());
        bioEditText.setText(utenteOriginale.getBiografia());
        webSiteEditText.setText(utenteOriginale.getSitoweb());
        countryEditText.setText(utenteOriginale.getPaese());

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuProfilo(v, utenteOriginale);
            }
        });

        buttonSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitService retrofitService = new RetrofitService();
                ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);

                utenteModificato = new UtenteDTO();

                utenteModificato.setId(utenteOriginale.getId());
                utenteModificato.setPassword(utenteOriginale.getPassword());
                utenteModificato.setTipo(utenteOriginale.getTipo());
                if (imageBytes != null) {
                    utenteModificato.setAvatar(imageBytes);
                    info_mod = true;
                } else if(utenteOriginale.getAvatar() != null){
                    utenteModificato.setAvatar(utenteOriginale.getAvatar());
                }
                if (!usernameEditText.getText().toString().equals(utenteOriginale.getUsername())) {
                    utenteModificato.setUsername(usernameEditText.getText().toString());
                    info_mod = true;
                } else {
                    utenteModificato.setUsername(utenteOriginale.getUsername());
                }
                if (!emailEditText.getText().toString().equals(utenteOriginale.getEmail())) {
                    utenteModificato.setEmail(emailEditText.getText().toString());
                    info_mod = true;
                } else {
                    utenteModificato.setEmail(utenteOriginale.getEmail());
                }
                if (!bioEditText.getText().toString().equals(utenteOriginale.getBiografia()) && !bioEditText.getText().toString().equals("")) {
                    utenteModificato.setBiografia(bioEditText.getText().toString());
                    info_mod = true;
                } else if(utenteOriginale.getBiografia() != null) {
                    utenteModificato.setBiografia(utenteOriginale.getBiografia());
                }
                if (!webSiteEditText.getText().toString().equals(utenteOriginale.getSitoweb()) && !webSiteEditText.getText().toString().equals("")) {
                    utenteModificato.setSitoweb(webSiteEditText.getText().toString());
                    info_mod = true;
                } else if(utenteOriginale.getSitoweb() != null) {
                    utenteModificato.setSitoweb(utenteOriginale.getSitoweb());
                }
                if (!countryEditText.getText().toString().equals(utenteOriginale.getPaese()) && !countryEditText.getText().toString().equals("")) {
                    utenteModificato.setPaese(countryEditText.getText().toString());
                    info_mod = true;
                } else if(utenteOriginale.getPaese() != null) {
                    utenteModificato.setPaese(utenteOriginale.getPaese());
                }

                apiService.aggiornaUtente(utenteModificato)
                        .enqueue(new Callback<UtenteDTO>() {
                            @Override
                            public void onResponse(Call<UtenteDTO> call, Response<UtenteDTO> response) {
                                if(response.isSuccessful()) {
                                    UtenteDTO utenteRicevuto = response.body();
                                    if (utenteRicevuto != null && utenteRicevuto.getAvatar() != null) {
                                        Bitmap avatarBitmap = BitmapFactory.decodeByteArray(utenteRicevuto.getAvatar(), 0, utenteRicevuto.getAvatar().length);
                                        avatarSelector.setImageBitmap(avatarBitmap);
                                    }

                                    usernameEditText.setText(utenteModificato.getUsername());
                                    emailEditText.setText(utenteModificato.getEmail());
                                    bioEditText.setText(utenteModificato.getBiografia());
                                    webSiteEditText.setText(utenteModificato.getSitoweb());
                                    countryEditText.setText(utenteModificato.getPaese());

                                    usernameEditText.setEnabled(false);
                                    emailEditText.setEnabled(false);
                                    bioEditText.setEnabled(false);
                                    webSiteEditText.setEnabled(false);
                                    countryEditText.setEnabled(false);
                                    buttonSalva.setVisibility(View.INVISIBLE);
                                    pulsantiAste.setVisibility(View.VISIBLE);

                                    Toast.makeText(ProfiloActivity.this, "Modifica effettuata con successo!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ProfiloActivity.this, "Errore durante la modifica dei dati, riprova.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<UtenteDTO> call, Throwable t) {
                                Toast.makeText(ProfiloActivity.this, "Errore di connessione", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void openMenuProfilo(View view, Utente utente) {
        PopupMenu popup = new PopupMenu(this, view);

        popup.getMenuInflater().inflate(R.menu.menu_profilo, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit_profile:
                        usernameEditText.setEnabled(true);
                        emailEditText.setEnabled(true);
                        bioEditText.setEnabled(true);
                        webSiteEditText.setEnabled(true);
                        countryEditText.setEnabled(true);
                        avatarSelector.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ImagePicker.with(ProfiloActivity.this)
                                        .crop()	    			//Crop image(Optional), Check Customization for more option
                                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                                        .start();
                            }
                        });

                        pulsantiAste.setVisibility(View.INVISIBLE);
                        buttonSalva.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.action_change_password:
                        openActivityModificaPassword(utente);
                        return true;
                    case R.id.action_switch_account:
                        openActivitySceltaAccount();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();;
    }


    @Override
    public void onBackPressed() {
        if(info_mod == true) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("utente", utenteModificato);

            startActivity(intent);

            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            avatarSelector.setImageURI(uri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                imageBytes = convertBitmapToByteArray(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }


    private void openActivityModificaPassword(Utente utente) {
        Intent intentR = new Intent(this, ModificaPasswordActivity.class);
        intentR.putExtra("id", utente.getId());
        intentR.putExtra("pass", utente.getPassword());
        startActivity(intentR);
    }

    private void openActivitySceltaAccount() {
        Intent intentR = new Intent(this, SceltaAccountActivity.class);
        startActivity(intentR);
    }
}