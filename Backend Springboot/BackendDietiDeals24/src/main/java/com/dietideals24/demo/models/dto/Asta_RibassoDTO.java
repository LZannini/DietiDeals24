package com.dietideals24.demo.models.dto;

import com.dietideals24.demo.enums.Categoria;

public class Asta_RibassoDTO {

    private int idCreatore;
    private String nome;
    private String descrizione;
    private Categoria categoria;
    private byte[] foto;
    private int id_asta;
    private float prezzo;
    private String timer;
    private float decremento;
    private float minimo;

    public int getId_asta() {
        return id_asta;
    }

    public void setId_asta(int id_asta) {
        this.id_asta = id_asta;
    }

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
    public Categoria getCategoria() {return categoria;}
    public void setCategoria(Categoria categoria) {this.categoria = categoria;}

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public float getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(float prezzo) {
        this.prezzo = prezzo;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
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