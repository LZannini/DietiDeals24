package com.example.dietideals24.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

public class TokenManager {
    private static final String PREF_NAME = "JwtTokenPrefs";
    private static final String TOKEN_KEY = "jwtToken";
    private SharedPreferences prefs;

    public TokenManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }

    public String getToken() {
        return prefs.getString(TOKEN_KEY, null);
    }

    public void deleteToken() {
        prefs.edit().remove(TOKEN_KEY).apply();
    }

    public int getUserIdFromToken() {
        String token = getToken();
        if (token != null) {
            String[] parts = token.split("\\.");
            if (parts.length == 3) {
                String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE));
                try {
                    JSONObject jsonPayload = new JSONObject(payload);
                    return jsonPayload.getInt("sub");  // Assumendo che l'ID sia nel campo "sub"
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;  // Ritorna un valore invalido se non riesce a estrarre l'ID
    }
}
