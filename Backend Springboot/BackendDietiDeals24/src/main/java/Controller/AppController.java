package Controller;

import java.util.List;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
	private Offerta_Repository Offerta_rep;
	@Autowired
	private Utente_Repository utente_rep;
	
	@GetMapping("/")
	public ResponseEntity<String> handledefault() {
	    String errorMessage = "Manneggia il cuore di Cristo!!";
	    return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
	}

	
	//Endpoint Compratore
	@PostMapping("/registraCompratore")
	public ResponseEntity<Utente> registraCompratore(@RequestBody Utente compratore){
		Utente nuovoCompratore = utente_rep.newRegistrazioneCompratore(compratore);
		return new ResponseEntity<>(nuovoCompratore,HttpStatus.CREATED);
	}
	
	//Endpoint Venditore
	@PostMapping("/registraVenditore")
	public ResponseEntity<Utente> registraVenditore(@RequestBody Utente venditore){
		Utente nuovoVenditore = utente_rep.newRegistrazioneVenditore(venditore);
		return new ResponseEntity<>(nuovoVenditore,HttpStatus.CREATED);
	}
	
	@PostMapping("/testRegistraVenditore")
	public ResponseEntity<Utente> testRegistraVenditore(){
		Utente Venditore = new Utente();
		Venditore.setEmail("test@email.com");
		Venditore.setPassword("test1!");
		Utente nuovoVenditore = utente_rep.newRegistrazioneVenditore(Venditore); 
		return new ResponseEntity<>(nuovoVenditore, HttpStatus.CREATED);
	}
	
	private Utente getCurrentUser() {
		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof Utente)) {
	            return null;
	        }
		 
	    return (Utente) authentication.getPrincipal();
	}
	
	 @PostMapping("/crea-asta")
	    public ResponseEntity<?> creaAsta(@RequestBody Asta asta, @RequestParam String tipo) {
	        Utente utente = getCurrentUser();
	        List<Utente> creatore = utente_rep.newAsta(utente, asta, tipo);
	        return ResponseEntity.ok(creatore);
	    }
	 
	 @PostMapping("/presenta-offerta")
	 public ResponseEntity<?> presentaOfferta(@RequestBody Asta asta, @RequestParam float importo) {
	     Utente utente = getCurrentUser(); // Utente corrente con Spring Security
	     Offerta offerta = Offerta_rep.presentaOfferta(utente, asta, importo); 
	     return ResponseEntity.ok(offerta);
	 }

}
