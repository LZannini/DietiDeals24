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
	public List<OffertaDTO> getOfferte(int id_asta) {
		List<Offerta> offerte = offertaRepository.trovaOfferte(id_asta);
		if (offerte.isEmpty())
			return null;
		List<OffertaDTO> lista_offerteDTO = new ArrayList<>();
        for (Offerta o : offerte) {
            OffertaDTO offertaDTO = new OffertaDTO();
            offertaDTO.setId_asta(o.getId_asta());
            offertaDTO.setId_utente(o.getId_utente());
            offertaDTO.setValore(o.getValore());
            offertaDTO.setData(o.getData()); 

            lista_offerteDTO.add(offertaDTO);
        }
        return lista_offerteDTO;
	}

}
