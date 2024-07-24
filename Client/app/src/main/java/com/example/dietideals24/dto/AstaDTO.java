package com.example.dietideals24.dto;

import com.example.dietideals24.enums.Categoria;
import com.example.dietideals24.enums.StatoAsta;

import java.io.Serializable;

public class AstaDTO implements Serializable {

    private int id;
    private int id_creatore;
    private Integer vincitore;
    private String nome;
    private String descrizione;
    private String tipo;
    private Categoria categoria;
    private byte[] foto;
    private StatoAsta stato;

    public int getID(){
        return id;
    }

    public void setID(int id){
        this.id = id;
    }

    public int getId_creatore(){
        return id_creatore;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getDescrizione(){
        return descrizione;
    }

    public void setDescrizione(String descrizione){
        this.descrizione = descrizione;
    }

    public Categoria getCategoria(){
        return categoria;
    }

    public void setCategoria(Categoria categoria){
        this.categoria = categoria;
    }

    public byte[] getFoto(){
        return foto;
    }

    public void setFoto(byte[] foto){
        this.foto = foto;
    }
    public String getTipo(){
        return tipo;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
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
