package com.example.ahmadsami.runningtracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.ahmadsami.runningtracker.MainActivity.context;
import static com.example.ahmadsami.runningtracker.MainActivity.locationService;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

                                  // declaring variable

    static public GoogleMap mMap;
    static TextView time_TV;
    static TextView distance_TV;
    static boolean runningStatus;
    public String LOG_ID = "trackerapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        time_TV = findViewById(R.id.totalTime);

        distance_TV = findViewById(R.id.totalDistance);

        // false by default
        runningStatus = false;
    }

    // user started running
    public void startClicked(View view) {

        if(runningStatus){

            Toast.makeText(this, "You need to click stop before starting a new session", Toast.LENGTH_LONG).show();
        }else{

            // start update location
            runningStatus = true;
            locationService.runningStarted();
            Toast.makeText(locationService, "Running Started", Toast.LENGTH_SHORT).show();
        }
    }

    // user stop running
    public void stopClicked(View view) {

        if(!runningStatus){

            Toast.makeText(this, "You need to click start first", Toast.LENGTH_LONG).show();
        }else{

            runningStatus = false;
            Toast.makeText(locationService, "Running Stopped", Toast.LENGTH_SHORT).show();

            //reset time and distance text views
            time_TV.setText("Time");
            distance_TV.setText("Distance");

            // ask user if he wants to save the previous session
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Do you want to save the previous session information ?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            locationService.runningStoped();
                            Toast.makeText(MapsActivity.this, "Session information saved", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
       
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.i(LOG_ID,"in onMapReady function");

        // create a google map object
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(location).title("Your Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        // configure zoom level (close to street level view)
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
    }
}
