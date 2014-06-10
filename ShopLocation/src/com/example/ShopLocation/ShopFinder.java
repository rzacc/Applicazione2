package com.example.ShopLocation;

import android.database.Cursor;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ShopFinder {

    public static final String TABLE_NAME = "shop";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_LATITUDE = "latitude";
    public static final String COLUMN_NAME_LONGITUDE = "longitude";

    private double radius = 10000;      //search radius = 10km

    public ShopFinder() {
    }

    public static PointF calculateDerivedPosition(PointF point, double range, double bearing) {
        double EarthRadius = 6371000;

        double latA = Math.toRadians(point.x);
        double lonA = Math.toRadians(point.y);
        double angularDistance = range / EarthRadius;
        double trueCourse = Math.toRadians(bearing);

        double lat = Math.asin(
                Math.sin(latA) * Math.cos(angularDistance) +
                        Math.cos(latA) * Math.sin(angularDistance)
                                * Math.cos(trueCourse)
        );

        double dlon = Math.atan2(
                Math.sin(trueCourse) * Math.sin(angularDistance)
                        * Math.cos(latA),
                Math.cos(angularDistance) - Math.sin(latA) * Math.sin(lat)
        );

        double lon = ((lonA + dlon + Math.PI) % (Math.PI * 2)) - Math.PI;

        lat = Math.toDegrees(lat);
        lon = Math.toDegrees(lon);

        PointF newPoint = new PointF((float) lat, (float) lon);
        return newPoint;
    }

    public static double getDistanceBetweenTwoPoints(PointF p1, PointF p2) {
        double R = 6371000;
        double dLat = Math.toRadians(p2.x - p1.x);
        double dLon = Math.toRadians(p2.y - p1.y);
        double lat1 = Math.toRadians(p1.x);
        double lat2 = Math.toRadians(p2.x);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2)
                * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;

        return d;
    }

    public static boolean shopIsInCircle(Shop shopToCheck, PointF center, double radius) {
        PointF pointToCheck = new PointF((float) shopToCheck.latitude, (float) shopToCheck.longitude);
        if (getDistanceBetweenTwoPoints(pointToCheck, center) <= radius)
            return true;
        else
            return false;
    }

    public ArrayList<Shop> getNearestShops(double latitude, double longitude) {
        PointF center = new PointF((float) latitude, (float) longitude);
        ArrayList<Shop> result = new ArrayList<Shop>();

        final double mult = 1.1;
        PointF p1 = calculateDerivedPosition(center, mult * radius, 0);
        PointF p2 = calculateDerivedPosition(center, mult * radius, 90);
        PointF p3 = calculateDerivedPosition(center, mult * radius, 180);
        PointF p4 = calculateDerivedPosition(center, mult * radius, 270);

        String whereClause = " WHERE "
                + COLUMN_NAME_LATITUDE + " > " + String.valueOf(p3.x) + " AND "
                + COLUMN_NAME_LATITUDE + " < " + String.valueOf(p1.x) + " AND "
                + COLUMN_NAME_LONGITUDE + " < " + String.valueOf(p2.y) + " AND "
                + COLUMN_NAME_LONGITUDE + " > " + String.valueOf(p4.y);

        String query = "SELECT * FROM " + TABLE_NAME + whereClause;
        Cursor cursor;

        try {
            cursor = ShopLocationDbHelper.myDatabase.rawQuery(query, null);
            if (cursor != null) {

                for (int i = 0; cursor.moveToNext(); i++) {

                    String shopName = cursor.getString(1);    //shop name
                    double shopLatitude = cursor.getDouble(2);  //shop latitude
                    double shopLongitude = cursor.getDouble(3); //shop longitude
                    Shop shop = new Shop(shopName, shopLatitude, shopLongitude);
                    result.add(shop);   //add shop to ArrayList<Shop> result
                }
                cursor.close();
            }
        } catch (Exception e) {
        }
        //loop on the filtered data to determine if they are near the user position (identified by center)
        for (int i = 0; i < result.size(); ) {
            if (shopIsInCircle(result.get(i), center, radius))
                i++;
            else
                result.remove(i);
        }

        //set the distance field for each Shop in result
        for (Shop shop : result) {
            PointF shopPoint = new PointF((float) shop.latitude, (float) shop.longitude);
            shop.distance = getDistanceBetweenTwoPoints(shopPoint, center);
        }

        //order Shop objects in result by distance from the user position
        Collections.sort(result, new Comparator<Shop>() {
            @Override
            public int compare(Shop shop1, Shop shop2) {
                return Double.compare(shop1.distance, shop2.distance);
            }
        });

        return result;

    }
}