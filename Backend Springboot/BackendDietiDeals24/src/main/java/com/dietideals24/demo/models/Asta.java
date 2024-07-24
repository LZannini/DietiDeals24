package com.dietideals24.demo.models;

import com.dietideals24.demo.enums.Categoria;
import com.dietideals24.demo.enums.StatoAsta;

import jakarta.persistence.*;

@Entity
@Table(name = "asta")
@Inheritance(strategy = InheritanceType.JOINED)
public class Asta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int id_creatore;
	private Integer vincitore;
	private String nome;
	private String descrizione;
	private Categoria categoria;
	private byte[] foto;
	private StatoAsta stato;
	
	public Asta(int id_creatore, String nome, String descrizione, Categoria categoria, byte[] foto) {
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

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}
	
	public StatoAsta getStato() {
		return stato;
	}
	
	public void setStato(StatoAsta stato) {
		this.stato = stato;
	}

	public Integer getVincitore() {
		return vincitore;
	}

	public void setVincitore(Integer vincitore) {
		this.vincitore = vincitore;
	}

}
