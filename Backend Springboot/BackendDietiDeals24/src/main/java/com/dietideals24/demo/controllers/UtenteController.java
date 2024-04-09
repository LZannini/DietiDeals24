package com.dietideals24.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dietideals24.demo.models.Utente;
import com.dietideals24.demo.models.dto.UtenteDTO;
import com.dietideals24.demo.repository.UtenteRepository;
import com.dietideals24.demo.service.UtenteService;

@RestController
public class UtenteController {
	
	@Autowired
    @Qualifier("UtenteService")
    private UtenteService utenteService;
	
	@PostMapping("/utente/registra")
	public ResponseEntity<UtenteDTO> registra(@RequestBody UtenteDTO utenteDTO) {
		String username = utenteDTO.getUsername(), email = utenteDTO.getEmail(), password = utenteDTO.getPassword();
		
		if (username.isBlank() || email.isBlank() || password.isBlank())
			throw new IllegalArgumentException("Errore Registrazione: Campi credenziali vuoti!");
		
		return ResponseEntity.ok(utenteService.registraUtente(utenteDTO));
	}
	
	@PostMapping("/utente/login")
	public ResponseEntity<UtenteDTO> login(@RequestBody UtenteDTO utenteDTO) {
		if (utenteDTO == null)
			throw new IllegalArgumentException("Errore Login: Utente non trovato!");
		return ResponseEntity.ok(utenteService.loginUtente(utenteDTO));
	}
	
	
	@GetMapping("/utente/cerca")
	public ResponseEntity<UtenteDTO> cerca(@RequestParam Integer id, @RequestParam String email) {
		if (id == null || email == null)
			throw new IllegalArgumentException("Errore Ricerca: Parametri non validi!");
		
		UtenteDTO utenteDTO = utenteService.recuperaUtente(id, email);
        return ResponseEntity.ok(utenteDTO);
	}
	
	@PostMapping("/utente/aggiorna")
	public void aggiorna(@RequestBody UtenteDTO utenteDTO) {
		if (utenteDTO == null)
			throw new IllegalArgumentException("Errore Aggiornamento: Utente non valido!");
		utenteService.aggiornaUtente(utenteDTO);
	}
}
