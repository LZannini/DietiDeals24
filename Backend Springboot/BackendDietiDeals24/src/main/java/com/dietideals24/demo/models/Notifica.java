package com.dietideals24.demo.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Notifica {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int id_utente;
	private Integer id_asta;
	private String testo;
	private LocalDateTime data;
	private boolean letta;
	
	public Notifica(int id, int id_utente, String testo, LocalDateTime data, boolean letta) {
		this.id = id;
		this.id_utente = id_utente;
		this.testo = testo;
		this.data = data;
		this.letta = letta;
	}
		
	public Notifica() {}

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

	public Integer getId_asta() {
		return id_asta;
	}

	public void setId_asta(Integer id_asta) {
		this.id_asta = id_asta;
	}
	
}
