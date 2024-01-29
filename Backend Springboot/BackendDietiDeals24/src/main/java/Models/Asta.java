package Models;

import jakarta.persistence.*;

@Entity
public class Asta {

	private @Id int id;
	private int id_creatore;
	private String nome;
	private String descrizione;
	private String categoria;
	private byte[] foto;
	
	public Asta(int id_creatore, String nome, String descrizione, String categoria, byte[] foto) {
		this.id_creatore = id_creatore;
		this.nome = nome;
		this.descrizione = descrizione;
		this.categoria = categoria;
		this.foto = foto;
	}
	
	public Asta() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_creatore() {
		return id_creatore;
	}

	public void setId_creatore(int id_creatore) {
		this.id_creatore = id_creatore;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

}
