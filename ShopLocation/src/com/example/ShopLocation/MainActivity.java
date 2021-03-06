package com.example.ShopLocation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

import java.util.ArrayList;

public class MainActivity extends Activity {

    /*
     * Define a request code to send to Google Play Services
     * This code is returned in Activity.onActivityResult
     */
    LocationClient locationClient;
    Location currentLocation;

    ShopList shopList;
    Adapter listAdapter;
    ArrayList<Shop> list;

    ShopRepository shopRepository;
    ShopFinder shopFinder;

    double radius;  //search radius
    EditText radiusEditText;
    boolean notSpecifiedRadius;
    boolean tooLargeRadius;

    //Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        locationClient = new LocationClient(this, new LocationCallbacks(), new LocationConnectionFailedListener(this));
        setContentView(R.layout.main);
        ShopLocationApp.setContext(this);

        radiusEditText = (EditText) findViewById(R.id.radius_editText);
        shopList = new ShopList();
        listAdapter = shopList.createList();
        list = shopList.getList();

        final ListView listView = (ListView) findViewById(R.id.shop_list);
        listView.setAdapter((ListAdapter) listAdapter);

        shopRepository = new ShopDbAdapter();
        shopRepository.initializeRepository();
        shopFinder = new ShopFinder();

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
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, LocationConnectionFailedListener.CONNECTION_FAILURE_RESOLUTION_REQUEST);

            //If Google Play services can provide an error dialog
            if (errorDialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                //Show the error dialog in the DialogFragment
                errorFragment.show(this.getFragmentManager(), "Location updates");
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
        //TODO questo potrebbe essere un unico if con le due condizioni in AND
        switch (requestCode) {
            case LocationConnectionFailedListener.CONNECTION_FAILURE_RESOLUTION_REQUEST:
                //If the result code is Activity.RESULT_OK, try to connect again
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        servicesConnected();
                }
        }
    }

    double getRadiusFromUserInput() {
        double radius = 0;
        try {
            radius = Double.parseDouble(radiusEditText.getText().toString());
        } catch (Exception e) {
            if (radiusEditText.getText().length() == 0) {
                notSpecifiedRadius = true;
            }
        }

        if (radius > 8000) {
            tooLargeRadius = true;
            radius = 0;
        }

        radius = radius * 1000; //convert kilometers in meters
        return radius;
    }

    //Called when "Find shops" is clicked
    //TODO il parametro se non serve toglilo
    public void searchForShops(View v) {
        if (servicesConnected()) {

            if (!list.isEmpty()) {
                list.clear();
            }

            currentLocation = locationClient.getLastLocation();

            notSpecifiedRadius = false;
            tooLargeRadius = false;
            radius = getRadiusFromUserInput();

            PointF center = new PointF((float) currentLocation.getLatitude(), (float) currentLocation.getLongitude());
            ArrayList<Shop> filteredShops = shopRepository.filterShops(center, radius);
            list.addAll(shopFinder.getShops(center, radius, filteredShops));
            shopList.notifyDataSetChanged();

            if (list.isEmpty()) {
                if (notSpecifiedRadius) {
                    //TODO codice duplicato
                    Toast toast = Toast.makeText(this, R.string.specify_search_radius, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, -40);
                    toast.show();
                } else if (tooLargeRadius) {
                    Toast toast = Toast.makeText(this, R.string.too_large_radius, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, -71);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(this, R.string.no_shops_found, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, -40);
                    toast.show();
                }
            }
        }
    }

    //Called when "Clear" is clicked
    //TODO il parametro se non serve toglilo
    public void clearList(View v) {

        if (!list.isEmpty()) {
            list.clear();
            shopList.notifyDataSetChanged();

        } else {
            //TODO codice duplicato
            Toast toast = Toast.makeText(this, R.string.empty_list, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, -40);
            toast.show();
        }
    }

}
