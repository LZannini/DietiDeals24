package com.dietideals24.demo.serviceimplements;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dietideals24.demo.enums.StatoAsta;
import com.dietideals24.demo.enums.StatoOfferta;
import com.dietideals24.demo.enums.TipoUtente;
import com.dietideals24.demo.models.Asta;
import com.dietideals24.demo.models.Asta_Inversa;
import com.dietideals24.demo.models.Asta_Ribasso;
import com.dietideals24.demo.models.Asta_Silenziosa;
import com.dietideals24.demo.models.Offerta;
import com.dietideals24.demo.models.Utente;
import com.dietideals24.demo.models.dto.OffertaDTO;
import com.dietideals24.demo.repository.AstaRepository;
import com.dietideals24.demo.repository.OffertaRepository;
import com.dietideals24.demo.repository.UtenteRepository;
import com.dietideals24.demo.service.OffertaService;

@Service("OffertaService")
public class OffertaServiceImplements implements OffertaService {
	
	@Autowired
	private OffertaRepository offertaRepository;
	@Autowired
	private AstaRepository astaRepository;
	@Autowired
	private UtenteRepository utenteRepository;

	@Override
	public void creaOfferta(OffertaDTO offertaDTO) {
	    Asta asta = astaRepository.getAsta(offertaDTO.getId_asta());
	    Optional<Utente> utenteOpt = utenteRepository.findById(offertaDTO.getId_utente());
	    Utente utente = utenteOpt.get();

		if(verificaOfferta(utente, asta, offertaDTO.getValore())) {
			Offerta offerta = new Offerta();
			offerta.setId_utente(offertaDTO.getId_utente());
			offerta.setId_asta(offertaDTO.getId_asta());
			offerta.setValore(offertaDTO.getValore());
			offerta.setData(offertaDTO.getData());
			offerta.setStato(offertaDTO.getStato());
			offertaRepository.save(offerta);
		}	
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
	public List<OffertaDTO> getOfferteUtente(int id_utente) {
		List<Offerta> offerte = offertaRepository.trovaOfferteUtente(id_utente);
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
	
	@Override
	public void setOffertaAccettata(int id_offerta) {
		offertaRepository.updateStatoOfferta(id_offerta, StatoOfferta.ACCETTATA);
	}
	
	@Override
	public void setOffertaRifiutata(int id_offerta) {
		offertaRepository.updateStatoOfferta(id_offerta, StatoOfferta.RIFIUTATA);
	}
	
	private OffertaDTO creaOffertaDTO(Offerta offerta) {
		OffertaDTO offertaDTO = new OffertaDTO();
		offertaDTO.setId(offerta.getId());
        offertaDTO.setId_asta(offerta.getId_asta());
        offertaDTO.setId_utente(offerta.getId_utente());
        offertaDTO.setValore(offerta.getValore());
        offertaDTO.setData(offerta.getData());
        offertaDTO.setOfferente(offerta.getOfferente());
        offertaDTO.setStato(offerta.getStato());
        return offertaDTO;
	}
	
	public boolean verificaOfferta(Utente utente, Asta asta, float valore) {
	    if (asta == null || !(asta.getStato() == StatoAsta.ATTIVA)) {
	        throw new IllegalStateException("L'asta non è più attiva.");
	    }

	    if (valore <= 0) {
	        throw new IllegalArgumentException("Il valore dell'offerta è inferiore al minimo consentito.");
	    }

	    if (utente == null) {
	        throw new IllegalStateException("L'utente non è stato trovato.");
	    }
	    
	    if (asta instanceof Asta_Inversa) {
	        if (utente.getTipo() == TipoUtente.COMPRATORE) {
	            throw new IllegalStateException("In un'asta inversa, il compratore non può partecipare.");
	        }
	    } else if (asta instanceof Asta_Ribasso || asta instanceof Asta_Silenziosa) {
	        if (utente.getTipo() == TipoUtente.VENDITORE) {
	            throw new IllegalStateException("In un'asta a ribasso o silenziosa, il venditore non può partecipare come offerente.");
	        }
	    }

	    return true;
	}

}
