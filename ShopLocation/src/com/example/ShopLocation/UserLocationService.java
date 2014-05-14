package com.example.ShopLocation;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class UserLocationService extends Service {
    //Binder given to clients
    private final IBinder binder = new UserLocationBinder();

    public class UserLocationBinder extends Binder{
        UserLocationService getService() {
            //Return this instance of UserLocationService so clients can call public methods
            return UserLocationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent){
        return binder;
    }

    //Method for clients
}
