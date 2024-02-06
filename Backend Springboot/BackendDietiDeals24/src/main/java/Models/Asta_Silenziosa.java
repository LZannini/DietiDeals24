package Models;

import java.sql.Date;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;

@Entity
public class Asta_Silenziosa extends Asta {
	
	private int id_asta;
	private LocalDateTime scadenza;
	
	public Asta_Silenziosa(int id_creatore, String nome, String descrizione, String categoria, byte[] foto, LocalDateTime scadenza) {
		super(id_creatore, nome, descrizione, categoria, foto);
		this.id_asta = super.getId();
		this.scadenza = scadenza;
	}

	public int getId_asta() {
		return id_asta;
	}

	public void setId_asta(int id_asta) {
		this.id_asta = id_asta;
	}

	public LocalDateTime getScadenza() {
		return scadenza;
	}

	public void setScadenza(LocalDateTime scadenza) {
		this.scadenza = scadenza;
	}

}
