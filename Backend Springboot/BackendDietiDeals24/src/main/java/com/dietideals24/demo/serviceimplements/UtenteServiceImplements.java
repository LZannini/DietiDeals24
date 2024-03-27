package com.dietideals24.demo.serviceimplements;

import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dietideals24.demo.models.Utente;
import com.dietideals24.demo.models.dto.UtenteDTO;
import com.dietideals24.demo.repository.UtenteRepository;
import com.dietideals24.demo.service.UtenteService;

@Service("UtenteService")
public class UtenteServiceImplements implements UtenteService {
	
	@Autowired
	private UtenteRepository utenteRepository;
	
	@Override
	public UtenteDTO registraUtente(UtenteDTO utenteDTO) {
		utenteRepository.save(UtenteServiceImplements.creaUtente(utenteDTO));
			
        java.util.Optional<Utente> check_utente = utenteRepository.findByEmailAndPassword(utenteDTO.getEmail(), utenteDTO.getPassword());
        if (check_utente.isPresent()) {
        	Utente utente = check_utente.get();
            utenteDTO.setId(utente.getId());
        }
        
        return utenteDTO;
	}
	
	@Override
	public UtenteDTO loginUtente(UtenteDTO utenteDTO) {
		java.util.Optional<Utente> check_utente = utenteRepository.findByEmailAndPassword(utenteDTO.getEmail(), utenteDTO.getPassword());
        if (!check_utente.isPresent()) 
        	 throw new IllegalArgumentException("Utente non trovato!");
  
        Utente utente = check_utente.get();
        return UtenteServiceImplements.creaUtenteLogin(utente);
	}
	

	@Override
	public void aggiornaUtente(UtenteDTO utenteDTO) {
		Utente utente = new Utente();
		utente.setId(utenteDTO.getId());
		utente.setUsername(utenteDTO.getUsername());
		utente.setEmail(utenteDTO.getEmail());
		utente.setPassword(utenteDTO.getPassword());
		utente.setBiografia(utenteDTO.getBiografia());
		utente.setPaese(utenteDTO.getPaese());
		utente.setSitoweb(utenteDTO.getSitoweb());
		utente.setAvatar(utenteDTO.getAvatar());
		utenteRepository.save(utente);
	}
	
	@Override
	public UtenteDTO recuperaUtente(int id, String email) {
		java.util.Optional<Utente> check_utente = utenteRepository.findByEmailAndId(email, id);
		Utente utente = null;
		UtenteDTO utenteDTO = null;
		
		if (check_utente.isPresent())
			utente = check_utente.get();
		
		if (utente != null) {
			utenteDTO = new UtenteDTO();
			utenteDTO.setId(utente.getId());
			utenteDTO.setUsername(utente.getUsername());
			utenteDTO.setEmail(utente.getEmail());
			utenteDTO.setPassword(utente.getPassword());
			utenteDTO.setBiografia(utente.getBiografia());
			utenteDTO.setPaese(utente.getPaese());
			utenteDTO.setSitoweb(utente.getSitoweb());
			utenteDTO.setAvatar(utente.getAvatar());
		}
		return utenteDTO;
	}
	
	private static Utente creaUtente(UtenteDTO utenteDTO) {
        Utente utente = new Utente();
        utente.setUsername(utenteDTO.getUsername());
        utente.setEmail(utenteDTO.getEmail());
        utente.setPassword(utenteDTO.getPassword());

        return utente;
    }
	
	private static UtenteDTO creaUtenteLogin(Utente utente) {
        UtenteDTO utenteDTO = new UtenteDTO();
        utenteDTO.setId(utente.getId());
        utenteDTO.setUsername(utente.getUsername());
        utenteDTO.setEmail(utente.getEmail());
        utenteDTO.setPassword(utente.getPassword());
        utenteDTO.setBiografia(utente.getBiografia());
        utenteDTO.setPaese(utente.getPaese());
        utenteDTO.setSitoweb(utente.getSitoweb());
        utenteDTO.setAvatar(utente.getAvatar());

        return utenteDTO;
    }
}
