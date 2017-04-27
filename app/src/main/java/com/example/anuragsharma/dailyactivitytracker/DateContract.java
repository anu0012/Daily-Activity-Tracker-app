package com.example.anuragsharma.dailyactivitytracker;

import android.provider.BaseColumns;

/**
 * Created by anuragsharma on 26/12/16.
 */

public class DateContract {

    public DateContract(){}

    public static final String DATABASE_NAME = "Date.db";
    public static final int DATABASE_VERSION = 1;

    public class DateEntry implements BaseColumns{
        public static final String TABLE_NAME = "Date";
        public static final String _ID = "_id";
    }

    public class ActivityEntry implements BaseColumns{
        public static final String TABLE_NAME = "Activity";
        public static final String _ID = "_id";
        public static final String _category = "category";
        public static final String COLUMN_START = "Start_time";
        public static final String COLUMN_END = "end_time";
        public static final String COLUMN_NAME = "name";
    }


}
