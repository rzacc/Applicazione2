package com.example.ShopLocation;

import android.graphics.PointF;

import java.util.ArrayList;

public interface ShopRepository {
    void initializeRepository();

    public ArrayList<Shop> filterShops(PointF p1, PointF p2, PointF p3, PointF P4);

    public ArrayList<Shop> getNearestShops(PointF center, double radius, ArrayList<Shop> filteredShops);
}
