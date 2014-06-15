package com.example.ShopLocation;

import android.app.Activity;
import android.app.Dialog;
import android.content.IntentSender;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class LocationConnectionFailedListener implements GooglePlayServicesClient.OnConnectionFailedListener {

    static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    Activity activity;

    LocationConnectionFailedListener(Activity activity) {
        this.activity = activity;
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
                connectionResult.startResolutionForResult(activity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            //If no resolution is available, display a dialog to the user with the error
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), activity,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            //If Google Play services can provide an error dialog
            if (errorDialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                //Show the error dialog in the DialogFragment
                errorFragment.show(activity.getFragmentManager(), "Connection Failed");
            }
        }
    }
}
