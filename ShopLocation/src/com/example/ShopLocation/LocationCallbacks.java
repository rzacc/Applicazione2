package com.example.ShopLocation;

import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.common.GooglePlayServicesClient;

public class LocationCallbacks implements GooglePlayServicesClient.ConnectionCallbacks {

    public LocationCallbacks() {
    }

    //Called by Location Services when the request to connect the client finishes successfully.
    @Override
    public void onConnected(Bundle dataBundle) {
        //Display the connection status
        Toast.makeText(ShopLocationApp.getContext(), "Connected to Location Services", Toast.LENGTH_SHORT).show();
    }

    //Called by Location Services if the connection to the location client drops because of an error.
    @Override
    public void onDisconnected() {
        //Display the connection status
        Toast.makeText(ShopLocationApp.getContext(), "Disconnected from Location Services. Please re-connect.", Toast.LENGTH_SHORT).show();
    }
}
