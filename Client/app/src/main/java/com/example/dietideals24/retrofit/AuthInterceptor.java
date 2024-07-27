package com.example.dietideals24.retrofit;

import android.content.Context;

import com.example.dietideals24.security.TokenManager;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class AuthInterceptor implements Interceptor {
    private TokenManager tokenManager;

    public AuthInterceptor(Context context) {
        this.tokenManager = new TokenManager(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String url = originalRequest.url().toString();

        if (url.contains("/auth") || url.contains("/utente/registra")) {
            return chain.proceed(originalRequest);
        }

        String token = tokenManager.getToken();
        if (token != null) {
            Request.Builder requestBuilder = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + token);
            Request request = requestBuilder.build();
            return chain.proceed(request);
        }

        return chain.proceed(originalRequest);
    }
}
