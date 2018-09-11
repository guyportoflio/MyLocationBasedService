 package com.example.presly.mylocationbasedservice;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


 public class MainActivity extends AppCompatActivity{


     Button btnShowLocation;
    private static final int REQUEST_CODE_PERMISSION =2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    //GPSTracker class
     GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            PackageManager packageManager = getPackageManager();

            if (ActivityCompat.checkSelfPermission(this, mPermission) != packageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{mPermission}, REQUEST_CODE_PERMISSION);

                //if any permission above is not allowed by the user then this will execute every time unlesss your "else" part will work

            }
        } catch (Exception e){
            e.printStackTrace();
            }

            btnShowLocation = (Button)findViewById(R.id.button);

        //show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create class object
                gps = new GPSTracker(MainActivity.this);

                //if the gps is enabled
                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    Toast.makeText(getApplicationContext(), "Your Location is - \n Lat: " +
                            latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    //cant get location + gps or network not enabled
                    gps.showSettingsAlert();
                }
            }
        });

    }

     @Override
     protected void onStop() {
         super.onStop();
         gps.stopUsingGPS();
     }
}
