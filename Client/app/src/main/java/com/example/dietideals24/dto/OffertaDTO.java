package com.example.dietideals24.dto;

import com.example.dietideals24.enums.StatoOfferta;

public class OffertaDTO {

    private int id;
    private int id_utente;
    private int id_asta;
    private float valore;
    private String data;
    private String offerente;
    private StatoOfferta stato;

    public OffertaDTO(int id_utente, int id_asta, float valore, String data) {
        this.id_utente = id_utente;
        this.id_asta = id_asta;
        this.valore = valore;
        this.data = data;
    }

    public OffertaDTO() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_utente() {
        return id_utente;
    }

    public void setId_utente(int id_utente) {
        this.id_utente = id_utente;
    }

    public int getId_asta() {
        return id_asta;
    }

    public void setId_asta(int id_asta) {
        this.id_asta = id_asta;
    }

    public float getValore() {
        return valore;
    }

    public void setValore(float valore) {
        this.valore = valore;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOfferente() {
        return offerente;
    }

    public void setOfferente(String offerente) {
        this.offerente = offerente;
    }

    public StatoOfferta getStato() {
        return stato;
    }

    public void setStato(StatoOfferta stato) {
        this.stato = stato;
    }
}
