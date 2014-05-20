package com.example.ShopLocation;

import android.provider.BaseColumns;

public final class ShopLocationDbContract {
    //Empty constructor to prevent from accidentally instantiating the contract class
    public ShopLocationDbContract(){}

    //Inner class that defines the shop table contents
    public static abstract class ShopTable implements BaseColumns {
        public static final String TABLE_NAME = "shop";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
    }
}
