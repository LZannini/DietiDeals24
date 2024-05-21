package com.dietideals24.demo.models.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.dietideals24.demo.enums.Categoria;

public class Asta_RibassoDTO extends AstaDTO implements Serializable {
	
	private int id_asta;
	private float prezzo;
	private LocalDateTime timer;
	private float decremento;
	private float minimo;

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

	public LocalDateTime getTimer() {
		return timer;
	}

	public void setTimer(LocalDateTime timer) {
		this.timer = timer;
	}

	public float getDecremento() {
		return decremento;
	}

	public void setDecremento(float decremento) {
		this.decremento = decremento;
	}

	public float getMinimo() {
		return minimo;
	}

	public void setMinimo(float minimo) {
		this.minimo = minimo;
	}

}