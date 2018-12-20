package com.example.ahmadsami.runningtracker.contentprovider;

// THIS CLASS IS TAKEN FROM LAB 5

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.ahmadsami.runningtracker.DB_Handler;


public class Tracker_Content_Provider extends ContentProvider {

    private static final String AUTHORITY = "com.example.ahmadsami.runningtracker.contentprovider";
    private static final String TRACKER_TABLE = "history";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TRACKER_TABLE);

    public static final int HISTORY = 1;
    public static final int TRACKER_ID = 2;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private DB_Handler db_handler;
    static
    {
        sURIMatcher.addURI(AUTHORITY,TRACKER_TABLE, HISTORY);
        sURIMatcher.addURI(AUTHORITY, TRACKER_TABLE +"/#", TRACKER_ID);
    }

    public Tracker_Content_Provider() {
    }

    public Tracker_Content_Provider(DB_Handler db_handler) {
        this.db_handler = db_handler;
    }

    @Override public int delete(Uri uri, String selection, String[] selectionArgs) {

        Log.i("trackerapp","in content provider delete function  ");

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = db_handler.getWritableDatabase();

        int rowsDeleted = 0;

        switch (uriType) {

            case HISTORY:

                rowsDeleted = sqlDB.delete(DB_Handler.TABLE_TRACKER, selection, selectionArgs);
                break;

            case TRACKER_ID:

                String id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(DB_Handler.TABLE_TRACKER, DB_Handler.COLUMN_TRACKER_ID
                            + "=" + id, null);
                }else {
                    rowsDeleted = sqlDB.delete(DB_Handler.TABLE_TRACKER, DB_Handler.COLUMN_TRACKER_ID + "="
                                    + id + " and " + selection,
                            selectionArgs);
                }

                break;

            default:

                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public
    Uri insert(Uri uri, ContentValues values) {

        Log.i("trackerapp","in content provider insert function ");

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = db_handler.getWritableDatabase();
        long id = 0;

        switch (uriType) {
            case HISTORY:
                id = sqlDB.insert(DB_Handler.TABLE_TRACKER, null,values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "+ uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(TRACKER_TABLE + "/" + id);
    }

    @Override
    public boolean onCreate() {

        Log.i("trackerapp","in content provider onCreate function ");


        db_handler = new DB_Handler(getContext(),null,null,1);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Log.i("trackerapp","in content provider query function ");

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DB_Handler.TABLE_TRACKER);

        int uriType = sURIMatcher.match(uri);

        switch (uriType) {

            case TRACKER_ID:
                queryBuilder.appendWhere(DB_Handler.COLUMN_TRACKER_ID + "=" + uri.getLastPathSegment());
                break;

            case HISTORY:
                break;

            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(db_handler.getReadableDatabase(), projection, selection, selectionArgs, null, null,
                sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),
                uri);
        return cursor;

    }


    @Override public int update(Uri uri, ContentValues values, String selection,
                                String[] selectionArgs) {

        Log.i("trackerapp","in content provider update function ");

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = db_handler.getWritableDatabase();

        int rowsUpdated = 0;

        switch (uriType) {

            case HISTORY:
                rowsUpdated = sqlDB.update(DB_Handler.TABLE_TRACKER, values, selection, selectionArgs);
                break;

            case TRACKER_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(DB_Handler.TABLE_TRACKER, values, DB_Handler.COLUMN_TRACKER_ID + "="
                            + id, null);
                }else {
                    rowsUpdated = sqlDB.update(DB_Handler.TABLE_TRACKER, values, DB_Handler.COLUMN_TRACKER_ID + "="
                            + id + " and " + selection, selectionArgs);
                }

                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;

    }
}
