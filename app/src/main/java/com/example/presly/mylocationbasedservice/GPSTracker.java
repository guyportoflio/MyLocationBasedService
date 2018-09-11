package com.example.presly.mylocationbasedservice;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

public class GPSTracker extends Service implements LocationListener{
    private final Context mContext;


    //flag gps status
    boolean isGPSEnabled = false;

    //flag network status
    boolean isNetworkEnabled = false;

    //flag Location
    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;

    //minimum distance to change updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 15;// 15 meters

    //minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES =  60000; // 1 minute

    //declaring location manager
    protected LocationManager locationManager;

    public GPSTracker(Context context){
        this.mContext = context;
        getLocation();
    }

    @SuppressLint("MissingPermission")
    public Location getLocation() {
        try {
            locationManager = (LocationManager)mContext.getSystemService(LOCATION_SERVICE);

            //getting gps status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isGPSEnabled && !isNetworkEnabled) {
                //no network or gps available
            }
            else {
                this.canGetLocation = true;
                //now lets get location from network provider
                if(isNetworkEnabled){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, +
                    MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES,this);

                    Log.d("Network", "Network");
                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if(location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                //if gps enabled then lets get lat/lng using gps service
                if(isGPSEnabled){
                    if(location == null){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, +
                        MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES,this);

                        Log.d("GPS enabled", " GPS enabled");
                        if(locationManager != null){
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if(location != null){
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return location;
    }
/** stop using GPS listener **/
public void stopUsingGPS(){
    if(locationManager != null){
        locationManager.removeUpdates(GPSTracker.this);
    }


    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
   //return latitude..duah
        return latitude;

}

public boolean canGetLocation(){
    return this.canGetLocation;
}

/** setting the alert dialog **/
public void showSettingsAlert(){
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);


    //setting dialog title
    alertDialog.setTitle("GPS settings");

    //setting dialog message
    alertDialog.setMessage("GPS is not enabled, do you want to go to the settings menu?");

    //on pressing the setting button
    alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog,int which){
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(intent);
        }
    });

    //on pressing cancel button
alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
});

//showing alert messages
    alertDialog.show();
}

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
