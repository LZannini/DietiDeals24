package com.dietideals24.demo.models;

import com.dietideals24.demo.enums.Categoria;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;


@Entity
@PrimaryKeyJoinColumn(name = "id_asta")
public class Asta_Inversa extends Asta {
	
	private float prezzo;
	private String scadenza;
	
	public Asta_Inversa(int id_creatore, String nome, String descrizione, Categoria categoria, byte[] foto, float prezzo, String scadenza) {
		super(id_creatore, nome, descrizione, categoria, foto);
		this.prezzo = prezzo;
		this.scadenza = scadenza; 
	}
	
	public Asta_Inversa() {
		
	}

	public float getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(float prezzo) {
		this.prezzo = prezzo;
	}

	public String getScadenza() {
		return scadenza;
	}

	public void setScadenza(String scadenza) {
		this.scadenza = scadenza;
	}
}
