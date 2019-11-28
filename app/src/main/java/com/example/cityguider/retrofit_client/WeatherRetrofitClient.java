package com.example.cityguider.retrofit_client;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherRetrofitClient {
    final static String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    public static Retrofit getClient(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
