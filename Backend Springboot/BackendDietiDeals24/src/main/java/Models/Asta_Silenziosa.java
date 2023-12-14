package Models;

import java.sql.Date;
import jakarta.persistence.Entity;

@Entity
public class Asta_Silenziosa extends Asta {
	
	private int id_asta;
	private Date scadenza;
	
	public Asta_Silenziosa(int id_creatore, String nome, String descrizione, String categoria, byte[] foto, Date scadenza) {
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

	public Date getScadenza() {
		return scadenza;
	}

	public void setScadenza(Date scadenza) {
		this.scadenza = scadenza;
	}

}
