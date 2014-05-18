package com.example.ShopLocation;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class ErrorDialogFragment extends DialogFragment {
    //Global field to contain the error dialog
    private Dialog errorDialog;

    //Default constructor. Sets the dialog field to null
    public ErrorDialogFragment(){
        super();
        errorDialog = null;
    }

    //Set the dialog to display
    public void setDialog(Dialog dialog){
        errorDialog = dialog;
    }

    //Return a Dialog to the DialogFragment
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        return errorDialog;
    }
}
