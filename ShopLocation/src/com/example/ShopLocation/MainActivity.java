package com.example.ShopLocation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MainActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    /*
     * Define a request code to send to Google Play Services
     * This code is returned in Activity.onActivityResult
     */
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    LocationClient locationClient;
    Location currentLocation;

    ShopList shopList;
    Adapter listAdapter;
    List<Shop> list;

    ShopRepository shopRepository;

    //Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationClient = new LocationClient(this, this, this);
        setContentView(R.layout.main);
        ShopLocationApp.setContext(this);

        shopList = ShopListImplementation.factory.getShopList();
        listAdapter = shopList.createList();
        list = shopList.getList();
        final ListView listView = (ListView) findViewById(R.id.shop_list);
        listView.setAdapter((ListAdapter) listAdapter);

        shopRepository = ShopDbAdapter.factory.getShopRepository();
        shopRepository.initializeRepository();



    }


    @Override
    protected void onStart() {
        super.onStart();
        locationClient.connect();
    }

    @Override
    protected void onStop() {
        locationClient.disconnect();
        super.onStop();
    }

    private boolean servicesConnected() {
        //Check that Google Play Services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        //If Google Play Services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            //In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            //Continue
            return true;
            //Google Play Services was not available for some reason
        } else {
            //The error code is in resultCode
            //Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

            //If Google Play services can provide an error dialog
            if (errorDialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                //Show the error dialog in the DialogFragment
                errorFragment.show(this.getFragmentManager(), "Location Updates");
            }
            return false;
        }
    }

    /*
     * Handle results returned to the Activity by Google Play services
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Decide what to do based on the original request code
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
                //If the result code is Activity.RESULT_OK, try to connect again
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        servicesConnected();
                }
        }
    }

    //Called by Location Services when the request to connect the client finishes successfully.
    @Override
    public void onConnected(Bundle dataBundle) {
        //Display the connection status
        Toast.makeText(this, "Connected to Location Services", Toast.LENGTH_SHORT).show();
    }

    //Called by Location Services if the connection to the location client drops because of an error.
    @Override
    public void onDisconnected() {
        //Display the connection status
        Toast.makeText(this, "Disconnected from Location Services. Please re-connect.", Toast.LENGTH_SHORT).show();
    }

    //Called by Location Services if the attempt to Location Services fails.
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects. If the error has a resolution, try sending an
         * Intent to start a Google Play services activity that can resolve the error.
         */
        if (connectionResult.hasResolution()) {
            try {
                //Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            //If no resolution is available, display a dialog to the user with the error
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            //If Google Play services can provide an error dialog
            if (errorDialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                //Show the error dialog in the DialogFragment
                errorFragment.show(this.getFragmentManager(), "Connection Failed");
            }
        }
    }


    //Called when "Cerca negozi" is clicked
    public void searchForShops(View v) {
        if (servicesConnected()) {
            currentLocation = locationClient.getLastLocation();

            //TextView to check current location (to be removed)
            TextView tv = (TextView) findViewById(R.id.currentLocation);
            tv.setText(currentLocation.toString());

            String[] s = shopRepository.query();

            Shop shop1 = new Shop(s[0]);
            Shop shop2 = new Shop(s[1]);
            Shop shop3 = new Shop(s[2]);
            list.add(shop1);
            list.add(shop2);
            list.add(shop3);
            shopList.notifyDataSetChanged();
        }
    }

}
