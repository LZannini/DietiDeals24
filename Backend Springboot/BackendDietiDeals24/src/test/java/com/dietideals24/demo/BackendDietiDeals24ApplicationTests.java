package com.dietideals24.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import Models.Utente;
import Repository_Implements.Utente_Implementation;

@SpringBootTest(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration")
class BackendDietiDeals24ApplicationTests {

	@Autowired
	private Utente_Implementation utente;
	
	@Test
	void addUtente() {
		Utente u = new Utente();
		u.setEmail("Casca@test.com");
		u.setPassword("Scatteru1!");
		u.setUsername("Casca");
		u.setBiografia("Salve a tutti ragazzi, sono qui per vendere prodotti!");
		u.setTipo("Venditore");
		u.setSitoweb("youtube.com");
		utente.save(u);
	}

}
