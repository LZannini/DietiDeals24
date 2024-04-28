package com.example.dietideals24.api;

import com.example.dietideals24.dto.UtenteDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/utente/registra")
    Call<UtenteDTO> registraUtente(@Body UtenteDTO utenteDTO);

    @POST("/utente/login")
    Call<UtenteDTO> loginUtente(@Body UtenteDTO utenteDTO);
}
