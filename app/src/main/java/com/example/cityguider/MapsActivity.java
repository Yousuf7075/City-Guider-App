package com.example.cityguider;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cityguider.direction_response.DirectionResponse;
import com.example.cityguider.direction_response.DirectionServiceApi;
import com.example.cityguider.direction_response.Step;
import com.example.cityguider.retrofit_client.RetrofitClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private double originLat, originLon, destinationLat, destinationLon;
    private String placeName;
    private TextView distanceTv, timeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        distanceTv = findViewById(R.id.distanceTV);
        timeTv = findViewById(R.id.timeTV);

        Intent receiveIntent = getIntent();
        originLat = receiveIntent.getDoubleExtra("olat",0.0);
        originLon = receiveIntent.getDoubleExtra("olon",0.0);
        destinationLat = receiveIntent.getDoubleExtra("dlat",0.0);
        destinationLon = receiveIntent.getDoubleExtra("dlon",0.0);
        placeName = receiveIntent.getStringExtra("name");
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
        LatLng myLoc = new LatLng(originLat, originLon);
        LatLng desLoc = new LatLng(destinationLat,destinationLon);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(desLoc,13f));
        Marker marker = mMap.addMarker(new MarkerOptions().position(desLoc).title(placeName));
        marker.showInfoWindow();
        getDirection();
    }

    private void getDirection() {
        String api = getString(R.string.direction_api);
        String endUrl = String.format("directions/json?origin=%f,%f&destination=%f,%f&key=%s"
                ,originLat,originLon,destinationLat,destinationLon,api);
        DirectionServiceApi serviceApi = RetrofitClient.getClient()
                .create(DirectionServiceApi.class);
        serviceApi.getDirectionResponse(endUrl)
                .enqueue(new Callback<DirectionResponse>() {
                    @Override
                    public void onResponse(Call<DirectionResponse> call, Response<DirectionResponse> response) {
                        if (response.isSuccessful()){
                            DirectionResponse directionResponse = response.body();
                            String distance = directionResponse.getRoutes().get(0).getLegs().get(0).getDistance().getText();
                            String time = directionResponse.getRoutes().get(0).getLegs().get(0).getDuration().getText();

                            //set distance and time
                            distanceTv.setText(distance);
                            timeTv.setText(time);
                            List<Step> steps = directionResponse
                                    .getRoutes()
                                    .get(0)
                                    .getLegs()
                                    .get(0)
                                    .getSteps();
                            for (Step s: steps){
                                drawPolyLines(s.getPolyline().getPoints());
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<DirectionResponse> call, Throwable t) {
                        Log.e("failed",t.getLocalizedMessage());
                    }
                });


    }

    private void drawPolyLines(String points) {
        List<LatLng> latLngList = decodePoly(points);
        PolylineOptions options = new PolylineOptions();
        options.color(Color.BLUE);
        options.width(14.0f);
        for (LatLng lt: latLngList){
            options.add(lt);
        }
        mMap.addPolyline(options);
    }
    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
