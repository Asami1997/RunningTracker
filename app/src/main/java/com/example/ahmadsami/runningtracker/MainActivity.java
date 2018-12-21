package com.example.ahmadsami.runningtracker;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //initializing variables
    static public LocationService locationService;
    static public Context context;
    public final static int  MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    public String LOG_ID = "trackerapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(LOG_ID,"in onCreate method");
        context = this;
        // starting the service and binding it to main activity
        final Intent serviceStart = new Intent(this.getApplication(), LocationService.class);
        this.getApplication().startService(serviceStart);
        this.getApplication().bindService(serviceStart, serviceConnection, Context.BIND_AUTO_CREATE);

        // get user location permission
        checkUserPermission();
    }

    // getting the user permission to access fine location
    private void checkUserPermission() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                //request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                //  MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }


    // creating a service connection to be used when starting a service
    public static ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {

                locationService = ((LocationService.locationServiceBinder) service).getService();
                Log.i("trackerapp","in service connection");
        }

        public void onServiceDisconnected(ComponentName className) {
            if (className.getClassName().equals("LocationService")) {
                locationService = null;
            }
        }
    };


    // result of user's permission request
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "GPS permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    // permission denied
                }
                return;
            }

            // permissions this app might request.
        }
    }


    // called when the user clicks the statistics button
    public void showStatistics(View view){

        Log.i(LOG_ID,"statistics button clicked");
        // start the statistics activity
        Intent statsIntent = new Intent(getApplicationContext(),Statistics.class);
        startActivity(statsIntent);
    }

    // called when the user clicks on the tracker button
    public void showTracker(View view){

        Log.i(LOG_ID,"tracker button clicked");
        // starts the tracker activity
        Intent trackerIntent = new Intent(getApplicationContext(),MapsActivity.class);
        startActivity(trackerIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_ID,"in onDestroy method");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_ID,"in onStop method");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_ID,"in onStart method");
    }
}

