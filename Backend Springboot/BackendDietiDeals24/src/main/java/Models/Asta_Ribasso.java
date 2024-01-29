package Models;

import java.sql.Date;
import jakarta.persistence.Entity;

@Entity
public class Asta_Ribasso extends Asta {
	
	private int id_asta;
	private float prezzo;
	private Date timer;
	private float decremento;
	private float minimo;
	
	public Asta_Ribasso(int id_creatore, String nome, String descrizione, String categoria, byte[] foto, float prezzo, Date timer, float decremento, float minimo) {
		super(id_creatore, nome, descrizione, categoria, foto);
		this.id_asta = super.getId();
		this.prezzo = prezzo;
		this.timer = timer;
		this.decremento = decremento;
		this.minimo = minimo;
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

	public Date getTimer() {
		return timer;
	}

	public void setTimer(Date timer) {
		this.timer = timer;
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