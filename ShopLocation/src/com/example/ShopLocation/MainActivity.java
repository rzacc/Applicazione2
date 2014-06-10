package com.example.ShopLocation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

import java.util.ArrayList;

public class MainActivity extends Activity implements GooglePlayServicesClient.OnConnectionFailedListener {

    /*
     * Define a request code to send to Google Play Services
     * This code is returned in Activity.onActivityResult
     */
    static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    LocationClient locationClient;
    Location currentLocation;

    ShopList shopList;
    Adapter listAdapter;
    ArrayList<Shop> list;

    ShopRepository shopRepository;

    //Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationClient = new LocationClient(this, new LocationCallbacks(), this);
        setContentView(R.layout.main);
        ShopLocationApp.setContext(this);

        shopList = new ShopList();
        listAdapter = shopList.createList();
        list = shopList.getList();
        final ListView listView = (ListView) findViewById(R.id.shop_list);
        listView.setAdapter((ListAdapter) listAdapter);

        shopRepository = new ShopDbAdapter();
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

    //Called when "Find shops" is clicked
    public void searchForShops(View v) {
        if (servicesConnected()) {
            if (!list.isEmpty()) {
                list.clear();
            }
            currentLocation = locationClient.getLastLocation();
            list.addAll(shopRepository.getNearestShops(currentLocation.getLatitude(), currentLocation.getLongitude()));
            shopList.notifyDataSetChanged();
        }
    }

    //Called when "Clear" is clicked
    public void clearList(View v) {
        if (!list.isEmpty()) {
            list.clear();
            shopList.notifyDataSetChanged();
        } else
            Toast.makeText(this, "List empty", Toast.LENGTH_SHORT).show();
    }

}
