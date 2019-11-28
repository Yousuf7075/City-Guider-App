package com.example.cityguider.adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityguider.R;
import com.example.cityguider.api_response.Photo;
import com.example.cityguider.api_response.PlaceApiResponse;
import com.example.cityguider.api_response.Result;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>{
    Context context;
    private List<Result> placeList;
    private PlaceListToDirectionListener listener;
    private double lat, lon;

    public PlaceAdapter(Context context, List<Result> placeList) {
        this.context = context;
        this.placeList = placeList;
        this.listener = (PlaceListToDirectionListener) context;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.places_list_row,parent,false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        final Result result = placeList.get(position);
        lat = placeList.get(position).getGeometry().getLocation().getLat();
        lon = placeList.get(position).getGeometry().getLocation().getLng();
        String placeName1 = placeList.get(position).getName();
        final String placeName = result.getName();
        double placeRating = placeList.get(position).getRating();

        holder.ratingBar.setRating((float) placeRating);
        holder.placesListTV.setText(placeName);
        try {
            List<Address> addressList =  holder.geocoder.getFromLocation(lat,lon,1);
            Address address = addressList.get(0);
            String addressLine = address.getAddressLine(0);
            holder.streetAddress.setText(addressLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.recyclerRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.goToDirection(lat,lon,placeName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder{
        TextView placesListTV,streetAddress;
        RatingBar ratingBar;
        CardView recyclerRow;
        Geocoder geocoder;
        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            placesListTV = itemView.findViewById(R.id.placeNameTV);
            geocoder = new Geocoder(context, Locale.getDefault());
            streetAddress = itemView.findViewById(R.id.addressTV);
            recyclerRow = itemView.findViewById(R.id.placeRow);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
    public interface PlaceListToDirectionListener{
        void goToDirection(double lat, double lon, String name);
    }
}
