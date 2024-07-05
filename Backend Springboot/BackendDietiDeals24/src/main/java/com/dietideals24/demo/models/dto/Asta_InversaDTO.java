package com.dietideals24.demo.models.dto;

import com.dietideals24.demo.enums.Categoria;

public class Asta_InversaDTO {
	private int idCreatore;
    private String nome;
    private String descrizione;
    private Categoria categoria;
    private byte[] foto;
    private int id_asta;
	private float prezzo;
	private Float offertaMinore;
	private String scadenza;
	
	public int getIdCreatore() {
		return idCreatore;
	}

	public void setIdCreatore(int idCreatore) {
		this.idCreatore = idCreatore;
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

	public String getScadenza() {
		return scadenza;
	}

	public void setScadenza(String scadenza) {
		this.scadenza = scadenza;
	}

	public Float getOffertaMinore() {
		return offertaMinore;
	}

	public void setOffertaMinore(Float offertaMinore) {
		this.offertaMinore = offertaMinore;
	}
}