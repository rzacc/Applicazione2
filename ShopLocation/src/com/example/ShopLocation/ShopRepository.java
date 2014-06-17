package com.example.ShopLocation;

import android.graphics.PointF;

import java.util.ArrayList;

public interface ShopRepository {
    void initializeRepository();

    public ArrayList<Shop> filterShops(PointF center, double radius);

}
