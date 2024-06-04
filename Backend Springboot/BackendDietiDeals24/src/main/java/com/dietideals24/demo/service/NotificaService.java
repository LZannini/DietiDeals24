package com.dietideals24.demo.service;

import java.util.List;

import com.dietideals24.demo.models.dto.NotificaDTO;

public interface NotificaService {
	
	List<NotificaDTO> getNotifiche(int id_utente);
	List<NotificaDTO> getNotificheLette(int id_utente);
	List<NotificaDTO> getNotificheNonLette(int id_utente);
	void setNotificaAsLetta(int id);
	void setAllNotificheAsLette(int id_utente);
	void rimuoviNotifica(int id);
	void rimuoviNotifiche(int id_utente);
	void rimuoviNotificheLette(int id_utente);
}
