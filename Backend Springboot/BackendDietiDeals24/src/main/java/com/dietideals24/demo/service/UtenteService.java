package com.dietideals24.demo.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;

import com.dietideals24.demo.models.Utente;
import com.dietideals24.demo.models.dto.UtenteDTO;

public interface UtenteService {
	
	UtenteDTO registraUtente(UtenteDTO utenteDTO);
	UtenteDTO loginUtente(UtenteDTO utenteDTO);
	UtenteDTO recuperaUtente(int id, String email);
	UtenteDTO recuperaUtenteById(int id);
	UtenteDTO updateUtente(UtenteDTO utenteDTO);
	UtenteDTO modificaPassword(int id, String password);
	Utente recuperaUtenteByEmail(String email);

}
