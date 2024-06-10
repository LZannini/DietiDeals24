package com.dietideals24.demo.serviceimplements;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dietideals24.demo.enums.Categoria;
import com.dietideals24.demo.models.Asta;
import com.dietideals24.demo.models.Asta_Inversa;
import com.dietideals24.demo.models.Asta_Ribasso;
import com.dietideals24.demo.models.Asta_Silenziosa;
import com.dietideals24.demo.models.dto.AstaDTO;
import com.dietideals24.demo.models.dto.Asta_InversaDTO;
import com.dietideals24.demo.models.dto.Asta_RibassoDTO;
import com.dietideals24.demo.models.dto.Asta_SilenziosaDTO;
import com.dietideals24.demo.repository.AstaRepository;
import com.dietideals24.demo.repository.Asta_Inversa_Repository;
import com.dietideals24.demo.repository.Asta_Ribasso_Repository;
import com.dietideals24.demo.repository.Asta_Silenziosa_Repository;
import com.dietideals24.demo.service.AstaService;

import jakarta.transaction.Transactional;

@Service("AstaService")
public class AstaServiceImplements implements AstaService {
	
	@Autowired
	private AstaRepository astaRepository;
	@Autowired
	private Asta_Inversa_Repository astaInversaRepository;
	@Autowired
	private Asta_Ribasso_Repository astaAlRibassoRepository;
	@Autowired
	private Asta_Silenziosa_Repository astaSilenziosaRepository;

	@Override
	@Transactional
	public void creaAstaInversa(Asta_InversaDTO astaDTO) {
		Asta asta = new Asta();
		asta.setId_creatore(astaDTO.getIdCreatore());
		asta.setNome(astaDTO.getNome());
		asta.setDescrizione(astaDTO.getDescrizione());
		asta.setCategoria(astaDTO.getCategoria());
		asta.setFoto(astaDTO.getFoto());
		astaRepository.save(asta);
		astaInversaRepository.insertAstaInversa(asta.getId(), astaDTO.getPrezzo(), astaDTO.getScadenza());
	}
	
	@Override
	@Transactional
	public void creaAstaAlRibasso(Asta_RibassoDTO astaDTO) {
		Asta asta = new Asta();
		asta.setId_creatore(astaDTO.getIdCreatore());
		asta.setNome(astaDTO.getNome());
		asta.setDescrizione(astaDTO.getDescrizione());
		asta.setCategoria(astaDTO.getCategoria());
		asta.setFoto(astaDTO.getFoto());
		astaRepository.save(asta);
		astaAlRibassoRepository.insertAstaAlRibasso(asta.getId(), astaDTO.getPrezzo(), astaDTO.getTimer(), astaDTO.getDecremento(), astaDTO.getMinimo());
	}
	
	@Override
	@Transactional
	public void creaAstaSilenziosa(Asta_SilenziosaDTO astaDTO) {
		Asta asta = new Asta();
        asta.setId_creatore(astaDTO.getIdCreatore());
        asta.setNome(astaDTO.getNome());
        asta.setDescrizione(astaDTO.getDescrizione());
        asta.setCategoria(astaDTO.getCategoria());
        asta.setFoto(astaDTO.getFoto());
        astaRepository.save(asta);
        astaSilenziosaRepository.insertAstaSilenziosa(asta.getId(), astaDTO.getScadenza());
	}
	
	@Override
	public void rimuoviAsta(int id) {
		Asta_Inversa astaI = astaInversaRepository.getAstaInversa(id);
		Asta_Ribasso astaR = astaAlRibassoRepository.getAstaAlRibasso(id);
		Asta_Silenziosa astaS = astaSilenziosaRepository.getAstaSilenziosa(id);
		if (astaI != null) 
			astaInversaRepository.eliminaAstaInversa(id);	
		else if (astaR != null) 
			astaAlRibassoRepository.eliminaAstaAlRibasso(id);	
		else if (astaS != null) 
			astaSilenziosaRepository.eliminaAstaSilenziosa(id);
		astaRepository.eliminaAsta(id);
	}
	
	@Override
	public AstaDTO trovaAsta(int id) {
		Asta asta = astaRepository.getAsta(id);
		AstaDTO astaDTO = null;
		if (asta != null) 
			astaDTO = creaAstaDTO(asta);
		
		return astaDTO;
	}
	
	@Override
	public List<AstaDTO> trovaAsteUtente(int id_creatore) {
		List<AstaDTO> aste_trovate = new ArrayList<>();
		List<Asta> check_aste = astaRepository.filtraPerUtente(id_creatore);
		if (!check_aste.isEmpty()) {
			for (Asta a : check_aste) {
				AstaDTO astaDTO = creaAstaDTO(a);
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
				AstaDTO astaDTO = creaAstaDTO(a);
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
				AstaDTO astaDTO = creaAstaDTO(a);
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
				AstaDTO astaDTO = creaAstaDTO(a);
				aste_trovate.add(astaDTO);
			}
		}
		return aste_trovate;
	}
	
	private AstaDTO creaAstaDTO(Asta asta) {
		AstaDTO astaDTO = new AstaDTO();
		astaDTO.setNome(asta.getNome());
		astaDTO.setId(asta.getId());
		astaDTO.setId_creatore(asta.getId_creatore());
		astaDTO.setCategoria(asta.getCategoria());
		astaDTO.setDescrizione(asta.getDescrizione());
		astaDTO.setFoto(asta.getFoto());
		return astaDTO;
	}
}
