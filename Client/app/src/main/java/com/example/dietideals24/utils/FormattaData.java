package com.example.dietideals24.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class FormattaData {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String formatta(String data) {
        // Aggiunge zeri o tronca i secondi frazionari per ottenere esattamente 9 cifre
        if (data.contains(".")) {
            String[] parts = data.split("\\.");
            String secondFraction = parts[1];
            if (secondFraction.length() < 9) {
                secondFraction = String.format("%-9s", secondFraction).replace(' ', '0'); // Pad con zeri
            } else {
                secondFraction = secondFraction.substring(0, 9); // Tronca a 9 cifre
            }
            data = parts[0] + "." + secondFraction;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
        LocalDateTime date = LocalDateTime.parse(data, formatter);

        LocalDate today = LocalDate.now();
        LocalDate dataDate = date.toLocalDate();

        long daysBetween = ChronoUnit.DAYS.between(dataDate, today);

        if (daysBetween == 0)
            return "Oggi, " + date.format(DateTimeFormatter.ofPattern("HH:mm"));
        else if (daysBetween == 1)
            return "Ieri, " + date.format(DateTimeFormatter.ofPattern("HH:mm"));
        else
            return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"));
    }
}

