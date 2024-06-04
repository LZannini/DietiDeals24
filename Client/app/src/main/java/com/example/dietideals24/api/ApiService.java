package com.example.dietideals24.api;

import com.example.dietideals24.dto.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/utente/registra")
    Call<UtenteDTO> registraUtente(@Body UtenteDTO utenteDTO);

    @POST("/utente/login")
    Call<UtenteDTO> loginUtente(@Body UtenteDTO utenteDTO);

    @POST("/utente/aggiorna")
    Call<UtenteDTO> aggiornaUtente(@Body UtenteDTO utenteDTO);

    @POST("/utente/modPassword")
    Call<UtenteDTO> modificaPassword(@Body UtenteDTO utenteDTO);

    @GET("/asta/cercaPerChiave")
    Call<List<AstaDTO>> cercaPerParolaChiave(@Query("chiave") String chiave);

    @GET("/asta/cercaPerCategoria")
    Call<List<AstaDTO>> cercaPerCategoria(@Query("categoria") String categoria);

    @GET("/asta/cercaPerChiaveAndCategoria")
    Call<List<AstaDTO>> cercaPerParolaChiaveAndCategoria(@Query("chiave") String chiave, @Query("categoria") String categoria);

    @GET("/notifica/mostraTutte")
    Call<List<NotificaDTO>> mostraNotifiche(@Query("id_utente") Integer id_utente);

    @POST("/notifica/rimuovi")
    Call<Void> rimuoviNotifica(@Query("id") Integer id);

    @POST("/notifica/rimuoviLette")
    Call<Void> rimuoviAllNotificheLette(@Query("id_utente") Integer id_utente);

    @POST("notifica/svuota")
    Call<Void> svuotaNotifiche(@Query("id_utente") Integer id_utente);

    @PUT("/notifica/segna")
    Call<Void> segnaNotifica(@Query("id") Integer id);

    @PUT("/notifica/segnaTutte")
    Call<Void> segnaTutteLeNotifiche(@Query("id_utente") Integer id_utente);

}
