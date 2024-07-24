package com.example.dietideals24;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dietideals24.api.ApiService;
import com.example.dietideals24.dto.AstaDTO;
import com.example.dietideals24.dto.Asta_InversaDTO;
import com.example.dietideals24.dto.Asta_RibassoDTO;
import com.example.dietideals24.dto.Asta_SilenziosaDTO;
import com.example.dietideals24.dto.NotificaDTO;
import com.example.dietideals24.dto.UtenteDTO;
import com.example.dietideals24.models.Asta;
import com.example.dietideals24.models.Asta_Inversa;
import com.example.dietideals24.models.Asta_Ribasso;
import com.example.dietideals24.models.Asta_Silenziosa;
import com.example.dietideals24.models.Utente;

import com.example.dietideals24.retrofit.RetrofitService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout buttonCrea;
    private LinearLayout buttonCerca;
    private LinearLayout buttonProfilo;
    private Button buttonNotifica;
    private LinearLayout buttonDisconnetti;
    private AlertDialog.Builder builder;
    private Utente utente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        utente = (Utente) getIntent().getSerializableExtra("utente");

        buttonCrea = findViewById(R.id.button_crea);
        buttonCerca = findViewById(R.id.button_cerca);
        buttonProfilo = findViewById(R.id.button_profilo);
        buttonNotifica = findViewById(R.id.button_notifica);
        buttonDisconnetti = findViewById(R.id.button_disconnetti);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        buttonCrea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityCreaAsta(utente);
            }
        });

        buttonCerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityCercaAsta(utente);
            }
        });

        buttonProfilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityProfilo(utente);
            }
        });


        ApiService apiService = RetrofitService.getRetrofit(this).create(ApiService.class);


        checkNotifiche(apiService);

        buttonNotifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperaNotifiche(apiService);
            }
        });

        buttonDisconnetti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disconnect();
            }
        });
    }

    private void setVisibilityRedNot(boolean pallino){
        View red_not = findViewById(R.id.pallino_notifiche);

        if(pallino) red_not.setVisibility(View.VISIBLE);
        else red_not.setVisibility(View.GONE);
    }

    private void checkNotifiche(ApiService apiService) {
        Call<List<NotificaDTO>> call;

        call = apiService.mostraNotifiche(utente.getId());

        call.enqueue(new Callback<List<NotificaDTO>>() {
            @Override
            public void onResponse(Call<List<NotificaDTO>> call, Response<List<NotificaDTO>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<NotificaDTO> notifiche = response.body();
                    boolean nonLetta = false;

                    for(NotificaDTO notifica: notifiche){
                        if(!notifica.isLetta()) {
                            nonLetta = true;
                            break;
                        }
                    }
                    setVisibilityRedNot(!notifiche.isEmpty() && nonLetta);
                } else
                    Logger.getLogger(HomeActivity.class.getName()).log(Level.WARNING, "Notifiche vuote");
            }

            @Override
            public void onFailure(Call<List<NotificaDTO>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Errore durante il caricamento delle notifiche, riprova!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
            }
        });
    }


    private void recuperaNotifiche(ApiService apiService) {
        Call<List<NotificaDTO>> call;

        call = apiService.mostraNotifiche(utente.getId());

        call.enqueue(new Callback<List<NotificaDTO>>() {
            @Override
            public void onResponse(Call<List<NotificaDTO>> call, Response<List<NotificaDTO>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<NotificaDTO> notifiche = response.body();
                   /* for(NotificaDTO notifica : notifiche){
                        RecuperaAsta(notifica,apiService);
                    }*/
                    Intent intent = new Intent(HomeActivity.this, NotificaActivity.class);
                    intent.putExtra("listaNotifiche", (Serializable) notifiche);
                    intent.putExtra("utente", utente);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(HomeActivity.this, NotificaActivity.class);
                    intent.putExtra("listaNotifiche", new ArrayList<NotificaDTO>());
                    intent.putExtra("utente", utente);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<List<NotificaDTO>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Errore durante il caricamento delle notifiche, riprova!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
            }
        });
    }

    /*
    public void RecuperaAsta(NotificaDTO notifica,ApiService apiService) {

        int id_asta = notifica.getId_Asta();
        Log.d("RecuperaAsta", "RecuperaAsta chiamato per id_asta: " + id_asta);

        Call<AstaDTO> callAsta = apiService.recuperaAsta(id_asta);

        callAsta.enqueue(new Callback<AstaDTO>() {
            @Override
            public void onResponse(Call<AstaDTO> call, Response<AstaDTO> response) {
                if(response.isSuccessful() && response.body() != null) {
                    AstaDTO astadto = response.body();
                        Asta asta = converteToModel(astadto);
                        Log.d("RecuperaAsta", "Tipo asta: " + asta.getClass().toString());

                        if (asta instanceof Asta_Inversa) {
                            Log.d("RecuperaAsta", "Asta di tipo Inversa");
                            Asta_Inversa inversa = (Asta_Inversa) asta;
                            recuperaAstaInversa(id_asta, notifica, apiService);
                        } else if (asta instanceof Asta_Ribasso){
                            Log.d("RecuperaAsta", "Asta di tipo Ribasso");
                            Asta_Ribasso ribasso = (Asta_Ribasso) asta;
                            recuperaAstaRibasso(id_asta, notifica, apiService);
                    }else if (asta instanceof Asta_Silenziosa) {
                            Log.d("RecuperaAsta", "Asta di tipo Silenziosa");
                            Asta_Silenziosa silenziosa = (Asta_Silenziosa) asta;
                            recuperaAstaSilenziosa(id_asta, notifica, apiService);
                        } else
                            Log.d("RecuperaAsta", "Asta di tipo Sconosciuta");
                    }
                }


            @Override
            public void onFailure(Call<AstaDTO> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Errore di Connessione", Toast.LENGTH_SHORT).show();
                Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
            }
        });
    }

    public void recuperaAstaSilenziosa(int id_asta, NotificaDTO notifica,ApiService apiService){
        Call<Asta_SilenziosaDTO> callS = apiService.recuperaDettagliAstaSilenziosa(id_asta);
        callS.enqueue(new Callback<Asta_SilenziosaDTO>() {
                          @Override
                          public void onResponse(Call<Asta_SilenziosaDTO> call, Response<Asta_SilenziosaDTO> response) {
                              if(response.isSuccessful() && response.body() != null){
                                  Asta_SilenziosaDTO astaS = response.body();
                                  if(astaS != null) {
                                      String nome_asta = astaS.getNome();
                                      Log.d("RecuperaAsta", "Nome Asta_Silenziosa recuperato: " + nome_asta);

                                      notifica.setNome_asta(astaS.getNome());
                                      Log.d("RecuperaAsta", "Nome Asta_Silenziosa Nelle Notifiche recuperato: " + notifica.getNome_asta());
                                  }
                                  else Log.d("RecuperaAsta","Asta Silenziosa null");
                              }
                              else
                                  Toast.makeText(HomeActivity.this, "Asta Silenziosa non trovata", Toast.LENGTH_SHORT).show();
                          }

                          @Override
                          public void onFailure(Call<Asta_SilenziosaDTO> call, Throwable t) {
                              Toast.makeText(HomeActivity.this, "Errore di Connessione", Toast.LENGTH_SHORT).show();
                              Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
                          }
                      }
        );

    }

    public void recuperaAstaRibasso(int id_asta, NotificaDTO notifica,ApiService apiService){
        Call<Asta_RibassoDTO> callR = apiService.recuperaDettagliAstaRibasso(id_asta);
        callR.enqueue(new Callback<Asta_RibassoDTO>() {
                          @Override
                          public void onResponse(Call<Asta_RibassoDTO> call, Response<Asta_RibassoDTO> response) {
                              if(response.isSuccessful() && response.body() != null){
                                  Asta_RibassoDTO astaR = response.body();
                                  if(astaR != null) {
                                      String nome_asta = astaR.getNome();
                                      Log.d("RecuperaAsta", "Nome Asta_Ribasso recuperato: " + nome_asta);
                                      notifica.setNome_asta(nome_asta);
                                      Log.d("RecuperaAsta", "Nome Asta_Ribasso nelle Notifiche recuperato: " + notifica.getNome_asta());
                                  }
                                  else Log.d("RecuperaAsta","Asta Silenziosa null");

                              }
                              else
                                  Toast.makeText(HomeActivity.this, "Asta a Ribasso non trovata", Toast.LENGTH_SHORT).show();
                          }

                          @Override
                          public void onFailure(Call<Asta_RibassoDTO> call, Throwable t) {
                              Toast.makeText(HomeActivity.this, "Errore di Connessione", Toast.LENGTH_SHORT).show();
                              Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
                          }
                      }
        );

    }

    public void recuperaAstaInversa(int id_asta, NotificaDTO notifica,ApiService apiService){
        Call<Asta_InversaDTO> callI = apiService.recuperaDettagliAstaInversa(id_asta);
        callI.enqueue(new Callback<Asta_InversaDTO>() {
                          @Override
                          public void onResponse(Call<Asta_InversaDTO> call, Response<Asta_InversaDTO> response) {
                              if(response.isSuccessful() && response.body() != null){
                                  Asta_InversaDTO astaI = response.body();
                                  if(astaI != null) {
                                      String nome_asta = astaI.getNome();
                                      Log.d("RecuperaAsta", "Nome Asta_Inversa recuperato: " + nome_asta);
                                      notifica.setNome_asta(astaI.getNome());
                                      Log.d("RecuperaAsta", "Nome Asta_Inversa Nelle Notifiche recuperato: " + notifica.getNome_asta());
                                  }
                                  else Log.d("RecuperaAsta","Asta Silenziosa null");
                              }
                              else
                                  Toast.makeText(HomeActivity.this, "Asta Inversa non trovata", Toast.LENGTH_SHORT).show();
                          }

                          @Override
                          public void onFailure(Call<Asta_InversaDTO> call, Throwable t) {
                              Toast.makeText(HomeActivity.this, "Errore di Connessione", Toast.LENGTH_SHORT).show();
                              Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE, "Errore rilevato", t);
                          }
                      }
        );
    }

    public Asta_Ribasso creaModelloAstaR(AstaDTO dto) {
        Asta_Ribasso asta = new Asta_Ribasso();
        asta.setId(dto.getID());
        asta.setId_creatore(dto.getId_creatore());
        asta.setCategoria(dto.getCategoria());
        asta.setFoto(dto.getFoto());
        asta.setNome(dto.getNome());
        asta.setDescrizione(dto.getDescrizione());
        asta.setStato(dto.getStato());

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
        asta.setStato(dto.getStato());

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
        asta.setStato(dto.getStato());

        return asta;
    }

    public Asta converteToModel(AstaDTO asta){
        Asta a = new Asta();
        if(asta.getTipo().equals("RIBASSO"))
            a = creaModelloAstaR(asta);
        else if(asta.getTipo().equals("SILENZIOSA"))
            a = creaModelloAstaS(asta);
        else if(asta.getTipo().equals("INVERSA"))
            a = creaModelloAstaI(asta);

        return a;
    }
*/


    @Override
    public void onBackPressed() {
        //
    }


    private void disconnect() {
        builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setMessage("Sei sicuro di voler uscire?")
                .setCancelable(true)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openActivityLogin();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }

    private void openActivityCreaAsta(Utente u) {
        Intent intentR = new Intent(this, CreaAstaActivity.class);
        intentR.putExtra("utente", u);
        intentR.putExtra("fromHome", true);
        startActivity(intentR);
    }

    private void openActivityCercaAsta(Utente utente) {
        Intent intentR = new Intent(this, CercaAstaActivity.class);
        intentR.putExtra("utente", utente);
        intentR.putExtra("fromHome", true);
        startActivity(intentR);
    }

    private void openActivityLogin() {
        Intent intentL = new Intent(this, LoginActivity.class);
        startActivity(intentL);
    }

    private void openActivityProfilo(Utente utente) {
        Intent intentR = new Intent(this, ProfiloActivity.class);
        intentR.putExtra("utente", utente);
        intentR.putExtra("fromDettagli", false);
        startActivity(intentR);
    }

}
