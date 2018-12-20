package com.example.ahmadsami.runningtracker;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ahmadsami.runningtracker.contentprovider.Tracker_Content_Provider;

import java.util.Random;

// interacts with the database
public class DB_Handler extends SQLiteOpenHelper {

    // initializing variables related to DB
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "trackerDB.db";
    public static final String TABLE_TRACKER = "history";


    // initializing variables related to table "history"
    public static final String COLUMN_TRACKER_ID ="tracker_id";
    public static final String COLUMN_TRACKER_TIME = "tracker_time";
    public static final String COLUMN_TRACKER_DISTANCE = "tracker_distance";
    public static final String COLUMN_TRACKER_DATE = "tracker_date";


    public static ContentResolver contentResolver;

    // random number min and max
    final int min = 101;
    final int max = 10101;
    int random;

    public DB_Handler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

        contentResolver = context.getContentResolver();
    }

    public  ContentResolver getContentResolver() {
        return contentResolver;
    }

    // creates a new table in the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // creates SQL statement to create table recipes
        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_TRACKER + "(" + COLUMN_TRACKER_ID + " INTEGER PRIMARY KEY," +
                COLUMN_TRACKER_TIME + " TEXT," + COLUMN_TRACKER_DISTANCE + " TEXT," + COLUMN_TRACKER_DATE + " TEXT" + ")";

        // execute create table sql statement
        sqLiteDatabase.execSQL(CREATE_RECIPES_TABLE);
    }

    // called when the handler is invoked with a greater database version number from the one previously used
    // updates table schema in an existing database
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // remove table if it already exists in the database
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKER);
        onCreate(sqLiteDatabase);
    }


    // inserts new entry to the database
    public void addEntry(Entry_Sructure entry){
        ContentValues contentValues = new ContentValues();

        // generate id
        random = new Random().nextInt((max - min) + 1) + min;

        // set id
        entry.setTracker_id(random);

        // insert recipe ID
        contentValues.put(COLUMN_TRACKER_ID,entry.getTracker_ID());
        // insert recipe title
        contentValues.put(COLUMN_TRACKER_TIME,entry.getTracker_time());
        // insert recipe content
        contentValues.put(COLUMN_TRACKER_DISTANCE,entry.getTracker_distance());
        Log.i("trackerappdb",entry.getTracker_distance());

        contentValues.put(COLUMN_TRACKER_DATE,entry.getTracker_date());

        contentResolver.insert(Tracker_Content_Provider.CONTENT_URI,contentValues);

    }

    /*
    // delete recipe from database
    public boolean deleteLog(String recipeTitle){
        boolean result = false;
        String selection = "recipe_title = \"" + recipeTitle + "\" ";
        int rowsDeleted = contentResolver.delete(RecipeContentProvider.CONTENT_URI, selection,null);

        if(rowsDeleted > 0){
            result = true;
        }
        return result;
    }
*/


}