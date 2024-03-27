package com.dietideals24.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dietideals24.demo.models.dto.OffertaDTO;
import com.dietideals24.demo.repository.OffertaRepository;
import com.dietideals24.demo.service.OffertaService;

@RestController
public class OffertaController {
	
	@Autowired
    @Qualifier("OffertaService")
    private OffertaService offertaService;
	@Autowired
	private OffertaRepository offertaRepository;
	
	@PostMapping("/offerta/crea_offerta")
	public void crea(@RequestBody OffertaDTO offertaDTO) {
		if (offertaDTO == null)
			throw new IllegalArgumentException("Errore Creazione Offerta: parametro inserito non valido!\n");
		offertaService.creaOfferta(offertaDTO);
	}
	
	@GetMapping("/offerta/recupera_offerte")
	public ResponseEntity<List<OffertaDTO>> getOfferte(@RequestParam Integer id_asta) throws IllegalArgumentException {
		if (id_asta == null) 
			throw new IllegalArgumentException("Errore Recupero Offerte: Il parametro 'id_asta' è null!\n");
		List<OffertaDTO> lista_offerteDTO = offertaService.getOfferte(id_asta);  
		if (lista_offerteDTO == null || lista_offerteDTO.isEmpty())
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(lista_offerteDTO);
	}
}
