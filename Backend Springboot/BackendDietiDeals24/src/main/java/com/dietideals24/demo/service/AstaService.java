package com.dietideals24.demo.service;

import java.util.List;
import java.util.Optional;

import com.dietideals24.demo.models.dto.AstaDTO;

public interface AstaService {
	void creaAsta(AstaDTO astaDTO);
	void rimuoviAsta(int id);
	List<AstaDTO> trovaAstePerNome(String nome);
}
