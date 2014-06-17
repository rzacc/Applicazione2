package com.example.ShopLocation;

import android.graphics.PointF;

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

    public ArrayList<Shop> filterShops(PointF center, double radius) {
        return dbHelper.filterShops(center, radius);
    }

}
