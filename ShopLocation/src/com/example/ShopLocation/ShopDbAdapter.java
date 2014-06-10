package com.example.ShopLocation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ShopDbAdapter implements ShopRepository {
    ShopLocationDbHelper dbHelper;
    ShopFinder shopFinder;

    ShopDbAdapter() {
    }

    public void initializeRepository() {
        dbHelper = new ShopLocationDbHelper(ShopLocationApp.getContext());
        shopFinder = new ShopFinder();
        try {
            dbHelper.createDatabase();
        } catch (IOException e) {
            throw new Error("Unable to create database");
        }
        try {
            dbHelper.openDatabase();
        } catch (SQLException e) {
        }
    }

    public ArrayList<Shop> getNearestShops(double latitude, double longitude) {
        return shopFinder.getNearestShops(latitude, longitude);
    }

}
