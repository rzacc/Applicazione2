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

    public ArrayList<Shop> filterShops(PointF p1, PointF p2, PointF p3, PointF p4) {
        return dbHelper.filterShops(p1, p2, p3, p4);
    }

    public ArrayList<Shop> getNearestShops(PointF center, double radius, ArrayList<Shop> filteredShops) {
        return shopFinder.getNearestShops(center, radius, filteredShops);
    }

}
