package com.example.ShopLocation;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class ShopLocationDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ShopDatabase.db";
    public static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ", ";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ShopLocationDbContract.ShopTable.TABLE_NAME + " (" +
            ShopLocationDbContract.ShopTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
            ShopLocationDbContract.ShopTable.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
            ShopLocationDbContract.ShopTable.COLUMN_NAME_LATITUDE + REAL_TYPE + COMMA_SEP +
            ShopLocationDbContract.ShopTable.COLUMN_NAME_LONGITUDE + REAL_TYPE + " );";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ShopLocationDbContract.ShopTable.TABLE_NAME;

    public ShopLocationDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

}
