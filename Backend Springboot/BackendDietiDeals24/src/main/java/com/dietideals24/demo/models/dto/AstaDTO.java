package com.dietideals24.demo.models.dto;

import java.io.Serializable;

import com.dietideals24.demo.enums.Categoria;
import com.dietideals24.demo.enums.StatoAsta;

@SuppressWarnings("serial")
public class AstaDTO implements Serializable {
	
	private int id;
	private int id_creatore;
	private String nome;
	private String descrizione;
	private String tipo;
	private Categoria categoria;
	private byte[] foto;
	private StatoAsta stato;

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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public StatoAsta getStato() {
		return stato;
	}

	public void setStato(StatoAsta stato) {
		this.stato = stato;
	}
}
