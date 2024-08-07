package com.dietideals24.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dietideals24.demo.models.dto.OffertaDTO;
import com.dietideals24.demo.service.OffertaService;

@RestController
public class OffertaController {
	
	@Autowired
    @Qualifier("OffertaService")
    private OffertaService offertaService;
	
	@PostMapping("/offerta/crea")
	public void crea(@RequestBody OffertaDTO offertaDTO) {
		if (offertaDTO == null)
			throw new IllegalArgumentException("Errore Creazione Offerta: parametro inserito non valido!\n");
		offertaService.creaOfferta(offertaDTO);
	}
	
	@PostMapping("/offerta/rimuovi")
	public void rimuovi(@RequestParam Integer id) {
		if (id == null)
			throw new IllegalArgumentException("Errore Rimozione Offerta: Il parametro 'id' è null!\n");
		offertaService.rimuoviOfferta(id);
	}
	
	@GetMapping("/offerta/recuperaPerUtente")
	public ResponseEntity<List<OffertaDTO>> getOfferte(@RequestParam Integer id_utente) throws IllegalArgumentException {
		if (id_utente == null) 
			throw new IllegalArgumentException("Errore Recupero Offerte: Il parametro 'id_utente' è null!\n");
		List<OffertaDTO> lista_offerteDTO = offertaService.getOfferteUtente(id_utente);  
		if (lista_offerteDTO == null || lista_offerteDTO.isEmpty())
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(lista_offerteDTO);
	}
	
	@GetMapping("/offerta/recuperaOrdinate")
	public ResponseEntity<List<OffertaDTO>> getOfferteOrdinate(@RequestParam Integer id_asta) throws IllegalArgumentException {
		if (id_asta == null) 
			throw new IllegalArgumentException("Errore Recupero Offerte Ordinate: Il parametro 'id_asta' è null!\n");
		List<OffertaDTO> lista_offerteDTO = offertaService.getOfferteOrdinate(id_asta);  
		if (lista_offerteDTO == null || lista_offerteDTO.isEmpty())
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(lista_offerteDTO);
	}
	
	@PutMapping("/offerta/accetta")
	public void accettaOfferta(@RequestParam Integer id_offerta) throws IllegalArgumentException {
		if (id_offerta == null)
			throw new IllegalArgumentException("Errore Accetta Offerta: Il parametro 'id_offerta' è null!\n");
		offertaService.setOffertaAccettata(id_offerta);
	}
	
	@PutMapping("/offerta/rifiuta")
	public void rifiutaOfferta(@RequestParam Integer id_offerta) throws IllegalArgumentException {
		if (id_offerta == null)
			throw new IllegalArgumentException("Errore Accetta Offerta: Il parametro 'id_offerta' è null!\n");
		offertaService.setOffertaRifiutata(id_offerta);
	}
}
