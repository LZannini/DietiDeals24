package com.dietideals24.demo.serviceimplements;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dietideals24.demo.models.Asta;
import com.dietideals24.demo.models.dto.AstaDTO;
import com.dietideals24.demo.repository.AstaRepository;
import com.dietideals24.demo.service.AstaService;

@Service("AstaService")
public class AstaServiceImplements implements AstaService {
	
	@Autowired
	private AstaRepository astaRepository;

	@Override
	public void creaAsta(AstaDTO astaDTO) {
		Asta asta = new Asta();
		asta.setNome(astaDTO.getNome());
		asta.setId(astaDTO.getId());
		asta.setId_creatore(astaDTO.getId_creatore());
		asta.setCategoria(astaDTO.getCategoria());
		asta.setTipo(astaDTO.getTipo());
		asta.setDescrizione(astaDTO.getDescrizione());
		asta.setFoto(astaDTO.getFoto());
		astaRepository.save(asta); 
	}

	@Override
	public void rimuoviAsta(int id) {
		astaRepository.eliminaAsta(id);
	}

	@Override
	public List<AstaDTO> trovaAstePerNome(String nome) {
		List<AstaDTO> aste_trovate = new ArrayList<>();
		List<Asta> check_aste = astaRepository.findAstaByNome(nome);
		if (!check_aste.isEmpty()) {
			for (Asta a : check_aste) {
				AstaDTO astaDTO = new AstaDTO();
				astaDTO.setNome(a.getNome());
				astaDTO.setId(a.getId());
				astaDTO.setId_creatore(a.getId_creatore());
				astaDTO.setCategoria(a.getCategoria());
				astaDTO.setTipo(a.getTipo());
				astaDTO.setDescrizione(a.getDescrizione());
				astaDTO.setFoto(a.getFoto());
				aste_trovate.add(astaDTO);
			}
		}
		return aste_trovate;
	}
}
