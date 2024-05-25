package com.dietideals24.demo.service;

import com.dietideals24.demo.models.dto.UtenteDTO;

public interface UtenteService {
	
	UtenteDTO registraUtente(UtenteDTO utenteDTO);
	UtenteDTO loginUtente(UtenteDTO utenteDTO);
	UtenteDTO recuperaUtente(int id, String email);
	UtenteDTO updateUtente(UtenteDTO utenteDTO);
	UtenteDTO modificaPassword(int id, String password);

}
