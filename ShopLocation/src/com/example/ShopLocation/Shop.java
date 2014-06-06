package com.example.ShopLocation;

public class Shop {
    String name;
    double latitude;
    double longitude;
    double distance;

    public Shop(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String toString() {
        return name + " " + latitude + " " + longitude;
    }

}
