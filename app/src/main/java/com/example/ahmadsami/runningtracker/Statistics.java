package com.example.ahmadsami.runningtracker;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Statistics extends AppCompatActivity {

    // text_views in this day linear layout
    TextView dayDuration;
    TextView dayDistance;
    TextView dayDate;

    // text_views in this month linear layout
    TextView monthDuration;
    TextView monthDistance;
    TextView monthDate;

    // text_views in all time linear layout
    TextView allTimeDuration;
    TextView allTimeDistance;
    TextView allTimeDate;

    //arraylists
    ArrayList<Entry_Sructure> allLogs;
    ArrayList<Entry_Sructure> dayLogs;
    ArrayList<Entry_Sructure> monthLogs;
    ArrayList<Entry_Sructure> allTimeLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        dayDuration = findViewById(R.id.dayTime);
        dayDistance = findViewById(R.id.dayDistance);
        dayDate = findViewById(R.id.dayDate);

        monthDuration = findViewById(R.id.monthTime);
        monthDistance = findViewById(R.id.monthDistance);
        monthDate = findViewById(R.id.monthDate);

        allTimeDuration = findViewById(R.id.allTime);
        allTimeDistance = findViewById(R.id.allDistance);
        allTimeDate = findViewById(R.id.allDate);

        allLogs = new ArrayList<Entry_Sructure>();
        dayLogs = new ArrayList<Entry_Sructure>();
        monthLogs = new ArrayList<Entry_Sructure>();
        allLogs = new ArrayList<Entry_Sructure>();

        getLogs();

        sortLogs();
    }


    // get all logs in database
    private void getLogs() {

        // specify which columns to get from the history database
        String[] mProjection = new String[] {
                Contract_Tracker.COLUMN_TRACKER_ID,
                Contract_Tracker.COLUMN_TRACKER_DATE,
                Contract_Tracker.COLUMN_TRACKER_DISTANCE,
                Contract_Tracker.COLUMN_TRACKER_TIME
        };


        DB_Handler db_handler = new DB_Handler(this,null,null,1);
        ContentResolver contentResolver = db_handler.getContentResolver();

        // get the whole database
        Cursor cursor = contentResolver.query(Contract_Tracker.CONTENT_URI, mProjection,
                null, null, null);


            // loop through the cursor contents
           Log.i("trackerappdis","here");
           while (cursor!=null &&cursor.moveToNext()) {
            Entry_Sructure entry = new Entry_Sructure(cursor.getString(cursor.getColumnIndex(Contract_Tracker.COLUMN_TRACKER_TIME)),
                    cursor.getString(cursor.getColumnIndex(Contract_Tracker.COLUMN_TRACKER_DISTANCE)),
                    cursor.getString(cursor.getColumnIndex(Contract_Tracker.COLUMN_TRACKER_DATE)),
                   cursor.getInt(cursor.getColumnIndex(Contract_Tracker.COLUMN_TRACKER_ID)));

                    Log.i("trackerappdis", entry.getTracker_distance());
                    Log.i("trackerappdis",entry.getTracker_date());
                    Log.i("trackerappdis",entry.getTracker_time());

            allLogs.add(entry);
        }
    }

    private void sortLogs() {

        //get date
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        for(Entry_Sructure entry : allLogs){

            checkToday(formattedDate,entry);

            checkMonth(formattedDate,entry);
        }

        addToDayView();

        addtoMonthView();

        addtoAllTimeViews();

    }

    private void addToDayView() {

        double totalDayDuration = 0;
        long totalDaySeconds = 0;
        String date = "";
        for(Entry_Sructure entry : dayLogs){
           totalDayDuration += Double.parseDouble(entry.getTracker_distance());
           totalDaySeconds += Double.parseDouble(entry.getTracker_time());
           date = entry.getTracker_date();
        }
        int hours = (int) (totalDaySeconds / 3600);
        int minutes = (int) ((totalDaySeconds % 3600) / 60);
        int seconds = (int) (totalDaySeconds % 60);

        //set texts
        dayDistance.setText("Distance : " + String.valueOf(totalDayDuration));
        dayDuration.setText("Time : " + String.valueOf(hours) +":" + String.valueOf(minutes) + ":" +  String.valueOf(seconds));
        dayDate.setText("Date : " + String.valueOf(date));

    }

    private void addtoMonthView(){

        double totalMonthDuration = 0;
        long totalMonthSeconds = 0;
        String date = "";
        for(Entry_Sructure entry : monthLogs){
            totalMonthDuration += Double.parseDouble(entry.getTracker_distance());
            totalMonthSeconds += Double.parseDouble(entry.getTracker_time());
            date = entry.getTracker_date();
        }
        int hours = (int) (totalMonthSeconds / 3600);
        int minutes = (int) ((totalMonthSeconds % 3600) / 60);
        int seconds = (int) (totalMonthSeconds % 60);

        //set texts
        monthDistance.setText("Distance : " + String.valueOf(totalMonthDuration));
        monthDuration.setText("Time : " + String.valueOf(hours) +":" + String.valueOf(minutes) + ":" +  String.valueOf(seconds));
        monthDate.setText("Date : " + String.valueOf(date));
    }

    private void addtoAllTimeViews(){

            double totalAllDuration = 0;
            long totalAllSeconds = 0;
            String date = "";
            for(Entry_Sructure entry : monthLogs){
                totalAllDuration += Double.parseDouble(entry.getTracker_distance());
                totalAllSeconds += Double.parseDouble(entry.getTracker_time());
                date = entry.getTracker_date();
            }
            int hours = (int) (totalAllSeconds / 3600);
            int minutes = (int) ((totalAllSeconds % 3600) / 60);
            int seconds = (int) (totalAllSeconds % 60);

            //set texts
            allTimeDistance.setText("Distance : " + String.valueOf(totalAllDuration));
            allTimeDuration.setText("Time : " + String.valueOf(hours) +":" + String.valueOf(minutes) + ":" +  String.valueOf(seconds));
            allTimeDate.setText("Date : " + String.valueOf(date));

    }

    // same day
    public void checkToday(String dateToday,Entry_Sructure entry) {

        if(dateToday.equals(entry.getTracker_date())){
            Log.i("trackerappdate","its today");

            //add to the today array list
            dayLogs.add(entry);
        }

    }

    // same month
    private void checkMonth(String dateToday,Entry_Sructure entry) {

        String monthOne = dateToday.substring(3,6);
        String monthTwo = entry.getTracker_date().substring(3,6);

        if(monthOne.equals(monthTwo)){

            //add to the month array list
            monthLogs.add(entry);
        }
    }

}
