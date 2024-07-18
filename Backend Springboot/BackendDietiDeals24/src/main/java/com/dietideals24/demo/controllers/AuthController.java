package com.dietideals24.demo.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietideals24.demo.configurations.JwtAuthenticationResponse;
import com.dietideals24.demo.configurations.JwtTokenProvider;
import com.dietideals24.demo.models.dto.UtenteDTO;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;
	
	@GetMapping("/google")
    public String welcomeGoogle() {
    	return "Welcome to Google!!";
    }
	
	 @GetMapping("/facebook")
	    public String welcomeFacebook() {
	        return "Welcome to Facebook!!";
	    }
    
	
	 @PostMapping("/login")
		public ResponseEntity<?> login(@RequestBody UtenteDTO utenteDTO) {
		 	try {
				Authentication authentication = authenticationManager.authenticate(
		                new UsernamePasswordAuthenticationToken(
		                        utenteDTO.getEmail(), utenteDTO.getPassword()));
	
		        SecurityContextHolder.getContext().setAuthentication(authentication);
		        
		        String token = tokenProvider.generateToken(authentication);
		        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
		 } catch (AuthenticationException e) {
		        // Gestione dell'errore di autenticazione
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenziali non valide");
		    } 
		}
	
    
    
	
}
