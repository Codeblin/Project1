package com.jiannapohotmail.com.project1.data.database;

import android.provider.BaseColumns;

/**
 * Created by Stamatis on 1/2/2018.
 */

public final class MapPointContract {

    private MapPointContract() {}

    public static class MapPointEntry implements BaseColumns {
        public static final String TABLE_NAME = "map_points";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PIC = "picture";
        public static final String COLUMN_NAME_VIDEO = "video";
        public static final String COLUMN_NAME_LAT = "lat";
        public static final String COLUMN_NAME_LNG = "lng";
        public static final String COLUMN_NAME_ACTIVE = "isActive";
    }

}
