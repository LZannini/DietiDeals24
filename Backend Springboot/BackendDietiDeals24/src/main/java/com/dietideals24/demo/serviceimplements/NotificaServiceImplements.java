package com.dietideals24.demo.serviceimplements;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dietideals24.demo.models.Notifica;
import com.dietideals24.demo.models.dto.NotificaDTO;
import com.dietideals24.demo.repository.NotificaRepository;
import com.dietideals24.demo.service.NotificaService;

@Service("NotificaService")
public class NotificaServiceImplements implements NotificaService {
	
	@Autowired
	private NotificaRepository notificaRepository;

	@Override
	public List<NotificaDTO> getNotifiche(int id_utente) {
		List<Notifica> notifiche = notificaRepository.trovaNotifiche(id_utente);
		if (notifiche.isEmpty())
			return null;
		List<NotificaDTO> notificheDTO = new ArrayList<>();
		for (Notifica n : notifiche) {
			NotificaDTO notificaDTO = creaNotificaDTO(n);
			notificheDTO.add(notificaDTO);
		}
		return notificheDTO;
	}

	@Override
	public List<NotificaDTO> getNotificheLette(int id_utente) {
		List<Notifica> notifiche = notificaRepository.trovaNotificheLette(id_utente);
		if (notifiche.isEmpty())
			return null;
		List<NotificaDTO> notificheDTO = new ArrayList<>();
		for (Notifica n : notifiche) {
			NotificaDTO notificaDTO = creaNotificaDTO(n);
			notificheDTO.add(notificaDTO);
		}
		return notificheDTO;
	}

	@Override
	public List<NotificaDTO> getNotificheNonLette(int id_utente) {
		List<Notifica> notifiche = notificaRepository.trovaNotificheNonLette(id_utente);
		if (notifiche.isEmpty())
			return null;
		List<NotificaDTO> notificheDTO = new ArrayList<>();
		for (Notifica n : notifiche) {
			NotificaDTO notificaDTO = creaNotificaDTO(n);
			notificheDTO.add(notificaDTO);
		}
		return notificheDTO;
	}
	
	@Override
	public void setAllNotificheAsLette(int id_utente) {
		notificaRepository.segnaAllNotificheComeLette(id_utente);
	}
	
	@Override
	public void setNotificaAsLetta(int id) {
		notificaRepository.segnaNotificaComeLetta(id);
	}
	
	@Override
	public void rimuoviNotifica(int id) {
		notificaRepository.eliminaNotifica(id);
	}

	@Override
	public void rimuoviNotifiche(int id_utente) {
		notificaRepository.eliminaNotifiche(id_utente);
	}

	@Override
	public void rimuoviNotificheLette(int id_utente) {
		notificaRepository.eliminaNotificheLette(id_utente);
	}
	
	private NotificaDTO creaNotificaDTO(Notifica notifica) {
		NotificaDTO notificaDTO = new NotificaDTO();
		notificaDTO.setId(notifica.getId());
		notificaDTO.setId_utente(notifica.getId_utente());
		if(notifica.getId_asta() != null) {
			notificaDTO.setId_asta(notifica.getId_asta());
		} else {
			notificaDTO.setId_asta(0);
		}
		notificaDTO.setTesto(notifica.getTesto());
		notificaDTO.setData(notifica.getData());
		notificaDTO.setLetta(notifica.isLetta());
		return notificaDTO;
	}
}
