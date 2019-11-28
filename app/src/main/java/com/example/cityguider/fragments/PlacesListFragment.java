package com.example.cityguider.fragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.cityguider.R;
import com.example.cityguider.adapter.PlaceAdapter;
import com.example.cityguider.api_response.PlaceApiResponse;
import com.example.cityguider.api_response.PlaceServiceApi;
import com.example.cityguider.api_response.Result;
import com.example.cityguider.retrofit_client.RetrofitClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlacesListFragment extends Fragment {
    private  Context context;
    private String placeType;
    private double lat,lon;
    private PlaceAdapter placeAdapter;
    private RecyclerView placesRecycler;
    private ProgressBar progressBar;
    private RelativeLayout snackBarLayout;
    private TextView errorTv, toolbarTitle;
    int position;
    List <String> itemList,toolbarTitleList;

    public PlacesListFragment() {
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
        return inflater.inflate(R.layout.fragment_places_list, container, false);
    }
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        placesRecycler = view.findViewById(R.id.placeRecyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        snackBarLayout = view.findViewById(R.id.snackBarView);
        errorTv = view.findViewById(R.id.errorTV);
        toolbarTitle = view.findViewById(R.id.itemTV);
        //Toolbar myToolbar = view.findViewById(R.id.toolbar);

        toolbarTitleList = Arrays.asList(getResources().getStringArray(R.array.home_item_name));
        itemList = Arrays.asList(getResources().getStringArray(R.array.home_item));
        //get bundle data from home
        Bundle bundle = getArguments();
        if (bundle != null){
            position = bundle.getInt("pos");
            lat = bundle.getDouble("lat");
            lon = bundle.getDouble("lon");
        }

        //matching item name with item list
        for (int i = 0; i<= itemList.size(); i++){
            if (position == i){
                placeType = itemList.get(i);

                //set toolbar title
                toolbarTitle.setText(toolbarTitleList.get(i));
            }
        }

        final String END_URL = String.format("place/nearbysearch/json?location=%f,%f&radius=10000&type=%s&key=%s",lat,lon,placeType,getResources().getString(R.string.place_api));
        PlaceServiceApi serviceApi = RetrofitClient.getClient()
                .create(PlaceServiceApi.class);
        serviceApi.getNearByPlaces(END_URL).enqueue(new Callback<PlaceApiResponse>() {
            @Override
            public void onResponse(Call<PlaceApiResponse> call, Response<PlaceApiResponse> response) {
                if (response.isSuccessful()){
                    PlaceApiResponse apiResponse = response.body();
                    List<Result> placeList = apiResponse.getResults();
                    int resultSize = placeList.size();
                    if (resultSize > 0) {
                        placeAdapter = new PlaceAdapter(context,placeList);
                        LinearLayoutManager llm = new LinearLayoutManager(context);
                        placesRecycler.setLayoutManager(llm);
                        placesRecycler.setAdapter(placeAdapter);
                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                    }
                    else{
                        Snackbar snackbar = Snackbar.make(snackBarLayout,"Api is not respond!",Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.RED);
                        snackbar.show();
                        errorTv.setText("no result found !!");
                        errorTv.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                    }
                }
            }
            @Override
            public void onFailure(Call<PlaceApiResponse> call, Throwable t) {
                Log.e("failed",t.getLocalizedMessage());
            }
        });
    }

}
