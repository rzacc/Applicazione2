package com.example.ShopLocation;

import java.util.ArrayList;

public interface ShopRepository {
    void initializeRepository();

    public ArrayList<Shop> getNearestShops(double latitude, double longitude);
}
