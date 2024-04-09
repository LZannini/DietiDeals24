package com.dietideals24.demo.service;

import java.util.List;
import java.util.Optional;

import com.dietideals24.demo.enums.Categoria;
import com.dietideals24.demo.enums.TipoAsta;
import com.dietideals24.demo.models.dto.AstaDTO;

public interface AstaService {
	
	void creaAsta(AstaDTO astaDTO);
	void rimuoviAsta(int id);
	List<AstaDTO> trovaAsteUtente(int id_creatore);
	List<AstaDTO> trovaAstePerTipo(TipoAsta tipo);
	List<AstaDTO> trovaAstePerParolaChiave(String chiave);
	List<AstaDTO> trovaAstePerCategoria(Categoria categoria);
	List<AstaDTO> trovaAstePerParolaChiaveAndCategoria(String chiave, Categoria categoria);
}
