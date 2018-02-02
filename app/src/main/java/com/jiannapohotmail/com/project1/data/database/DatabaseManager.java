package com.jiannapohotmail.com.project1.data.database;

/**
 * Created by Stamatis on 2/2/2018.
 */

public class DatabaseManager {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MapPointContract.MapPointEntry.TABLE_NAME + " (" +
                    MapPointContract.MapPointEntry._ID + " INTEGER PRIMARY KEY," +
                    MapPointContract.MapPointEntry.COLUMN_NAME_TITLE + " TEXT," +
                    MapPointContract.MapPointEntry.COLUMN_NAME_DESCRIPTION + " TEXT)";


}
