package com.dietideals24.demo.models.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class NotificaDTO implements Serializable {
	
	private int id;
	private int id_utente;
	private String testo;
	private LocalDateTime data;
	private boolean letta;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId_utente() {
		return id_utente;
	}
	
	public void setId_utente(int id_utente) {
		this.id_utente = id_utente;
	}
	
	public String getTesto() {
		return testo;
	}
	public void setTesto(String testo) {
		this.testo = testo;
	}
	
	public LocalDateTime getData() {
		return data;
	}
	
	public void setData(LocalDateTime data) {
		this.data = data;
	}
	
	public boolean isLetta() {
		return letta;
	}
	
	public void setLetta(boolean letta) {
		this.letta = letta;
	}
}
