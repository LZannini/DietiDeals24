package com.dietideals24.demo.models.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.dietideals24.demo.enums.StatoOfferta;

@SuppressWarnings("serial")
public class OffertaDTO implements Serializable {
	
	private int id;
	private int id_utente;
	private int id_asta;
	private float valore;
	private String data;
	private String offerente;
	private StatoOfferta stato;

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

	public int getId_asta() {
		return id_asta;
	}

	public void setId_asta(int id_asta) {
		this.id_asta = id_asta;
	}

	public float getValore() {
		return valore;
	}

	public void setValore(float valore) {
		this.valore = valore;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getOfferente() {
		return offerente;
	}

	public void setOfferente(String offerente) {
		this.offerente = offerente;
	}

	public StatoOfferta getStato() {
		return stato;
	}

	public void setStato(StatoOfferta stato) {
		this.stato = stato;
	}
}
