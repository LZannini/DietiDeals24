package com.dietideals24.demo.serviceimplements;

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
        if (!check_utente.isPresent()) {
        	throw new IllegalArgumentException("Utente non trovato!");	
        } else {
        	Utente utente = check_utente.get();
        	return UtenteServiceImplements.creaUtenteDTO(utente);
        }
	}
	

	@Override
	public UtenteDTO updateUtente(UtenteDTO utenteDTO) {
		utenteRepository.save(UtenteServiceImplements.aggiornaUtente(utenteDTO));
		
		return utenteDTO;
	}
	
	public UtenteDTO modificaPassword(int id, String password) {
		utenteRepository.updatePassword(password, id);
		
		java.util.Optional<Utente> utente_modificato = utenteRepository.findById(id);
		if(!utente_modificato.isPresent()) {
			throw new IllegalArgumentException("Utente non trovato!");
		} else {
			Utente utente = utente_modificato.get();
			return UtenteServiceImplements.creaUtenteDTO(utente);
		}
	}
	
	private static Utente aggiornaUtente(UtenteDTO utenteDTO) {
		Utente utente = new Utente();
		utente.setId(utenteDTO.getId());
		utente.setUsername(utenteDTO.getUsername());
		utente.setEmail(utenteDTO.getEmail());
		utente.setPassword(utenteDTO.getPassword());
		utente.setBiografia(utenteDTO.getBiografia());
		utente.setPaese(utenteDTO.getPaese());
		utente.setSitoweb(utenteDTO.getSitoweb());
		utente.setAvatar(utenteDTO.getAvatar());
		utente.setTipo(utenteDTO.getTipo());
		
		return utente;
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
			utenteDTO.setTipo(utente.getTipo());
			utenteDTO.setAvatar(utente.getAvatar());
		}
		return utenteDTO;
	}
	
	@Override
	public UtenteDTO recuperaUtenteById(int id) {
		java.util.Optional<Utente> check_utente = utenteRepository.findById(id);
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
			utenteDTO.setTipo(utente.getTipo());
			utenteDTO.setAvatar(utente.getAvatar());
		}
		return utenteDTO;
	}
	
	private static Utente creaUtente(UtenteDTO utenteDTO) {
        Utente utente = new Utente();
        utente.setUsername(utenteDTO.getUsername());
        utente.setEmail(utenteDTO.getEmail());
        utente.setPassword(utenteDTO.getPassword());
        utente.setTipo(utenteDTO.getTipo());

        return utente;
    }
	
	private static UtenteDTO creaUtenteDTO(Utente utente) {
        UtenteDTO utenteDTO = new UtenteDTO();
        utenteDTO.setId(utente.getId());
        utenteDTO.setUsername(utente.getUsername());
        utenteDTO.setEmail(utente.getEmail());
        utenteDTO.setPassword(utente.getPassword());
        utenteDTO.setBiografia(utente.getBiografia());
        utenteDTO.setPaese(utente.getPaese());
        utenteDTO.setSitoweb(utente.getSitoweb());
        utenteDTO.setTipo(utente.getTipo());
        utenteDTO.setAvatar(utente.getAvatar());

        return utenteDTO;
    }
}
