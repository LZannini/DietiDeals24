package com.dietideals24.demo.models.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.dietideals24.demo.enums.Categoria;

public class Asta_InversaDTO extends AstaDTO implements Serializable {
	
	private int id_asta;
	private float prezzo;
	private LocalDateTime scadenza;

	public int getId_asta() {
		return id_asta;
	}

	public void setId_asta(int id_asta) {
		this.id_asta = id_asta;
	}

	public float getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(float prezzo) {
		this.prezzo = prezzo;
	}

	public LocalDateTime getScadenza() {
		return scadenza;
	}

	public void setScadenza(LocalDateTime scadenza) {
		this.scadenza = scadenza;
	}
}