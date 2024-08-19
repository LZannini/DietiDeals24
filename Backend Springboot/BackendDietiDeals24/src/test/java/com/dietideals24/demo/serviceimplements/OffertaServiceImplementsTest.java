package com.dietideals24.demo.serviceimplements;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import com.dietideals24.demo.enums.StatoAsta;
import com.dietideals24.demo.enums.TipoUtente;
import com.dietideals24.demo.models.Asta;
import com.dietideals24.demo.models.Asta_Inversa;
import com.dietideals24.demo.models.Asta_Ribasso;
import com.dietideals24.demo.models.Asta_Silenziosa;
import com.dietideals24.demo.models.Utente;

class OffertaServiceImplementsTest {
	
	@InjectMocks
	private OffertaServiceImplements service = new OffertaServiceImplements();
	
	private Utente utenteCompratore;
	private Utente utenteVenditore;
	private Asta_Inversa astaInversa;
	private Asta_Ribasso astaRibasso;
	private Asta_Silenziosa astaSilenziosa;
	private Asta astaNonAttiva;
	
	@BeforeEach
    void setUp() {
        utenteCompratore = new Utente("Utente1", "Email1", "Pass1", TipoUtente.COMPRATORE);
        utenteVenditore = new Utente("Utente2", "Email2", "Pass2", TipoUtente.VENDITORE);
        
        astaInversa = new Asta_Inversa(StatoAsta.ATTIVA);
        astaRibasso = new Asta_Ribasso(StatoAsta.ATTIVA);
        astaSilenziosa = new Asta_Silenziosa(StatoAsta.ATTIVA);
        astaNonAttiva = new Asta(StatoAsta.FALLITA);
    }

	@Test
	void testVerificaOfferta_OffertaValida_AstaInversa() {
		assertTrue(service.verificaOfferta(utenteVenditore, astaInversa, 50));
	}
	
	@Test
	void testVerificaOfferta_OffertaValida_AstaRibasso() {
		assertTrue(service.verificaOfferta(utenteCompratore, astaRibasso, 50));
	}
	
	@Test
	void testVerificaOfferta_OffertaValida_AstaSilenziosa() {
		assertTrue(service.verificaOfferta(utenteCompratore, astaSilenziosa, 50));
	}
	
	@Test
	void testVerificaOfferta_AstaNulla() {
		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            service.verificaOfferta(utenteCompratore, null, 50);
        });
        assertEquals("L'asta non è più attiva.", exception.getMessage());
	}
	
	@Test
	void testVerificaOfferta_UtenteNullo() {
		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            service.verificaOfferta(null, astaRibasso, 50);
        });
        assertEquals("L'utente non è stato trovato.", exception.getMessage());
	}
	
	@Test
	void testVerificaOfferta_AstaNonAttiva() {
		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            service.verificaOfferta(utenteCompratore, astaNonAttiva, 50);
        });
        assertEquals("L'asta non è più attiva.", exception.getMessage());
	}
	
	@Test
	void testVerificaOfferta_ValoreZero() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.verificaOfferta(utenteCompratore, astaSilenziosa, 0);
        });
        assertEquals("Il valore dell'offerta è inferiore al minimo consentito.", exception.getMessage());
	}
	
	@Test
	void testVerificaOfferta_ValoreMinoreDiZero() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.verificaOfferta(utenteCompratore, astaSilenziosa, 0);
        });
        assertEquals("Il valore dell'offerta è inferiore al minimo consentito.", exception.getMessage());
	}
	
	@Test
	void testVerificaOfferta_InversaCompratore() {
		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            service.verificaOfferta(utenteCompratore, astaInversa, 50);
        });
        assertEquals("In un'asta inversa, il compratore non può partecipare.", exception.getMessage());
	}
	
	@Test
	void testVerificaOfferta_RibassoVenditore() {
		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            service.verificaOfferta(utenteVenditore, astaRibasso, 50);
        });
        assertEquals("In un'asta a ribasso o silenziosa, il venditore non può partecipare come offerente.", exception.getMessage());
	}
	
	@Test
	void testVerificaOfferta_SilenziosaVenditore() {
		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            service.verificaOfferta(utenteVenditore, astaSilenziosa, 50);
        });
        assertEquals("In un'asta a ribasso o silenziosa, il venditore non può partecipare come offerente.", exception.getMessage());
	}
}
