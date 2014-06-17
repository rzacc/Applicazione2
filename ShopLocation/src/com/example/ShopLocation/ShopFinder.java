package com.example.ShopLocation;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ShopFinder {

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

    public ArrayList<Shop> getShops(PointF center, double radius, ArrayList<Shop> filteredShops) {

        //loop on the filtered data to determine if they are near the user position (identified by center)
        for (int i = 0; i < filteredShops.size(); ) {
            if (shopIsInCircle(filteredShops.get(i), center, radius))
                i++;
            else
                filteredShops.remove(i);
        }

        //set the distance field for each Shop in result
        for (Shop shop : filteredShops) {
            PointF shopPoint = new PointF((float) shop.latitude, (float) shop.longitude);
            shop.distance = getDistanceBetweenTwoPoints(shopPoint, center);
        }

        //order Shop objects in result by distance from the user position
        Collections.sort(filteredShops, new Comparator<Shop>() {
            @Override
            public int compare(Shop shop1, Shop shop2) {
                return Double.compare(shop1.distance, shop2.distance);
            }
        });

        return filteredShops;

    }
}
