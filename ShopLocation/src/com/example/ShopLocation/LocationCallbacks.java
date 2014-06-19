package com.example.ShopLocation;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;
import com.google.android.gms.common.GooglePlayServicesClient;

public class LocationCallbacks implements GooglePlayServicesClient.ConnectionCallbacks {

    public LocationCallbacks() {
    }

    //Called by Location Services when the request to connect the client finishes successfully.
    @Override
    public void onConnected(Bundle dataBundle) {
        //Display the connection status
        //TODO codice duplicato la stessa duplicazione c'è in piu classi quindi si può definire un oggetto che gestisce questo comportamento
        Toast toast = Toast.makeText(ShopLocationApp.getContext(), R.string.connected_to_location_services, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, -40);
        toast.show();
    }

    //Called by Location Services if the connection to the location client drops because of an error.
    @Override
    public void onDisconnected() {
        //Display the connection status
        Toast toast = Toast.makeText(ShopLocationApp.getContext(), R.string.disconnected_from_location_services, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, -40);
        toast.show();
    }
}
