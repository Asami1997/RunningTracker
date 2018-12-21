package com.example.ahmadsami.runningtracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LocationService extends Service implements LocationListener {

                        //initializing variables

    int intial = 0;
    double previousLat ;
    double previousLong ;
    static float totalDistance = 0;
    float totalTime;
    String date;
    Location startingLocation ;
    Date intitalTime;
    Date timeEnd;
    public String LOG_ID = "trackerapp";
    private final IBinder mBinder = new locationServiceBinder();
    // binder class
    public class locationServiceBinder extends Binder {

        LocationService getService() {

            return LocationService.this;
        }
    }

    // will start listening to user's location changes
    public void startUpdatingLocation() {

        Log.i(LOG_ID,"in start update location function");

        LocationManager locationManager =
                (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        getSystemService(LOCATION_SERVICE);

        // location listener
        LocationService locationListener = new LocationService();

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    5, // minimum time interval between updates
                    5, // minimum distance between updates, in metres
                    locationListener);
        } catch (SecurityException e) {
            Log.d(LOG_ID, e.toString());
        }

    }




   // location listener implementation
    @Override
    public IBinder onBind(Intent intent) {

        Log.i(LOG_ID,"in service onbind");

        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(LOG_ID,"in service onCreate");

    }

    @Override
        public void onLocationChanged(Location location) {

        // listen to changes only when the user is running
        if(MapsActivity.runningStatus){

            Log.i(LOG_ID,"in onLocationChanged");
            // get the user's location latitude
            double latitude=location.getLatitude();
            // get the user's location longitude
            double longitude=location.getLongitude();
            String msg="New Latitude: "+latitude + "New Longitude: "+longitude;
            Log.i(LOG_ID,msg);

            // when its the first time location listener is called
            if(intial == 0){

                intitalTime = Calendar.getInstance().getTime();
                Log.i(LOG_ID,"initial time : " + String.valueOf(intitalTime));
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

                Log.i(LOG_ID,"speed" + location.getSpeed() * 0.514);
            }
        }else{

            // reset
            totalDistance = 0;
            totalTime = 0;
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

           // create location objects for the new and previous locations
           Location prevLocation = new Location("point A");
           Location newLocation  = new Location("point B");

           newLocation.setLatitude(newLat);
           newLocation.setLongitude(newLong);

           //update google maps

            LatLng userLocation = new LatLng(newLat,newLong);
            MapsActivity.mMap.clear();
            MapsActivity.mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
            MapsActivity.mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
            MapsActivity.mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );

            prevLocation.setLatitude(previousLat);
            prevLocation.setLongitude(previousLong);

            // calculate distance difference
            totalDistance+=prevLocation.distanceTo(newLocation);
            Log.i(LOG_ID,"total distance: " + String.valueOf(totalDistance));

            // update the marker on google map
            MapsActivity.distance_TV.setText("Distance" + "\n" +  String.valueOf(totalDistance));

            //to update time text view
            getTotalTime();

            //update previous
            previousLat = newLat;
            previousLong = newLong;

            }

    // called when user stoped running , logging session details in the database starts

    public void runningStoped () {

            Log.i(LOG_ID,"user stopped running");

            DB_Handler dbHandler = new DB_Handler(this,null, null,1);

            //get time
            totalTime = getTotalTime();

            //get date
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c);

            // creates a new entry object
            Entry_Sructure entry_sructure = new Entry_Sructure();
            //prepare structure to be saved in DB
            entry_sructure.setTracker_date(formattedDate);
            entry_sructure.setTracker_distance(String.valueOf(totalDistance));
            entry_sructure.setTracker_time(String.valueOf(totalTime));

            // store in database
            dbHandler.addEntry(entry_sructure);
    }

    private float getTotalTime() {

        // get the time of stooping
        timeEnd = Calendar.getInstance().getTime();

        // calculate the time difference between the initial and the end running session time
        long diff =timeEnd.getTime() - intitalTime.getTime();

        // calculate minutes , hours and days using seconds
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        Log.i(LOG_ID,"total seconds" + String.valueOf(seconds));
        // update the time text view
        MapsActivity.time_TV.setText("Time" + "\n" + hours + ":" + minutes + ":" + seconds);
        return seconds;
    }


    // starts getting information about location as the user is running
    public void runningStarted(){

        // get initial time when the user starts running
        intitalTime = Calendar.getInstance().getTime();

        Log.i(LOG_ID,"user started running");

        // start update location when the user starts
        startUpdatingLocation();

    }

    @Override
    public void unbindService(ServiceConnection conn) {
        // unbind service
        super.unbindService(conn);
        Log.i(LOG_ID,"serviceunbinded");
    }
}




