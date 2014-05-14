package com.example.ShopLocation;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends Activity {
    UserLocationService locationService;
    boolean boundToService = false;

    //Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    protected void onStart(){
        super.onStart();
        //Bind to UserLocationService
        Intent intent = new Intent(this, UserLocationService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop(){
        super.onStop();
        //Unbind from the location service
        if(boundToService){
            unbindService(serviceConnection);
            boundToService = false;
        }
    }

    //Called when "Cerca negozi" is clicked
    public void searchForShops(View v){}


    //Defines callbacks for service binding, passed to bindService()
    private ServiceConnection serviceConnection = new ServiceConnection(){

        @Override
        //Called when the connection with the service is established
        public void onServiceConnected(ComponentName className, IBinder service){
            UserLocationService.UserLocationBinder binder = (UserLocationService.UserLocationBinder) service;
            locationService = binder.getService();
            boundToService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0){
            boundToService = false;
        }
    };
}
