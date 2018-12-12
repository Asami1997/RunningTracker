package com.example.ahmadsami.runningtracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service implements LocationListener {
    int intial = 0;
    double previousLat ;
    double previousLong ;

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

            if(intial == 0){

              intial++;
              previousLat = location.getLatitude();
              previousLong = location.getLongitude();

            }else{

                // calculate difference between old location and new location
                calculateDistance(latitude,longitude);

                Log.i("trackerapp","speed" + location.getSpeed() * 0.514);
            }

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

        private void calculateDistance(double newLat,double newLong) {


           Location prevLocation = new Location("point A");
           Location newLocation  = new Location("point B");

           newLocation.setLatitude(newLat);
           newLocation.setLongitude(newLong);

           prevLocation.setLatitude(previousLat);
           prevLocation.setLongitude(previousLong);


           float[] results = new float[1];
            Location.distanceBetween(
                    previousLat,previousLong,
                    newLat ,newLong, results);


            Log.i("trackerapp","distance to : " + prevLocation.distanceTo(newLocation));
            Log.i("trackerapp","distance between : " + String.valueOf(results[0]));


            //if(newLocation.hasSpeed()){
               // Log.i("trackerapp","speed : " + String.valueOf(newLocation.getSpeed()));

            }

    // starts adding user
    public void addEntry () {


            DB_Handler dbHandler = new DB_Handler(this,"history", null,1);

            // creates a new recipe object
            //Entry_Sructure entry_sructure = new Recipe(recipeTitle,recipeContent);

            //dbHandler.addRecipe(recipe);

            // resets textviews
            //recipeTitleET.setText("");
            //recipeContentET.setText("");
            //recipeIDET.setText("");


    }


}




