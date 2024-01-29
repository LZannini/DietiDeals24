package Models;

import jakarta.persistence.*;

@Entity
public class Compratore extends Utente {
	
	private int id_utente;
	
	public Compratore(String username, String email, String password, int id_utente) {
		super(username, email, password);
		this.id_utente = super.getId();
	}

	public int getId_utente() {
		return id_utente;
	}

	public void setId_utente(int id_utente) {
		this.id_utente = id_utente;
	}

}