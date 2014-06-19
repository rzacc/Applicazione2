package com.example.ShopLocation;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.PointF;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

public class ShopLocationDbHelper extends SQLiteOpenHelper {


    private static String DB_PATH = "/data/data/com.example.ShopLocation/databases/";
    private static String DB_NAME = "ShopDatabase.sqlite";
    public static int DB_VERSION = 1;

    SQLiteDatabase myDatabase;
    private final Context myContext;

    public static final String TABLE_NAME = "shop";
    //TODO attributo non usato
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_LATITUDE = "latitude";
    public static final String COLUMN_NAME_LONGITUDE = "longitude";

    public ShopLocationDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;
    }

    public void createDatabase() throws IOException {
        boolean dbExists = checkDatabase();

        //TODO qua si può togliere if else e può rimanere un if con la condizione
        if (dbExists) {
            //do nothing, database already exists
        } else {
            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDatabase() {
        SQLiteDatabase checkDb = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            //database doesn't exist yet
        }
        if (checkDb != null) {
            checkDb.close();
        }
        return checkDb != null ? true : false;
    }

    private void copyDatabase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDatabase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if (myDatabase != null) {
            myDatabase.close();
            super.close();
        }
    }

    public ArrayList<Shop> filterShops(PointF center, double radius) {
        final double mult = 1.1;
        PointF p1 = ShopFinder.calculateDerivedPosition(center, mult * radius, 0);
        PointF p2 = ShopFinder.calculateDerivedPosition(center, mult * radius, 90);
        PointF p3 = ShopFinder.calculateDerivedPosition(center, mult * radius, 180);
        PointF p4 = ShopFinder.calculateDerivedPosition(center, mult * radius, 270);

        ArrayList<Shop> arrayList = new ArrayList<Shop>();
        String whereClause = " WHERE "
                + COLUMN_NAME_LATITUDE + " > " + String.valueOf(p3.x) + " AND "
                + COLUMN_NAME_LATITUDE + " < " + String.valueOf(p1.x) + " AND "
                + COLUMN_NAME_LONGITUDE + " < " + String.valueOf(p2.y) + " AND "
                + COLUMN_NAME_LONGITUDE + " > " + String.valueOf(p4.y);

        String query = "SELECT * FROM " + TABLE_NAME + whereClause;
        Cursor cursor;

        try {
            cursor = myDatabase.rawQuery(query, null);
            if (cursor != null) {

                for (int i = 0; cursor.moveToNext(); i++) {

                    String shopName = cursor.getString(1);    //shop name
                    double shopLatitude = cursor.getDouble(2);  //shop latitude
                    double shopLongitude = cursor.getDouble(3); //shop longitude
                    String shopAddress = cursor.getString(4); //shop address
                    Shop shop = new Shop(shopName, shopLatitude, shopLongitude, shopAddress);
                    arrayList.add(shop);   //add shop to ArrayList<Shop> result
                }
                cursor.close();
            }
        } catch (Exception e) {
        }
        return arrayList;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}