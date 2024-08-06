package com.dietideals24.demo.serviceimplements;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dietideals24.demo.models.Utente;
import com.dietideals24.demo.repository.UtenteRepository;

@Service
public class CustomUserDetailsServiceImplements implements UserDetailsService{
	
	@Autowired
	private final UtenteRepository utenteRepository;
	
	public CustomUserDetailsServiceImplements(UtenteRepository utenteRepository) {
		this.utenteRepository = utenteRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Utente utente = utenteRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Email not found"));
		return new CustomUserDetails(
                utente.getId(),
                utente.getEmail(),
                utente.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(utente.getTipo().toString()))
        );
	}
	
	public UserDetails loadUserById(int id) throws UsernameNotFoundException {
		if(id == 0) return null;
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        
        return new CustomUserDetails(
                utente.getId(),
                utente.getEmail(),
                utente.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(utente.getTipo().toString()))
        );
    }

}
