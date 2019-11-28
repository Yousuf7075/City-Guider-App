package com.example.cityguider.api_response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface PlaceServiceApi {
    @GET
    Call<PlaceApiResponse> getNearByPlaces(@Url String url);
}
