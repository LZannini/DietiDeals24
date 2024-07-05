package com.example.dietideals24;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.example.dietideals24.dto.AstaDTO;
import com.example.dietideals24.dto.UtenteDTO;
import com.example.dietideals24.models.Asta;
import com.example.dietideals24.models.Asta_Inversa;
import com.example.dietideals24.models.Asta_Ribasso;
import com.example.dietideals24.models.Asta_Silenziosa;
import com.example.dietideals24.models.Utente;
import com.example.dietideals24.retrofit.RetrofitService;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textview.MaterialTextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfiloActivity extends AppCompatActivity {

    private ImageView menuButton;
    private ImageView avatarSelector;
    private EditText emailEditText;
    private EditText bioEditText;
    private EditText webSiteEditText;
    private EditText countryEditText;
    private MaterialTextView textUsername;
    private LinearLayout pulsantiAste;
    private Button buttonSalva, buttonAsteCreate;
    private Utente utenteOriginale;
    private Utente utenteModificato;
    private Boolean info_mod = false;
    private byte[] imageBytes;
    private boolean fromDettagli;
    private boolean modificaAvvenuta;
    private ImageButton back_button;

    @SuppressLint({"SuspiciousIndentation", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        fromDettagli = getIntent().getBooleanExtra("fromDettagli", false);

        menuButton = findViewById(R.id.icona_menu);
        avatarSelector = findViewById(R.id.foto_profilo);
        emailEditText = findViewById(R.id.email);
        bioEditText = findViewById(R.id.shortBio);
        webSiteEditText = findViewById(R.id.sito);
        countryEditText = findViewById(R.id.paese);
        pulsantiAste = findViewById(R.id.pulsanti_aste);
        buttonSalva = findViewById(R.id.salva_button);
        back_button = findViewById(R.id.back_button);
        buttonAsteCreate = findViewById(R.id.asteCreate_button);
        textUsername = findViewById((R.id.text_nomeProfilo));

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        if(fromDettagli) {
            utenteModificato = (Utente) getIntent().getSerializableExtra("utente_home");
            utenteOriginale = (Utente) getIntent().getSerializableExtra("utente");
            menuButton.setVisibility(View.INVISIBLE);
        } else {
            modificaAvvenuta = getIntent().getBooleanExtra("modificaAvvenuta", modificaAvvenuta);
            if (modificaAvvenuta) {
                utenteModificato = (Utente) getIntent().getSerializableExtra("utente");
                utenteOriginale = utenteModificato;
            } else {
                utenteOriginale = (Utente) getIntent().getSerializableExtra("utente");
                utenteModificato = utenteOriginale;
            }
        }

        if(utenteOriginale.getAvatar() != null) {
            Bitmap avatarBitmap = BitmapFactory.decodeByteArray(utenteOriginale.getAvatar(), 0, utenteOriginale.getAvatar().length);
            avatarSelector.setImageBitmap(avatarBitmap);
        }

        textUsername.setText(utenteOriginale.getUsername());
        emailEditText.setText(utenteOriginale.getEmail());
        bioEditText.setText(utenteOriginale.getBiografia());
        webSiteEditText.setText(utenteOriginale.getSitoweb());
        countryEditText.setText(utenteOriginale.getPaese());

        buttonAsteCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitService retrofitService = new RetrofitService();
                ApiService apiService = retrofitService.getRetrofit().create(ApiService.class);
                trovaAsteCreate(apiService);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fromDettagli)
                    openActivityHome();
                finish();
            }
        });

        if(!fromDettagli)
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

                    utenteModificato = new Utente();

                    utenteModificato.setId(utenteOriginale.getId());
                    utenteModificato.setPassword(utenteOriginale.getPassword());
                    utenteModificato.setTipo(utenteOriginale.getTipo());
                    if (imageBytes != null) {
                        utenteModificato.setAvatar(imageBytes);
                        info_mod = true;
                    } else if(utenteOriginale.getAvatar() != null){
                        utenteModificato.setAvatar(utenteOriginale.getAvatar());
                    }
                    if (!textUsername.getText().toString().equals(utenteOriginale.getUsername())) {
                        utenteModificato.setUsername(textUsername.getText().toString());
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

                    UtenteDTO utenteModificatoDTO = creaUtenteDTO(utenteModificato);
                    apiService.aggiornaUtente(utenteModificatoDTO)
                            .enqueue(new Callback<UtenteDTO>() {
                                @Override
                                public void onResponse(Call<UtenteDTO> call, Response<UtenteDTO> response) {
                                    if(response.isSuccessful()) {
                                        UtenteDTO utenteRicevuto = response.body();
                                        if (utenteRicevuto != null && utenteRicevuto.getAvatar() != null) {
                                            Bitmap avatarBitmap = BitmapFactory.decodeByteArray(utenteRicevuto.getAvatar(), 0, utenteRicevuto.getAvatar().length);
                                            avatarSelector.setImageBitmap(avatarBitmap);
                                        }

                                        textUsername.setText(utenteModificato.getUsername());
                                        emailEditText.setText(utenteModificato.getEmail());
                                        bioEditText.setText(utenteModificato.getBiografia());
                                        webSiteEditText.setText(utenteModificato.getSitoweb());
                                        countryEditText.setText(utenteModificato.getPaese());

                                        textUsername.setEnabled(false);
                                        emailEditText.setEnabled(false);
                                        bioEditText.setEnabled(false);
                                        webSiteEditText.setEnabled(false);
                                        countryEditText.setEnabled(false);
                                        buttonSalva.setVisibility(View.INVISIBLE);
                                        pulsantiAste.setVisibility(View.VISIBLE);
                                        utenteOriginale = utenteModificato;

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
                        textUsername.setEnabled(true);
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

                        pulsantiAste.setVisibility(View.GONE);
                        buttonSalva.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.action_change_password:
                        openActivityModificaPassword(utente);
                        return true;
                    case R.id.action_switch_account:
                        openActivitySceltaAccount(utente);
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
        super.onBackPressed();
        if (!fromDettagli)
            openActivityHome();
        finish();
    }

    private UtenteDTO creaUtenteDTO(Utente u) {
        UtenteDTO utente = new UtenteDTO();
        utente.setId(u.getId());
        utente.setUsername(u.getUsername());
        utente.setEmail(u.getEmail());
        utente.setPassword(u.getPassword());
        utente.setBiografia(u.getBiografia());
        utente.setSitoweb(u.getSitoweb());
        utente.setPaese(u.getPaese());
        utente.setTipo(u.getTipo());
        utente.setAvatar(u.getAvatar());
        return utente;
    }

    private void openActivityHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("utente", utenteModificato);
        startActivity(intent);
        finish();
    }

    private void openActivityDettagliAsta() {
        Intent intentR = new Intent(this, DettagliAstaActivity.class);
        intentR.putExtra("utente_home", utenteModificato);
        intentR.putExtra("utente", utenteOriginale);
        startActivity(intentR);
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
        intentR.putExtra("utente", utente);
        startActivity(intentR);
    }

    private void openActivitySceltaAccount(Utente utente) {
        Intent intentR = new Intent(this, SceltaAccountActivity.class);
        intentR.putExtra("utente", utente);
        intentR.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentR);
    }

    private void trovaAsteCreate(ApiService apiService) {
        Call<List<AstaDTO>> call;

        call = apiService.cercaPerUtente(utenteOriginale.getId());
        call.enqueue(new Callback<List<AstaDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<AstaDTO>> call, @NonNull Response<List<AstaDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<AstaDTO> asteResponse = response.body();
                    List<Asta> asteList = creaListaModelloAsta(asteResponse);
                    List<Asta> aste = new ArrayList<>();
                    for (Asta a : asteList) {
                        aste.add(a);
                    }
                    Intent intent = new Intent(ProfiloActivity.this, AsteCreateActivity.class);
                    intent.putExtra("listaAste", (Serializable) aste);
                    intent.putExtra("utente_home", utenteModificato);
                    intent.putExtra("utente", utenteOriginale);
                    intent.putExtra("fromDettagli", fromDettagli);
                    intent.putExtra("modificaAvvenuta", info_mod);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(ProfiloActivity.this, AsteCreateActivity.class);
                    intent.putExtra("listaAste", new ArrayList<Asta>());
                    intent.putExtra("utente_home", utenteModificato);
                    intent.putExtra("utente", utenteOriginale);
                    intent.putExtra("fromDettagli", fromDettagli);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<AstaDTO>> call, @NonNull Throwable t) {
                Toast.makeText(ProfiloActivity.this, "Errore di Connessione", Toast.LENGTH_SHORT).show();
                Logger.getLogger(ProfiloActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
            }
        });
    }

    public Asta_Ribasso creaModelloAstaR(AstaDTO dto) {
        Asta_Ribasso asta = new Asta_Ribasso();
        asta.setId(dto.getID());
        asta.setId_creatore(dto.getId_creatore());
        asta.setCategoria(dto.getCategoria());
        asta.setFoto(dto.getFoto());
        asta.setNome(dto.getNome());
        asta.setDescrizione(dto.getDescrizione());

        return asta;
    }

    public Asta_Silenziosa creaModelloAstaS(AstaDTO dto) {
        Asta_Silenziosa asta = new Asta_Silenziosa();
        asta.setId(dto.getID());
        asta.setId_creatore(dto.getId_creatore());
        asta.setCategoria(dto.getCategoria());
        asta.setFoto(dto.getFoto());
        asta.setNome(dto.getNome());
        asta.setDescrizione(dto.getDescrizione());

        return asta;
    }

    public Asta_Inversa creaModelloAstaI(AstaDTO dto) {
        Asta_Inversa asta = new Asta_Inversa();
        asta.setId(dto.getID());
        asta.setId_creatore(dto.getId_creatore());
        asta.setCategoria(dto.getCategoria());
        asta.setFoto(dto.getFoto());
        asta.setNome(dto.getNome());
        asta.setDescrizione(dto.getDescrizione());

        return asta;
    }

    public List<Asta> creaListaModelloAsta(List<AstaDTO> listaDto) {
        List<Asta> asteList = new ArrayList<>();
        for (AstaDTO dto : listaDto) {
            if (dto.getTipo().equals("RIBASSO")) {
                asteList.add(creaModelloAstaR(dto));
            } else if (dto.getTipo().equals("SILENZIOSA")) {
                asteList.add(creaModelloAstaS(dto));
            } else if (dto.getTipo().equals("INVERSA")) {
                asteList.add(creaModelloAstaI(dto));
            }
        }
        return asteList;
    }
}