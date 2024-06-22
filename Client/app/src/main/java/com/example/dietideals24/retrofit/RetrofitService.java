package com.example.dietideals24.retrofit;

import com.example.dietideals24.utils.ByteArrayToBase64TypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;
    private Gson gson;

    public RetrofitService() {
        inizializeRetrofit();
    }

    private void inizializeRetrofit() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        gson = new GsonBuilder()
                .registerTypeAdapter(byte[].class, new ByteArrayToBase64TypeAdapter())
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.115:8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

}
