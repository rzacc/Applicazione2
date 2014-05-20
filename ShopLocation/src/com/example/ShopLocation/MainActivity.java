package com.example.ShopLocation;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.*;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

public class MainActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    /*
     * Define a request code to send to Google Play Services
     * This code is returned in Activity.onActivityResult
     */
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    LocationClient locationClient;
    Location currentLocation;
    SQLiteDatabase db;
    ShopLocationDbHelper dbHelper;

    //Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationClient = new LocationClient(this, this, this);
        setContentView(R.layout.main);

        //Insert some test values into the database
        dbHelper = new ShopLocationDbHelper(this);
        db = dbHelper.getWritableDatabase();

        ContentValues shop1 = new ContentValues();
        shop1.put(ShopLocationDbContract.ShopTable.COLUMN_NAME_NAME, "SHOP1-VA");
        shop1.put(ShopLocationDbContract.ShopTable.COLUMN_NAME_LATITUDE, 45.807594);
        shop1.put(ShopLocationDbContract.ShopTable.COLUMN_NAME_LONGITUDE, 8.86266);
        db.insert(ShopLocationDbContract.ShopTable.TABLE_NAME, null, shop1);

        ContentValues shop2 = new ContentValues();
        shop2.put(ShopLocationDbContract.ShopTable.COLUMN_NAME_NAME, "SHOP2-VA");
        shop2.put(ShopLocationDbContract.ShopTable.COLUMN_NAME_LATITUDE, 45.815755);
        shop2.put(ShopLocationDbContract.ShopTable.COLUMN_NAME_LONGITUDE, 8.82805);
        db.insert(ShopLocationDbContract.ShopTable.TABLE_NAME, null, shop2);

        ContentValues shop3 = new ContentValues();
        shop2.put(ShopLocationDbContract.ShopTable.COLUMN_NAME_NAME, "SHOP3-MI");
        shop2.put(ShopLocationDbContract.ShopTable.COLUMN_NAME_LATITUDE, 45.610042);
        shop2.put(ShopLocationDbContract.ShopTable.COLUMN_NAME_LONGITUDE, 8.962486);
        db.insert(ShopLocationDbContract.ShopTable.TABLE_NAME, null, shop3);

    }

    @Override
    protected void onStart(){
        super.onStart();
        locationClient.connect();
    }

    @Override
    protected void onStop(){
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //Decide what to do based on the original request code
        switch(requestCode){
            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
                //If the result code is Activity.RESULT_OK, try to connect again
                switch(resultCode){
                    case Activity.RESULT_OK:
                        servicesConnected();
                }
        }
    }

    //Called by Location Services when the request to connect the client finishes successfully.
    @Override
    public void onConnected(Bundle dataBundle){
        //Display the connection status
        Toast.makeText(this, "Connected to Location Services", Toast.LENGTH_SHORT).show();
    }

    //Called by Location Services if the connection to the location client drops because of an error.
    @Override
    public void onDisconnected(){
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
    public void searchForShops(View v){
        if(servicesConnected()){
            currentLocation = locationClient.getLastLocation();
            //TextView to check current location (to be removed)
            TextView tv = (TextView)findViewById(R.id.currentLocation);
            tv.setText(currentLocation.toString());
        }
    }

}
