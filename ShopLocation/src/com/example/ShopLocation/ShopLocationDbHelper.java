package com.example.ShopLocation;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

public class ShopLocationDbHelper extends SQLiteOpenHelper {


    private static String DB_PATH = "/data/data/com.example.ShopLocation/databases/";
    private static String DB_NAME = "ShopDatabase.sqlite";
    public static int DB_VERSION = 1;

    private SQLiteDatabase myDatabase;
    private final Context myContext;


    public ShopLocationDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;
    }

    public void createDatabase() throws IOException {
        boolean dbExists = checkDatabase();
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

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    protected String[] query() {
        String myQuery = "SELECT * FROM shop";
        String[] result = {"no_result", "no_result", "no_result"};
        Cursor cursor;
        try {
            cursor = myDatabase.rawQuery(myQuery, null);
            if (cursor != null) {

                for (int i = 0; cursor.moveToNext(); i++) {
                    //while (cursor.moveToNext()) {
                    result[i] = cursor.getString(1);
                }
                cursor.deactivate();
            }
        } catch (Exception e) {
        }
        return result;
    }

}
