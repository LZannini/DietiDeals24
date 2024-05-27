package com.example.dietideals24.models;

import com.example.dietideals24.enums.Categoria;

import java.time.LocalDateTime;

public class Asta_Ribasso extends Asta {

    private float prezzo;
    private LocalDateTime timer;
    private float decremento;
    private float minimo;

    public Asta_Ribasso(int id_creatore, String nome, String descrizione, Categoria categoria, byte[] foto, float prezzo, LocalDateTime timer, float decremento, float minimo) {
        super(id_creatore, nome, descrizione, categoria, foto);
        this.prezzo = prezzo;
        this.timer = timer;
        this.decremento = decremento;
        this.minimo = minimo;
    }

    public Asta_Ribasso() {}

    public float getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(float prezzo) {
        this.prezzo = prezzo;
    }

    public LocalDateTime getTimer() {
        return timer;
    }

    public void setTimer(LocalDateTime timer) {
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