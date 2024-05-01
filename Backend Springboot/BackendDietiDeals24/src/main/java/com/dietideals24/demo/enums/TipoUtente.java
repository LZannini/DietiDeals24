package com.dietideals24.demo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoUtente {
	COMPRATORE("COMPRATORE"),
    VENDITORE("VENDITORE"),
    COMPLETO("COMPLETO");

    private String value;

    TipoUtente(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static TipoUtente fromValue(String value) {
        for (TipoUtente tipo : values()) {
            if (tipo.value.equalsIgnoreCase(value)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Valore non valido per TipoUtente: " + value);
    }
}
