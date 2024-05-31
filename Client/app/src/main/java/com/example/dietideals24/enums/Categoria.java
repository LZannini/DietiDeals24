package com.example.dietideals24.enums;

public enum Categoria {
    ABBIGLIAMENTO("Abbigliamento"),
    ACCESSORI("Accessori"),
    ARREDAMENTO("Arredamento"),
    ARTE("Arte"),
    COLLEZIONISMO("Collezionismo"),
    CUCINA("Cucina"),
    ELETTRONICA("Elettronica"),
    LETTURA("Lettura"),
    MOTORI("Motori"),
    SERVIZI("Servizi"),
    SPORT("Sport"),
    SVAGO("Svago");

    private final String displayName;

    Categoria(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

