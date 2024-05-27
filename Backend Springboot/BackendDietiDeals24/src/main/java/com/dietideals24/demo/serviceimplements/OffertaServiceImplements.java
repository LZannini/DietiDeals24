package com.dietideals24.demo.serviceimplements;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dietideals24.demo.models.Offerta;
import com.dietideals24.demo.models.dto.OffertaDTO;
import com.dietideals24.demo.repository.OffertaRepository;
import com.dietideals24.demo.service.OffertaService;

@Service("OffertaService")
public class OffertaServiceImplements implements OffertaService {
	
	@Autowired
	private OffertaRepository offertaRepository;

	@Override
	public void creaOfferta(OffertaDTO offertaDTO) {
		Offerta offerta = new Offerta();
		offerta.setId_utente(offertaDTO.getId_utente());
		offerta.setId_asta(offertaDTO.getId_asta());
		offerta.setValore(offertaDTO.getValore());
		offerta.setData(offertaDTO.getData());
		offertaRepository.save(offerta);
	}
	
	@Override
	public void rimuoviOfferta(int id) {
		offertaRepository.eliminaOfferta(id);
	}
	
	@Override
	public OffertaDTO getOffertaMinima(int id_asta) {
		Offerta offertaMin = offertaRepository.trovaOffertaMinima(id_asta);
		if (offertaMin == null)
			return null;
		OffertaDTO offertaMin_DTO = creaOffertaDTO(offertaMin);
		return offertaMin_DTO;
	}

	@Override
	public List<OffertaDTO> getOfferte(int id_asta) {
		List<Offerta> offerte = offertaRepository.trovaOfferte(id_asta);
		if (offerte.isEmpty())
			return null;
		List<OffertaDTO> lista_offerteDTO = new ArrayList<>();
        for (Offerta o : offerte) {
            OffertaDTO offertaDTO = creaOffertaDTO(o);
            lista_offerteDTO.add(offertaDTO);
        }
        return lista_offerteDTO;
	}

	@Override
	public List<OffertaDTO> getOfferteOrdinate(int id_asta) {
		List<Offerta> offerte = offertaRepository.trovaOfferteOrdinate(id_asta);
		if (offerte.isEmpty())
			return null;
		List<OffertaDTO> lista_offerteDTO = new ArrayList<>();
        for (Offerta o : offerte) {
            OffertaDTO offertaDTO = creaOffertaDTO(o);
            lista_offerteDTO.add(offertaDTO);
        }
        return lista_offerteDTO;
	}
	
	private OffertaDTO creaOffertaDTO(Offerta offerta) {
		OffertaDTO offertaDTO = new OffertaDTO();
		offertaDTO.setId(offerta.getId());
        offertaDTO.setId_asta(offerta.getId_asta());
        offertaDTO.setId_utente(offerta.getId_utente());
        offertaDTO.setValore(offerta.getValore());
        offertaDTO.setData(offerta.getData());
        return offertaDTO;
	}
}
