package com.example.ahmadsami.runningtracker;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

public class LocationService extends Service implements LocationListener {

    public class locationServiceBinder extends Binder {

        LocationService getService() {

            return LocationService.this;
        }
    }

    public void startUpdatingLocation() {

        Log.i("trackerapp","in start update location function");

        LocationManager locationManager =
                (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        getSystemService(LOCATION_SERVICE);

        LocationService locationListener = new LocationService();
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setBearingRequired(false);

        //API level 9 and up
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);

       // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
       // locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);
      // locationManager.requestLocationUpdates(1000,1,criteria,this,null);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    5, // minimum time interval between updates
                    5, // minimum distance between updates, in metres
                    locationListener);
        } catch (SecurityException e) {
            Log.d("trackerapp", e.toString());
        }

    }


   // location listener implementation
    @Override
    public IBinder onBind(Intent intent) {

        Log.i("trackerapp","onbind");

        startUpdatingLocation();
        return null;
    }


        @Override
        public void onLocationChanged(Location location) {

            Log.i("trackerapp","in onLocationChanged");
            double latitude=location.getLatitude();
            double longitude=location.getLongitude();
            String msg="New Latitude: "+latitude + "New Longitude: "+longitude;
            Log.i("trackerapp",msg);
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



