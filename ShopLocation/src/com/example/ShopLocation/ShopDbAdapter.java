package com.example.ShopLocation;

import java.io.IOException;
import java.sql.SQLException;

public class ShopDbAdapter implements ShopRepository {
    ShopLocationDbHelper dbHelper;

    private ShopDbAdapter() {
    }

    public static ShopRepositoryFactory factory = new ShopRepositoryFactory(){
        public ShopRepository getShopRepository(){
            return new ShopDbAdapter();
        }
    };

    public void initializeRepository() {
        dbHelper = new ShopLocationDbHelper(ShopLocationApp.getContext());
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

    public String[] query(){
       return dbHelper.query();
    }

}
