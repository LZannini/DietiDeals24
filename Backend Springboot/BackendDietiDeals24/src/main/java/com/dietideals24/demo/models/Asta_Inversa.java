package com.dietideals24.demo.models;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.dietideals24.demo.enums.Categoria;

import jakarta.persistence.Entity;

@Entity
public class Asta_Inversa extends Asta {
	
	private int id_asta;
	private float prezzo;
	private LocalDateTime scadenza;
	
	public Asta_Inversa(int id_creatore, String nome, String descrizione, Categoria categoria, byte[] foto, float prezzo,LocalDateTime scadenza) {
		super(id_creatore, nome, descrizione, categoria, foto);
		this.id_asta = super.getId();
		this.prezzo = prezzo;
		this.scadenza = scadenza; 
	}
	
	public Asta_Inversa() {
		
	}

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
