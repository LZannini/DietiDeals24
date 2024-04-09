package com.dietideals24.demo.serviceimplements;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dietideals24.demo.enums.Categoria;
import com.dietideals24.demo.enums.TipoAsta;
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
	public List<AstaDTO> trovaAsteUtente(int id_creatore) {
		List<AstaDTO> aste_trovate = new ArrayList<>();
		List<Asta> check_aste = astaRepository.filtraPerUtente(id_creatore);
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

	@Override
	public List<AstaDTO> trovaAstePerTipo(TipoAsta tipo) {
		List<AstaDTO> aste_trovate = new ArrayList<>();
		List<Asta> check_aste = astaRepository.filtraPerTipo(tipo);
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

	@Override
	public List<AstaDTO> trovaAstePerParolaChiave(String chiave) {
		List<AstaDTO> aste_trovate = new ArrayList<>();
		List<Asta> check_aste = astaRepository.filtraPerParolaChiave(chiave);
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

	@Override
	public List<AstaDTO> trovaAstePerCategoria(Categoria categoria) {
		List<AstaDTO> aste_trovate = new ArrayList<>();
		List<Asta> check_aste = astaRepository.filtraPerCategoria(categoria);
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

	@Override
	public List<AstaDTO> trovaAstePerParolaChiaveAndCategoria(String chiave, Categoria categoria) {
		List<AstaDTO> aste_trovate = new ArrayList<>();
		List<Asta> check_aste = astaRepository.filtraPerCategoriaAndParoleChiave(chiave, categoria);
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
