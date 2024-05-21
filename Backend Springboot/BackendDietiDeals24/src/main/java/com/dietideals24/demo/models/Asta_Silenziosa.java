package com.dietideals24.demo.models;

import java.time.LocalDateTime;
import com.dietideals24.demo.enums.Categoria;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@PrimaryKeyJoinColumn(name = "id_asta")
public class Asta_Silenziosa extends Asta {
	
	private LocalDateTime scadenza;
	
	public Asta_Silenziosa(int id_creatore, String nome, String descrizione, Categoria categoria, byte[] foto,LocalDateTime scadenza) {
		super(id_creatore, nome, descrizione, categoria, foto);;
		this.scadenza = scadenza;
	}
	
	public Asta_Silenziosa() {}

	public LocalDateTime getScadenza() {
		return scadenza;
	}

	public void setScadenza(LocalDateTime scadenza) {
		this.scadenza = scadenza;
	}

}
