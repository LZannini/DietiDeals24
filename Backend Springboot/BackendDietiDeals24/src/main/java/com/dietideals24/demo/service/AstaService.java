package com.dietideals24.demo.service;

import java.util.List;

import com.dietideals24.demo.enums.Categoria;
import com.dietideals24.demo.models.dto.AstaDTO;
import com.dietideals24.demo.models.dto.Asta_InversaDTO;
import com.dietideals24.demo.models.dto.Asta_RibassoDTO;
import com.dietideals24.demo.models.dto.Asta_SilenziosaDTO;

public interface AstaService {
	
	void creaAstaInversa(Asta_InversaDTO astaDTO);
	void creaAstaAlRibasso(Asta_RibassoDTO astaDTO);
	void creaAstaSilenziosa(Asta_SilenziosaDTO astaDTO);
	void rimuoviAsta(int id);
	AstaDTO trovaAsta(int id);
	List<AstaDTO> trovaTutte();
	List<AstaDTO> trovaAsteUtente(int id_creatore);
	List<AstaDTO> trovaAstePerParolaChiave(String chiave);
	List<AstaDTO> trovaAstePerCategoria(Categoria categoria);
	List<AstaDTO> trovaAstePerParolaChiaveAndCategoria(String chiave, Categoria categoria);
	Asta_InversaDTO trovaAstaInversa(Integer id);
	Asta_RibassoDTO trovaAstaRibasso(Integer id);
	Asta_SilenziosaDTO trovaAstaSilenziosa(Integer id);
}
