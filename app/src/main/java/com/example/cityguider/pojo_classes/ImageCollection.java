package com.example.cityguider.pojo_classes;

import com.example.cityguider.R;

import java.util.ArrayList;
import java.util.List;

public class ImageCollection {
    private int icon;

    public ImageCollection(int icon) {
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
    public static List<ImageCollection> getIconList(){
        List<ImageCollection> iconList = new ArrayList<>();
        iconList.add(new ImageCollection(R.drawable.restaurant));
        iconList.add(new ImageCollection(R.drawable.cafe));
        iconList.add(new ImageCollection(R.drawable.atm));
        iconList.add(new ImageCollection(R.drawable.bank));
        iconList.add(new ImageCollection(R.drawable.hospital));
        iconList.add(new ImageCollection(R.drawable.library));
        iconList.add(new ImageCollection(R.drawable.police));
        iconList.add(new ImageCollection(R.drawable.bus_station));
        iconList.add(new ImageCollection(R.drawable.train_station));
        iconList.add(new ImageCollection(R.drawable.fire_station));
        iconList.add(new ImageCollection(R.drawable.parking));
        iconList.add(new ImageCollection(R.drawable.doctor));
        iconList.add(new ImageCollection(R.drawable.dentist));
        iconList.add(new ImageCollection(R.drawable.drugtore));
        iconList.add(new ImageCollection(R.drawable.attraction_place));
        iconList.add(new ImageCollection(R.drawable.school));
        iconList.add(new ImageCollection(R.drawable.university));
        iconList.add(new ImageCollection(R.drawable.car_repairing));
        iconList.add(new ImageCollection(R.drawable.electrocian));
        iconList.add(new ImageCollection(R.drawable.shopping_mall));
        iconList.add(new ImageCollection(R.drawable.super_shop));
        iconList.add(new ImageCollection(R.drawable.gym));
        iconList.add(new ImageCollection(R.drawable.church));
        iconList.add(new ImageCollection(R.drawable.mosque));
        iconList.add(new ImageCollection(R.drawable.museum));
        iconList.add(new ImageCollection(R.drawable.theater));
        iconList.add(new ImageCollection(R.drawable.nightlife));
        return iconList;
    }
}
