package com.dietideals24.demo.models;

import java.sql.Date;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class Offerta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int id_utente;
	private int id_asta;
	private float valore;
	private LocalDateTime data;
	
	public Offerta(int id, int id_utente, int id_asta, float valore, LocalDateTime data) {
		this.id = id;
		this.id_utente = id_utente;
		this.id_asta = id_asta;
		this.valore = valore;
		this.data = data;
	}
	
	public Offerta() {
		
	}
	
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

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}
	
}