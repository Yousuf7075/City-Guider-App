package com.example.cityguider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityguider.pojo_classes.ImageCollection;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ItemViewHolder>{
    List <String> itemList;
    List<ImageCollection> iconList;
    Context context;
    private GoToPlacesDetailsListener listener;

    public HomeAdapter(List<String> itemList, List<ImageCollection> iconList, Context context) {
        this.itemList = itemList;
        this.iconList = iconList;
        this.context = context;
        listener = (GoToPlacesDetailsListener) context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_item_row,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        holder.itemName.setText(itemList.get(position));
        holder.itemIcon.setImageResource(iconList.get(position).getIcon());
        holder.eachLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemToPlaces(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        CardView eachLayout;
        LinearLayout linearLayout;
        TextView itemName;
        ImageView itemIcon;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemNameTV);
            itemIcon = itemView.findViewById(R.id.itemIcon);
            eachLayout =itemView.findViewById(R.id.eachItem);
            linearLayout = itemView.findViewById(R.id.innerLayout);
        }
    }
    //fragment transaction item to places
    public interface GoToPlacesDetailsListener {
         void itemToPlaces(int position);
    }
}
