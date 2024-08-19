package com.example.dietideals24;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

public class DettagliAstaActivityTest {
    @Test
    public void testEntrambiNulli() {
        String risultato = DettagliAstaActivity.calcolaTempoRimanente(null ,null);
        assertEquals("Data non valida", risultato);
    }

    @Test
    public void testScadenzaNullaAttualeValida() {
        LocalDateTime dataAttuale = LocalDateTime.now();
        String risultato = DettagliAstaActivity.calcolaTempoRimanente(null ,dataAttuale);
        assertEquals("Data non valida", risultato);
    }

    @Test
    public void testScadenzaValidaAttualeNulla() {
        LocalDateTime dataScadenza = LocalDateTime.now();
        String risultato = DettagliAstaActivity.calcolaTempoRimanente(dataScadenza,null);
        assertEquals("Data non valida", risultato);
    }

    @Test
    public void testEntrambiValideAttualeDopoScadenza() {
        LocalDateTime dataScadenza = LocalDateTime.now().minusDays(1);
        LocalDateTime dataAttuale = LocalDateTime.now();
        String risultato = DettagliAstaActivity.calcolaTempoRimanente(dataScadenza,dataAttuale);
        assertEquals("Scaduta", risultato);
    }

    @Test
    public void testEntrambiValideScadenzaMaggioreUnAnno() {
        LocalDateTime dataScadenza = LocalDateTime.now();
        LocalDateTime dataAttuale = LocalDateTime.now().minusYears(2);
        Period period = Period.between(dataAttuale.toLocalDate(), dataScadenza.toLocalDate());
        String risultato = DettagliAstaActivity.calcolaTempoRimanente(dataScadenza,dataAttuale);
        assertEquals(period.getYears() + " anni, " + period.getMonths() + " mesi", risultato);
    }

    @Test
    public void testEntrambiValideScadenzaMaggioreUnMese() {
        LocalDateTime dataScadenza = LocalDateTime.now();
        LocalDateTime dataAttuale = LocalDateTime.now().minusMonths(2);
        Period period = Period.between(dataAttuale.toLocalDate(), dataScadenza.toLocalDate());
        String risultato = DettagliAstaActivity.calcolaTempoRimanente(dataScadenza,dataAttuale);
        assertEquals(period.getMonths() + " mesi, " + period.getDays() + " giorni", risultato);
    }

    @Test
    public void testEntrambiValideScadenzaMaggioreUnGiorno() {
        LocalDateTime dataScadenza = LocalDateTime.now();
        LocalDateTime dataAttuale = LocalDateTime.now().minusDays(2);
        Period period = Period.between(dataAttuale.toLocalDate(), dataScadenza.toLocalDate());
        String risultato = DettagliAstaActivity.calcolaTempoRimanente(dataScadenza,dataAttuale);
        assertEquals(period.getDays() + " giorni", risultato);
    }

    @Test
    public void testEntrambiValideScadenzaMaggioreUnOra() {
        LocalDateTime dataScadenza = LocalDateTime.now();
        LocalDateTime dataAttuale = LocalDateTime.now().minusHours(2);
        Duration duration = Duration.between(dataAttuale.toLocalTime(), dataScadenza.toLocalTime());
        String risultato = DettagliAstaActivity.calcolaTempoRimanente(dataScadenza,dataAttuale);
        assertEquals(duration.toHours() + " ore", risultato);
    }

    @Test
    public void testEntrambiValideScadenzaMaggioreUnMinuto() {
        LocalDateTime dataScadenza = LocalDateTime.now();
        LocalDateTime dataAttuale = LocalDateTime.now().minusMinutes(2);
        Duration duration = Duration.between(dataAttuale.toLocalTime(), dataScadenza.toLocalTime());
        String risultato = DettagliAstaActivity.calcolaTempoRimanente(dataScadenza,dataAttuale);
        assertEquals(duration.toMinutes() + " minuti", risultato);
    }

    @Test
    public void testEntrambiValideScadenzaMaggioreUnSecondo() {
        LocalDateTime dataScadenza = LocalDateTime.now();
        LocalDateTime dataAttuale = LocalDateTime.now().minusSeconds(1);
        String risultato = DettagliAstaActivity.calcolaTempoRimanente(dataScadenza,dataAttuale);
        assertEquals("meno di un minuto", risultato);
    }

    @Test
    public void testEntrambiValideScadenzaUgualeAttuale() {
        LocalDateTime dataScadenza = LocalDateTime.now();
        LocalDateTime dataAttuale = LocalDateTime.now();
        String risultato = DettagliAstaActivity.calcolaTempoRimanente(dataScadenza,dataAttuale);
        assertEquals("Scaduta", risultato);
    }
}