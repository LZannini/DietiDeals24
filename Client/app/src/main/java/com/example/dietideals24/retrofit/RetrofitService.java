package com.example.dietideals24.retrofit;

import android.content.Context;
import android.util.Log;

import com.example.dietideals24.utils.ByteArrayToBase64TypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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

        httpClient.addInterceptor(new AuthInterceptor(context));

        /*httpClient.connectTimeout(30, TimeUnit.SECONDS);
        httpClient.readTimeout(30, TimeUnit.SECONDS);
        httpClient.writeTimeout(30, TimeUnit.SECONDS);*/

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