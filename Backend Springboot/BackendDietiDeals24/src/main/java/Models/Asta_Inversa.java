package Models;

import java.sql.Date;
import jakarta.persistence.Entity;

@Entity
public class Asta_Inversa extends Asta {
	
	private int id_asta;
	private float prezzo;
	private Date scadenza;
	
	public Asta_Inversa(int id_creatore, String nome, String descrizione, String categoria, byte[] foto, float prezzo, Date scadenza) {
		super(id_creatore, nome, descrizione, categoria, foto);
		this.id_asta = super.getId();
		this.prezzo = prezzo;
		this.scadenza = scadenza;
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

	public Date getScadenza() {
		return scadenza;
	}

	public void setScadenza(Date scadenza) {
		this.scadenza = scadenza;
	}

}
