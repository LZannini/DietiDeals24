package com.example.dietideals24.api;

import com.example.dietideals24.dto.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/utente/registra")
    Call<UtenteDTO> registraUtente(@Body UtenteDTO utenteDTO);

    @POST("/utente/login")
    Call<UtenteDTO> loginUtente(@Body UtenteDTO utenteDTO);
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======

>>>>>>> b93a37852ba927f9049e7a3707ca702d6aa6d91a
=======

>>>>>>> b93a37852ba927f9049e7a3707ca702d6aa6d91a
=======

>>>>>>> b93a37852ba927f9049e7a3707ca702d6aa6d91a
    @POST("/utente/aggiorna")
    Call<UtenteDTO> aggiornaUtente(@Body UtenteDTO utenteDTO);

    @POST("/utente/modPassword")
    Call<UtenteDTO> modificaPassword(@Body UtenteDTO utenteDTO);
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD

    @GET("/asta/cercaPerChiave")
    Call<List<AstaDTO>> cercaPerParolaChiave(@Query("chiave") String chiave);

    @GET("/asta/cercaPerCategoria")
    Call<List<AstaDTO>> cercaPerCategoria(@Query("categoria") String categoria);

    @GET("/asta/cercaPerChiaveAndCategoria")
    Call<List<AstaDTO>> cercaPerParolaChiaveAndCategoria(@Query("chiave") String chiave, @Query("categoria") String categoria);
=======
>>>>>>> b93a37852ba927f9049e7a3707ca702d6aa6d91a
=======
>>>>>>> b93a37852ba927f9049e7a3707ca702d6aa6d91a
=======
>>>>>>> b93a37852ba927f9049e7a3707ca702d6aa6d91a
}
