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

import com.dietideals24.demo.enums.Categoria;
import com.dietideals24.demo.enums.TipoAsta;
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
	
	@GetMapping("/asta/lista")
	public Iterable<Asta> getAll() {
		List<Asta> list = new ArrayList<>();
		
		return list;
	}
	
	@PostMapping("/asta/crea")
	public ResponseEntity<AstaDTO> crea(@RequestBody AstaDTO asta) {
		
		astaService.creaAsta(asta);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/asta/rimuovi")
	public void rimuovi(@RequestParam Integer id) {
		if (id == null)
			throw new IllegalArgumentException("Errore Rimozione Asta: Campo 'id' nullo!\n");
		astaService.rimuoviAsta(id);
	}
	
	@GetMapping("/asta/cercaPerUtente")
	public ResponseEntity<List<AstaDTO>> cercaPerUtente(@RequestParam Integer id_creatore) {
		if (id_creatore == null)
			throw new IllegalArgumentException("Errore Ricerca Asta (per utente): Campo 'id_creatore' nullo!\n");
		List<AstaDTO> lista_asteDTO = astaService.trovaAsteUtente(id_creatore);
		if (lista_asteDTO == null || lista_asteDTO.isEmpty())
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(lista_asteDTO);
	}
	
	@GetMapping("/asta/cercaPerTipo")
	public ResponseEntity<List<AstaDTO>> cercaPerTipo(@RequestParam TipoAsta tipo) {
		if (tipo == null)
			throw new IllegalArgumentException("Errore Ricerca Asta (per tipo): Campo 'tipo' nullo!\n");
		List<AstaDTO> lista_asteDTO = astaService.trovaAstePerTipo(tipo);
		if (lista_asteDTO == null || lista_asteDTO.isEmpty())
			return ResponseEntity.notFound().build();
					
		return ResponseEntity.ok(lista_asteDTO);
	}
	
	@GetMapping("/asta/cercaPerChiave")
	public ResponseEntity<List<AstaDTO>> cercaPerParolaChiave(@RequestParam String chiave) {
		if (chiave == null)
			throw new IllegalArgumentException("Errore Ricerca Asta (per parola chiave): Campo 'chiave' nullo!\n");
		List<AstaDTO> lista_asteDTO = astaService.trovaAstePerParolaChiave(chiave);
		if (lista_asteDTO == null || lista_asteDTO.isEmpty())
			return ResponseEntity.notFound().build();
					
		return ResponseEntity.ok(lista_asteDTO);
	}
	
	@GetMapping("/asta/cercaPerCategoria")
	public ResponseEntity<List<AstaDTO>> cercaPerCategoria(@RequestParam Categoria categoria) {
		if (categoria == null)
			throw new IllegalArgumentException("Errore Ricerca Asta (per categoria): Campo 'categoria' nullo!\n");
		List<AstaDTO> lista_asteDTO = astaService.trovaAstePerCategoria(categoria);
		if (lista_asteDTO == null || lista_asteDTO.isEmpty())
			return ResponseEntity.notFound().build();
					
		return ResponseEntity.ok(lista_asteDTO);
	}
	
	@GetMapping("/asta/cercaPerChiaveAndCategoria")
	public ResponseEntity<List<AstaDTO>> cercaPerParolaChiaveAndCategoria(@RequestParam String chiave, @RequestParam Categoria categoria) {
		if (chiave == null || categoria == null)
			throw new IllegalArgumentException("Errore Ricerca Asta (per chiave e categoria): Campi nulli!\n");
		List<AstaDTO> lista_asteDTO = astaService.trovaAstePerParolaChiaveAndCategoria(chiave, categoria);
		if (lista_asteDTO == null || lista_asteDTO.isEmpty())
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(lista_asteDTO);
	}
 }
