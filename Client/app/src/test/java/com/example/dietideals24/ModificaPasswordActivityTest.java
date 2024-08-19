package com.example.dietideals24;

import static org.junit.Assert.*;

import org.junit.Test;


public class ModificaPasswordActivityTest {

    @Test
    public void testPasswordValida() {
        assertTrue(ModificaPasswordActivity.passwordValida("Password1.", "Password1.", "VecchiaPassword1."));
    }

    @Test
    public void testNuovaUgualeAVecchia() {
        assertFalse(ModificaPasswordActivity.passwordValida("Password1.", "Password1.", "Password1."));
        assertEquals("La nuova password deve essere diversa dalla vecchia password.", ModificaPasswordActivity.getMessaggioErrore());
    }

    @Test
    public void testPasswordCorta() {
        assertFalse(ModificaPasswordActivity.passwordValida("Pass1.", "Pass1.", "Password1."));
        assertEquals("La nuova password deve contenere almeno 8 caratteri.", ModificaPasswordActivity.getMessaggioErrore());
    }

    @Test
    public void testNessunaMaiuscola() {
        assertFalse(ModificaPasswordActivity.passwordValida("password1.", "password1.", "VecchiaPassword1."));
        assertEquals("La nuova password deve contenere almeno una lettera maiuscola.", ModificaPasswordActivity.getMessaggioErrore());
    }

    @Test
    public void testNessunNumero() {
        assertFalse(ModificaPasswordActivity.passwordValida("Password.", "Password.", "VecchiaPassword1."));
        assertEquals("La nuova password deve contenere almeno un numero.", ModificaPasswordActivity.getMessaggioErrore());
    }

    @Test
    public void testNessunCarattereSpeciale() {
        assertFalse(ModificaPasswordActivity.passwordValida("Password1", "Password1", "VecchiaPassword1."));
        assertEquals("La nuova password deve contenere almeno un carattere speciale.", ModificaPasswordActivity.getMessaggioErrore());
    }

    @Test
    public void testNuovaDiversaDaConferma() {
        assertFalse(ModificaPasswordActivity.passwordValida("Password1.", "Password2.", "VecchiaPassword1."));
        assertEquals("Le password non corrispondono, riprova.", ModificaPasswordActivity.getMessaggioErrore());
    }

    @Test
    public void testPasswordComplesse() {
        assertTrue(ModificaPasswordActivity.passwordValida("C0mpl3x!Pass", "C0mpl3x!Pass", "VecchiaPassword1."));
    }

    @Test
    public void testPassword8Caretteri() {
        assertTrue(ModificaPasswordActivity.passwordValida("Pass123.", "Pass123.", "VecchiaPassword1."));
    }

    @Test
    public void testPasswordLunga() {
        String nuovaPassword = "A" + "a".repeat(9998) + "1.";
        assertTrue(ModificaPasswordActivity.passwordValida(nuovaPassword, nuovaPassword, "VecchiaPassword1."));
    }
}