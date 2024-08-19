package com.dietideals24.demo.serviceimplements;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.dietideals24.demo.enums.Categoria;
import com.dietideals24.demo.enums.StatoAsta;
import com.dietideals24.demo.models.Asta;
import com.dietideals24.demo.models.Asta_Inversa;
import com.dietideals24.demo.models.Asta_Ribasso;
import com.dietideals24.demo.models.Asta_Silenziosa;
import com.dietideals24.demo.models.dto.AstaDTO;
import com.dietideals24.demo.repository.AstaRepository;
import com.dietideals24.demo.serviceimplements.AstaServiceImplements;

class AstaServiceImplementsTest {
	
	@Mock
	private AstaRepository astaRepository;
	
	@InjectMocks
	private AstaServiceImplements service = new AstaServiceImplements();
	
	@BeforeEach
	private void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void testTrovaAstePerParolaChiaveAndCategoria_EntrambiNulli() {
		List<AstaDTO> lista = service.trovaAstePerParolaChiaveAndCategoria(null, null);
		assertNull(lista);
	}
	
	@Test
	void testTrovaAstePerParolaChiaveAndCategoria_ParolaChiaveNulla_CategoriaValida() {
		Categoria categoria = Categoria.ABBIGLIAMENTO;
		List<AstaDTO> lista = service.trovaAstePerParolaChiaveAndCategoria(null, categoria);
		assertNull(lista);
	}
	
	@Test
	void testTrovaAstePerParolaChiaveAndCategoria_ParolaChiaveValida_CategoriaNulla() {
		String chiave = "T-Shirt";
		List<AstaDTO> lista = service.trovaAstePerParolaChiaveAndCategoria(chiave, null);
		assertNull(lista);
	}
	
	@Test
	void testTrovaAstePerParolaChiaveAndCategoria_ParolaChiaveValida_CategoriaValida_AsteTrovate() {
		String chiave = "T-Shirt";
		Categoria categoria = Categoria.ABBIGLIAMENTO;
		List<Asta> aste = Arrays.asList(new Asta_Ribasso(), new Asta_Silenziosa(), new Asta_Inversa());
		when(astaRepository.filtraPerCategoriaAndParoleChiave(chiave, categoria, StatoAsta.ATTIVA)).thenReturn(aste);
		
		List<AstaDTO> risultato = service.trovaAstePerParolaChiaveAndCategoria(chiave, categoria); 
		
		assertNotNull(risultato);
		assertEquals(3, risultato.size());
		assertEquals("RIBASSO", risultato.get(0).getTipo());
		assertEquals("SILENZIOSA", risultato.get(1).getTipo());
		assertEquals("INVERSA", risultato.get(2).getTipo());
		
		verify(astaRepository, times(1))
        	.filtraPerCategoriaAndParoleChiave(chiave, categoria, StatoAsta.ATTIVA);
	}
	
	@Test
	void testTrovaAstePerParolaChiaveAndCategoria_ParolaChiaveValida_CategoriaValida_AsteNonTrovate() {
		String chiave = "T-Shirt";
		Categoria categoria = Categoria.ABBIGLIAMENTO;
		
		 when(astaRepository.filtraPerCategoriaAndParoleChiave(chiave, categoria, StatoAsta.ATTIVA)).thenReturn(Collections.emptyList());
		 
		 List<AstaDTO> risultato = service.trovaAstePerParolaChiaveAndCategoria(chiave, categoria);
		 
		 assertNotNull(risultato);
		 assertTrue(risultato.isEmpty());
	}


}
