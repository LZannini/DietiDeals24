package com.dietideals24.demo.models;

import com.dietideals24.demo.enums.Categoria;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "asta_silenziosa")
@PrimaryKeyJoinColumn(name = "id")
public class Asta_Silenziosa extends Asta {
	
	private String scadenza;
	
	public Asta_Silenziosa(int id_creatore, String nome, String descrizione, Categoria categoria, byte[] foto, String scadenza) {
		super(id_creatore, nome, descrizione, categoria, foto);
		this.scadenza = scadenza;
	}
	
	public Asta_Silenziosa() {}

	public String getScadenza() {
		return scadenza;
	}

	public void setScadenza(String scadenza) {
		this.scadenza = scadenza;
	}

}
