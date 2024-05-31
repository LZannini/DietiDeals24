package com.example.dietideals24.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Asta_SilenziosaDTO extends AstaDTO implements Serializable {

    private int id_asta;
    private LocalDateTime scadenza;

    public int getId_asta() {
        return id_asta;
    }

    public void setId_asta(int id_asta) {
        this.id_asta = id_asta;
    }

    public LocalDateTime getScadenza() {
        return scadenza;
    }

    public void setScadenza(LocalDateTime scadenza) {
        this.scadenza = scadenza;
    }

}