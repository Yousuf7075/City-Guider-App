package com.example.cityguider.direction_response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface DirectionServiceApi {
    @GET
    Call<DirectionResponse> getDirectionResponse(@Url String url);
}
