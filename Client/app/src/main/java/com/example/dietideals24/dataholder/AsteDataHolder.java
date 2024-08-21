package com.example.dietideals24.dataholder;

import androidx.lifecycle.ViewModel;

import com.example.dietideals24.models.Asta;

import java.util.ArrayList;
import java.util.List;

public class AsteDataHolder extends ViewModel {
    private static AsteDataHolder instance;
    private List<Asta> listaAste;

    private AsteDataHolder() {
        listaAste = new ArrayList<>();
    }

    public static synchronized AsteDataHolder getInstance() {
        if (instance == null) {
            instance = new AsteDataHolder();
        }
        return instance;
    }

    public List<Asta> getListaAste() {
        return listaAste;
    }

    public void setListaAste(List<Asta> listaAste) {
        this.listaAste = listaAste;
    }
}
