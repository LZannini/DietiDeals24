package com.dietideals24.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dietideals24.demo.models.Asta;
import com.dietideals24.demo.models.Utente;
import com.dietideals24.demo.models.dto.AstaDTO;
import com.dietideals24.demo.repository.AstaRepository;
import com.dietideals24.demo.repository.Asta_Inversa_Repository;
import com.dietideals24.demo.repository.Asta_Ribasso_Repository;
import com.dietideals24.demo.repository.Asta_Silenziosa_Repository;
import com.dietideals24.demo.repository.UtenteRepository;
import com.dietideals24.demo.service.AstaService;

@RestController
public class AstaController {
	
	@Autowired
    @Qualifier("AstaService")
    private AstaService astaService;
	@Autowired
	private Asta_Inversa_Repository Asta_Inversa;
	@Autowired
	private AstaRepository Asta;
	@Autowired
	private Asta_Ribasso_Repository Asta_Ribasso;
	@Autowired
	private Asta_Silenziosa_Repository Asta_Silenziosa;
	
	@GetMapping("/asta/lista_aste")
	public Iterable<Asta> getAll() {
		List<Asta> list = new ArrayList<>();
		
		return list;
	}
	
	@PostMapping("/asta/rimuovi_asta")
	public void rimuovi(@RequestParam Integer id) {
		if (id == null)
			throw new IllegalArgumentException("Errore Rimozione Asta: Parametro inserito non valido!\n");
		astaService.rimuoviAsta(id);
	}
	
	@GetMapping("/asta/cerca_per_nome")
	public ResponseEntity<List<AstaDTO>> cercaPerNome(@RequestParam String nome) {
		if (nome == null)
			throw new IllegalArgumentException("Errore Ricerca Asta: Campo 'nome' non valido!\n");
		List<AstaDTO> lista_asteDTO = astaService.trovaAstePerNome(nome);
		if (lista_asteDTO == null || lista_asteDTO.isEmpty())
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(lista_asteDTO);
	}

}
