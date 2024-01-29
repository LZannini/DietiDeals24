package Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Repository.*;
import Models.*;

@RestController
@RequestMapping("/api")
public class AppController {

	@Autowired
	private Asta_Inversa_Repository Asta_Inversa;
	@Autowired
	private Asta_Repository Asta;
	@Autowired
	private Asta_Ribasso_Repository Asta_Ribasso;
	@Autowired
	private Asta_Silenziosa_Repository Asta_Silenziosa;
	@Autowired
	private Compratore_Repository Compratore;
	@Autowired
	private Offerta_Repository Offerta;
	@Autowired
	private Utente_Repository utente;
	@Autowired
	private Venditore_Repository Venditore;
	
	//Endpoint Compratore
	@PostMapping("/registraCompratore")
	public ResponseEntity<Utente> registraCompratore(@RequestBody Utente compratore){
		Utente nuovoCompratore = utente.register(compratore);
		return new ResponseEntity<>(nuovoCompratore,HttpStatus.CREATED);
	}
	
	//Endpoint Venditore
	@PostMapping("/registraVenditore")
	public ResponseEntity<Utente> registraVenditore(@RequestBody Utente venditore){
		Utente nuovoVenditore = utente.register(venditore);
		return new ResponseEntity<>(nuovoVenditore,HttpStatus.CREATED);
	}
}
