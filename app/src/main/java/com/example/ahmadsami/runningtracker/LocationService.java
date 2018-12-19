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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LocationService extends Service implements LocationListener {
    int intial = 0;
    double previousLat ;
    double previousLong ;
    static float totalDistance = 0;
    float totalTime;
    int totalSteps;
    String date;
    Location startingLocation ;
    Location lastLocation ;
    Date intitalTime;
    Date timeEnd;
    private final IBinder mBinder = new locationServiceBinder();
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

        return mBinder;
    }


        @Override
        public void onLocationChanged(Location location) {

            Log.i("trackerapp","in onLocationChanged");
            double latitude=location.getLatitude();
            double longitude=location.getLongitude();
            String msg="New Latitude: "+latitude + "New Longitude: "+longitude;
            Log.i("trackerapp",msg);

            if(intial == 0){

                Log.i("trackerapp","firsttime");
                intitalTime = Calendar.getInstance().getTime();
                Log.i("trackerapp",String.valueOf(intitalTime));
                intial++;
              previousLat = location.getLatitude();
              previousLong = location.getLongitude();
              startingLocation = new Location("start");
              startingLocation.setLatitude(latitude);
              startingLocation.setLongitude(longitude);
            }else{

                intial++;
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


           /*
           float[] results = new float[1];
            Location.distanceBetween(
                    previousLat,previousLong,
                    newLat ,newLong, results);
*/

            totalDistance+=prevLocation.distanceTo(newLocation);
            Log.i("trackerapp","distance to : " + prevLocation.distanceTo(newLocation));
            Log.i("trackerapp","total distance: " + String.valueOf(totalDistance));


            //if(newLocation.hasSpeed()){
               // Log.i("trackerapp","speed : " + String.valueOf(newLocation.getSpeed()));

            //update previous
            previousLat = newLat;
            previousLong = newLong;

            }

    // called when user stoped running , logging session details in the database starts

    public void runningStoped () {

            timeEnd = Calendar.getInstance().getTime();

            Log.i("trackerappends","totaldistance" + String.valueOf(totalDistance));

            Log.i("trackerapp","user stopped running");

            DB_Handler dbHandler = new DB_Handler(this,"history", null,1);

            //get time
            totalTime = getTotalTime();

            //get date
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c);

            Log.i("trackerappendsd",formattedDate);

            // creates a new entry object
            Entry_Sructure entry_sructure = new Entry_Sructure();
            //prepare structure to be saved in DB
            entry_sructure.setTracker_date(formattedDate);
            entry_sructure.setTracker_distance(String.valueOf(totalDistance));
            entry_sructure.setTracker_time(String.valueOf(totalTime));

            dbHandler.addEntry(entry_sructure);
    }

    private float getTotalTime() {

        //Log.i("trackerappend",String.valueOf(timeEnd));
       // Log.i("trackerappends",String.valueOf(intitalTime));

        long diff =timeEnd.getTime() - intitalTime.getTime();

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        Log.i("trackerappendt",String.valueOf(seconds));
        return seconds;
    }


    // starts getting information about location as the user is running
    public void runningStarted(){

        intitalTime = Calendar.getInstance().getTime();
        Log.i("trackerapp",String.valueOf(intitalTime));

        Log.i("trackerapp","user started running");

        startUpdatingLocation();

    }
}




