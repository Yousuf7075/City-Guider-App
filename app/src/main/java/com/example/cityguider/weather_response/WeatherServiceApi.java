package com.example.cityguider.weather_response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WeatherServiceApi {
    @GET
    Call<WeatherResponse> getWeatherResponse(@Url String url);
}
