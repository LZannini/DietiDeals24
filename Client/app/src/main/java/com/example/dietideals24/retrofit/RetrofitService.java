package com.example.dietideals24.retrofit;

import android.content.Context;

import com.example.dietideals24.security.TokenManager;
import com.example.dietideals24.utils.ByteArrayToBase64TypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private static RetrofitService instance;
    private static Retrofit retrofit;
    private Gson gson;
    private Context context;

    public RetrofitService(Context context) {
        this.context = context;
        inizializeRetrofit();
    }

    public static synchronized RetrofitService getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitService(context);
        }
        return instance;
    }

    private void inizializeRetrofit() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            TokenManager tokenManager = new TokenManager(context);
            String token = tokenManager.getToken();
<<<<<<< Updated upstream

=======
<<<<<<< Updated upstream
<<<<<<< HEAD
            Log.d("TokenDebug", "Interceptor chiamato, token: " + token);
=======
>>>>>>> Stashed changes
>>>>>>> Stashed changes
            if (token != null) {
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", "Bearer " + token);
                Request request = requestBuilder.build();

<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
                Log.d("RetrofitService", "Request Headers: " + request.headers().toString());
=======

            if (token != null) {
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", "Bearer " + token);
                Request request = requestBuilder.build();

>>>>>>> a79df5e6f12bd57ede716f2a587b09c7a20fbb3e
=======
>>>>>>> Stashed changes
>>>>>>> Stashed changes
                return chain.proceed(request);
            }

            return chain.proceed(original);
        });

        gson = new GsonBuilder()
                .registerTypeAdapter(byte[].class, new ByteArrayToBase64TypeAdapter())
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.31.15:8080")
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static Retrofit getRetrofit(Context context) {
        if (retrofit == null) {
            new RetrofitService(context);
        }
        return retrofit;
    }

    public static void resetInstance() {
        instance = null;
        retrofit = null;
    }

}
