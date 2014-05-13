package com.example.ShopLocation;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class ShopLocationDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ShopDatabase.db";
    private static int DATABASE_VERSION = 1;

    public static final String SHOP_TABLE_NAME = "shop";

    private ShopLocationDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory){
        super(context, name, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        createTable(sqLiteDatabase);
    }

    private void createTable(SQLiteDatabase sqLiteDatabase){
        String qs = "CREATE TABLE " + SHOP_TABLE_NAME + " (" +
                ShopTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ShopTable.SHOP_NAME + " TEXT, " +
                ShopTable.LATITUDE + " REAL, " +
                ShopTable.LONGITUDE + " REAL);";
        sqLiteDatabase.execSQL(qs);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SHOP_TABLE_NAME + ";");
        createTable(sqLiteDatabase);
    }

}
