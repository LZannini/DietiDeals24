package com.example.dietideals24.models;

import com.example.dietideals24.enums.Categoria;

import java.time.LocalDateTime;

public class Asta_Silenziosa extends Asta {

    private LocalDateTime scadenza;

    public Asta_Silenziosa(int id_creatore, String nome, String descrizione, Categoria categoria, byte[] foto, LocalDateTime scadenza) {
        super(id_creatore, nome, descrizione, categoria, foto);;
        this.scadenza = scadenza;
    }

    public Asta_Silenziosa() {}

    public LocalDateTime getScadenza() {
        return scadenza;
    }

    public void setScadenza(LocalDateTime scadenza) {
        this.scadenza = scadenza;
    }

}

