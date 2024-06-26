package com.dietideals24.demo.models;

import org.springframework.data.annotation.Id;

import com.dietideals24.demo.enums.Categoria;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;


@Entity
@Table(name = "asta_ribasso")
@PrimaryKeyJoinColumn(name = "id")
public class Asta_Ribasso extends Asta {
	
	private float prezzo;
	private String timer;
	private String timer_iniziale;
	private float decremento;
	private float minimo;
	
	public Asta_Ribasso(int id_creatore, String nome, String descrizione, Categoria categoria, byte[] foto, float prezzo, String timer, float decremento, float minimo) {
		super(id_creatore, nome, descrizione, categoria, foto);
		this.prezzo = prezzo;
		this.timer = timer;
		this.decremento = decremento;
		this.minimo = minimo;
	}
	
	public Asta_Ribasso() {}

	public float getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(float prezzo) {
		this.prezzo = prezzo;
	}

	public String getTimer() {
		return timer;
	}

	public void setTimer(String timer) {
		this.timer = timer;
	}
	
	public String getTimerIniziale() {
		return timer_iniziale;
	}

	public void setTimerIniziale(String timer_iniziale) {
		this.timer_iniziale = timer_iniziale;
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