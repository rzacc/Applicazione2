package com.example.ShopLocation;

public class Shop {
    String name;
    String address;
    double latitude;
    double longitude;
    double distance;


    public Shop(String name, double latitude, double longitude, String address) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public String toString() {
        double distanceKm = distance / 1000;
        return name + "\n" + address + "\nDistance: " + distanceKm + " km";
    }

}
