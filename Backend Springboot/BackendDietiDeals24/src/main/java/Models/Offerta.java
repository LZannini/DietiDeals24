package Models;

import java.sql.Date;

import jakarta.persistence.*;

@Entity
public class Offerta {
	
	private int id_utente;
	private int id_asta;
	private float valore;
	private Date data;
	
	public Offerta(int id_utente, int id_asta, float valore, Date data) {
		this.id_utente = id_utente;
		this.id_asta = id_asta;
		this.valore = valore;
		this.data = data;
	}
	
	public Offerta() {
		
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

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
}