package com.example.dietideals24.retrofit;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;

    public RetrofitService() {
        inizializeRetrofit();
    }

    private void inizializeRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.178.119:8080")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

}
