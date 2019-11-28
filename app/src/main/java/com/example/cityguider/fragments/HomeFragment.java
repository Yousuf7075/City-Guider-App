package com.example.cityguider.fragments;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cityguider.HomeAdapter;
import com.example.cityguider.R;
import com.example.cityguider.pojo_classes.ImageCollection;
import com.example.cityguider.retrofit_client.WeatherRetrofitClient;
import com.example.cityguider.weather_response.WeatherResponse;
import com.example.cityguider.weather_response.WeatherServiceApi;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    Context context;
    private RecyclerView recyclerView;
    private String[] items;
    private HomeAdapter adapter;
    ImageCollection imageCollection;
    Geocoder geocoder;
    private final String units = "metric";
    private TextView locationTv,temperatureTV;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        geocoder = new Geocoder(context, Locale.getDefault());
        locationTv = view.findViewById(R.id.locationTV);
        temperatureTV = view.findViewById(R.id.tempTv);

        recyclerView = view.findViewById(R.id.homeRecyclerView);
        items = getResources().getStringArray(R.array.home_item_name);
        adapter = new HomeAdapter(Arrays.asList(items), ImageCollection.getIconList(),context);
        GridLayoutManager glm = new GridLayoutManager(context, 3);
        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(adapter);
    }

    public void onReceivedData(double latitude, double longitude) {
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude,longitude,1);
            Address address = addressList.get(0);
            String addressLine = address.getAddressLine(0);
            locationTv.setText(addressLine);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //get current weather codes
        String apiKey = getString(R.string.weather_api_key);
        String endUrl = String.format("weather?lat=%f&lon=%f&units=%s&appid=%s",latitude,longitude,units,apiKey);
        WeatherServiceApi serviceApi = WeatherRetrofitClient.getClient()
                .create(WeatherServiceApi.class);
        serviceApi.getWeatherResponse(endUrl).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()){
                    WeatherResponse weatherResponse = response.body();
                    double currentTemp = weatherResponse.getMain().getTemp();
                    temperatureTV.setText(currentTemp +" \u2103");
                }
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("weather failed",t.getLocalizedMessage());
            }
        });
    }
}
