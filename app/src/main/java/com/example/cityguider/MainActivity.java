package com.example.cityguider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.cityguider.adapter.PlaceAdapter;
import com.example.cityguider.fragments.EventsFragment;
import com.example.cityguider.fragments.HomeFragment;
import com.example.cityguider.fragments.PlacesListFragment;
import com.example.cityguider.fragments.UserProfileFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements
        HomeAdapter.GoToPlacesDetailsListener,
        PlaceAdapter.PlaceListToDirectionListener {
    FragmentManager fragmentManager;
    private FusedLocationProviderClient providerClient;
    public double latitude, longitude;
    private HomeFragment hm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        providerClient = LocationServices.getFusedLocationProviderClient(this);

        // load the store fragment by default
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        hm = new HomeFragment();
        ft.add(R.id.fragmentContainer, hm);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void itemToPlaces(int position) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        PlacesListFragment placesListFragment = new PlacesListFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble("lat",latitude);
        bundle.putDouble("lon",longitude);
        bundle.putInt("pos",position);
        placesListFragment.setArguments(bundle);
        ft.replace(R.id.fragmentContainer,placesListFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    //check location permission method
    private boolean checkLocationPermission(){
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String [] {Manifest.permission.ACCESS_FINE_LOCATION},123);
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkLocationPermission()){
            getDeviceLastLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 123 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
           getDeviceLastLocation();
        }
    }
    //get device location method
    public void getDeviceLastLocation(){
        if (checkLocationPermission()){
            providerClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        hm.onReceivedData(latitude,longitude);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("onFailed",e.getLocalizedMessage());
                }
            });
        }
    }

    @Override
    public void goToDirection(double lat, double lon, String placeName) {
        double dLat = lat;
        double dLon = lon;
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        intent.putExtra("olat",latitude);
        intent.putExtra("olon",longitude);
        intent.putExtra("dlat",lat);
        intent.putExtra("dlon",lon);
        intent.putExtra("name",placeName);
        startActivity(intent);
    }
}
