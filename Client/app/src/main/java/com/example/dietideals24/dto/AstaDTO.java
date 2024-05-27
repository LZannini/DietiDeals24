package com.example.dietideals24.dto;

import java.io.Serializable;

public class AstaDTO implements Serializable {

    private int id;
    private int id_creatore;
    private String nome;
    private String descrizione;
    private String categoria;
    private byte[] foto;

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

    public String getCategoria(){
        return categoria;
    }

    public void setCategoria(String categoria){
        this.categoria = categoria;
    }

    public byte[] getFoto(){
        return foto;
    }

    public void setFoto(byte[] foto){
        this.foto = foto;
    }

}
