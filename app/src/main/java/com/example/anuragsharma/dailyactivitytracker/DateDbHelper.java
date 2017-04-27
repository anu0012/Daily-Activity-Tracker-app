package com.example.anuragsharma.dailyactivitytracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by anuragsharma on 26/12/16.
 */

public class DateDbHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_TABLE_DATES = "CREATE TABLE " + DateContract.DateEntry.TABLE_NAME +
            " (" + DateContract.DateEntry._ID + " TEXT PRIMARY KEY )";

    private static final String SQL_CREATE_TABLE_ACTIVITIES = "CREATE TABLE " + DateContract.ActivityEntry.TABLE_NAME +
            " (" + DateContract.ActivityEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP
            + DateContract.ActivityEntry.COLUMN_START + TEXT_TYPE + COMMA_SEP
            + DateContract.ActivityEntry.COLUMN_END + TEXT_TYPE + COMMA_SEP
            + DateContract.ActivityEntry._category + TEXT_TYPE + COMMA_SEP
            + DateContract.ActivityEntry._ID + TEXT_TYPE + COMMA_SEP
            + " FOREIGN KEY ("+DateContract.ActivityEntry._ID+") REFERENCES "+DateContract.DateEntry.TABLE_NAME+"("+DateContract.DateEntry._ID+"))";


    public DateDbHelper(Context context) {
        super(context, DateContract.DATABASE_NAME, null, DateContract.DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_DATES);
        db.execSQL(SQL_CREATE_TABLE_ACTIVITIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + DateContract.DateEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DateContract.ActivityEntry.TABLE_NAME);
        onCreate(db);
    }

    public void insertDate(Date date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DateContract.DateEntry._ID,date.getDate());
        db.insert(DateContract.DateEntry.TABLE_NAME,null,values);
        db.close();
    }

    public void deleteDate(Date date) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DateContract.DateEntry.TABLE_NAME, DateContract.DateEntry._ID + " =?",
                new String[]{String.valueOf(date.getDate())});
        db.close();
    }

    public ArrayList<Date> queryAllDateEntries() {
        ArrayList<Date> datesList = new ArrayList<>();
        final String SQL_QUERY_ALL_DATES = "SELECT * FROM " + DateContract.DateEntry.TABLE_NAME + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_QUERY_ALL_DATES, null);

        if (cursor.moveToFirst()) {
            do {
                Date date = new Date(cursor.getString(0));
                datesList.add(date);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return datesList;
    }

    public void insertActivity(Activity activity,String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DateContract.ActivityEntry.COLUMN_NAME,activity.getActivityName());
        values.put(DateContract.ActivityEntry.COLUMN_START,activity.getStartTime());
        values.put(DateContract.ActivityEntry.COLUMN_END,activity.getEndTime());
        values.put(DateContract.ActivityEntry._category,activity.getCategory());
        values.put(DateContract.ActivityEntry._ID,id);
        db.insert(DateContract.ActivityEntry.TABLE_NAME,null,values);
        db.close();
    }

    public void deleteActivity(Activity activity){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DateContract.ActivityEntry.TABLE_NAME, DateContract.ActivityEntry.COLUMN_NAME + " =?",
                new String[]{String.valueOf(activity.getActivityName())});
        db.close();
    }

    public ArrayList<Activity> queryAllActivityEntries(String id) {
        ArrayList<Activity> activityArrayList = new ArrayList<>();
        final String SQL_QUERY_ALL_ACTIVITIES = "SELECT * FROM " + DateContract.ActivityEntry.TABLE_NAME +
                " WHERE " + DateContract.ActivityEntry._ID + " = '" + id + "';";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_QUERY_ALL_ACTIVITIES, null);

        if (cursor.moveToFirst()) {
            do {
                Activity activity = new Activity(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
                activityArrayList.add(activity);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return activityArrayList;
    }

    public ArrayList<Activity> queryAllActivityEntries() {
        ArrayList<Activity> activityArrayList = new ArrayList<>();
        final String SQL_QUERY_ALL_ACTIVITIES = "SELECT * FROM " + DateContract.ActivityEntry.TABLE_NAME + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_QUERY_ALL_ACTIVITIES, null);

        if (cursor.moveToFirst()) {
            do {
                Activity activity = new Activity(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
                activityArrayList.add(activity);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return activityArrayList;
    }

}
