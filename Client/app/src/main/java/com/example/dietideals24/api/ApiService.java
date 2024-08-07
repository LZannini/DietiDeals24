package com.example.dietideals24.api;

import com.example.dietideals24.dto.*;
import com.example.dietideals24.security.JwtAuthenticationResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiService {

    @POST("/auth/login")
    Call<JwtAuthenticationResponse> loginUtente(@Body UtenteDTO utenteDTO);

    @POST("/auth/google")
    Call<JwtAuthenticationResponse> loginGoogle(@Query("idToken") String idToken);

    @POST("/utente/registra")
    Call<UtenteDTO> registraUtente(@Body UtenteDTO utenteDTO);

    @GET("/utente/recupera")
    Call<UtenteDTO> recuperaUtente(@Query("id") int id);

    @POST("/utente/aggiorna")
    Call<UtenteDTO> aggiornaUtente(@Body UtenteDTO utenteDTO);

    @POST("/utente/modPassword")
    Call<UtenteDTO> modificaPassword(@Body UtenteDTO utenteDTO);

    @GET("/asta/cercaTutte")
    Call<List<AstaDTO>> cercaTutte();

    @GET("/asta/cercaPerUtente")
    Call<List<AstaDTO>> cercaPerUtente(@Query("id_creatore") int id_creatore);

    @GET("/asta/cercaPerChiave")
    Call<List<AstaDTO>> cercaPerParolaChiave(@Query("chiave") String chiave);

    @GET("/asta/cercaPerCategoria")
    Call<List<AstaDTO>> cercaPerCategoria(@Query("categoria") String categoria);

    @GET("/asta/cercaPerChiaveAndCategoria")
    Call<List<AstaDTO>> cercaPerParolaChiaveAndCategoria(@Query("chiave") String chiave, @Query("categoria") String categoria);

    @GET("/asta/cercaPerOfferteUtente")
    Call<List<AstaDTO>> cercaPerOfferteUtente(@Query("id_utente") int id_utente);

    @POST("/asta/creaAstaInversa")
    Call<Void> creaAstaInversa(@Body Asta_InversaDTO astaDTO);

    @POST("/asta/creaAstaAlRibasso")
    Call<Void> creaAstaAlRibasso(@Body Asta_RibassoDTO astaDTO);

    @POST("/asta/creaAstaSilenziosa")
    Call<Void> creaAstaSilenziosa(@Body Asta_SilenziosaDTO astaDTO);

    @GET("asta/recupera")
    Call<AstaDTO> recuperaAsta(@Query("id") int id);

    @GET("/asta/dettagliAstaInversa")
    Call<Asta_InversaDTO> recuperaDettagliAstaInversa(@Query("id") int id);

    @GET("/asta/dettagliAstaRibasso")
    Call<Asta_RibassoDTO> recuperaDettagliAstaRibasso(@Query("id") int id);

    @GET("/asta/dettagliAstaSilenziosa")
    Call<Asta_SilenziosaDTO> recuperaDettagliAstaSilenziosa(@Query("id") int id);

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

    @POST("/offerta/crea")
    Call<Void> creaOfferta(@Body OffertaDTO offertaDTO);

    @GET("/offerta/recuperaOrdinate")
    Call<List<OffertaDTO>> recuperaOffertePerId(@Query("id_asta") Integer id_asta);

    @GET("/offerta/recuperaPerUtente")
    Call<List<OffertaDTO>> recuperaOffertePerUtente(@Query("id_utente") Integer id_utente);

    @PUT("/offerta/accetta")
    Call<Void> accettaOfferta(@Query("id_offerta") Integer id_offerta);

    @PUT("/offerta/rifiuta")
    Call<Void> rifiutaOfferta(@Query("id_offerta") Integer id_offerta);
}
