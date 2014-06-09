package com.example.ShopLocation;

import android.app.Application;
import android.content.Context;


public class ShopLocationApp extends Application {
    private static Context myContext;

    public static void setContext(Context context) {
        myContext = context;
    }

    public static Context getContext() {
        return myContext;
    }
}
