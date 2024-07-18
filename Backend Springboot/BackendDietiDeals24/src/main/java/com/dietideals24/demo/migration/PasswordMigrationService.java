package com.dietideals24.demo.migration;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dietideals24.demo.models.Utente;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class PasswordMigrationService {
	 @PersistenceContext
	    private EntityManager entityManager;

	    private final PasswordEncoder passwordEncoder;

	    public PasswordMigrationService(PasswordEncoder passwordEncoder) {
	        this.passwordEncoder = passwordEncoder;
	    }

	    @Transactional
	    public void migratePasswords() {
	    	
	        List<Utente> users = entityManager.createQuery("SELECT u FROM Utente u", Utente.class).getResultList();
	        
	        for (Utente user : users) {
	            String currentPassword = user.getPassword();
	            if (!isBCryptEncoded(currentPassword)) {
	                String encodedPassword = passwordEncoder.encode(currentPassword);
	                user.setPassword(encodedPassword);
	                entityManager.persist(user);
	            }
	        }
	    }
	    
	    private boolean isBCryptEncoded(String password) {
	        return password != null && password.startsWith("$2a$");
	    }
}
