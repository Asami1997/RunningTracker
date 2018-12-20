package com.example.ahmadsami.runningtracker;

import android.net.Uri;

public class Contract_Tracker {


    public static final String AUTHORITY = "com.example.ahmadsami.runningtracker.contentprovider";
    public static final String TRACKER_TABLE = "history";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TRACKER_TABLE);
    public static final String COLUMN_TRACKER_ID ="tracker_id";
    public static final String COLUMN_TRACKER_TIME = "tracker_time";
    public static final String COLUMN_TRACKER_DISTANCE = "tracker_distance";
    public static final String COLUMN_TRACKER_DATE = "tracker_date";
}
