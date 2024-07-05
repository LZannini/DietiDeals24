package com.dietideals24.demo.service;

import java.util.List;

import com.dietideals24.demo.models.dto.OffertaDTO;

public interface OffertaService {
	
	void creaOfferta(OffertaDTO offertaDTO);
	void rimuoviOfferta(int id);
	OffertaDTO getOffertaMinima(int id_asta);
	List<OffertaDTO> getOfferte(int id_asta);
	List<OffertaDTO> getOfferteOrdinate(int id_asta);
	void setOffertaAccettata(int id_offerta);
	void setOffertaRifiutata(int id_offerta);
}
