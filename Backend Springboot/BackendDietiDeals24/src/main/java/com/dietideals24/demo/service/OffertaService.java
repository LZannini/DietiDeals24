package com.dietideals24.demo.service;

import java.util.List;

import com.dietideals24.demo.models.dto.OffertaDTO;

public interface OffertaService {

	void creaOfferta(OffertaDTO offertaDTO);
	List<OffertaDTO> getOfferte(int id_asta);
}
