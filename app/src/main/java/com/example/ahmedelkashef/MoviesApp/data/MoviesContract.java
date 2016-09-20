package com.example.ahmedelkashef.MoviesApp.data;

import android.provider.BaseColumns;

/**
 * Created by ahmedelkashef on 8/21/2016.
 */
public class MoviesContract {

    MoviesContract(){}

    public static final class MovieEntry implements BaseColumns{

        public static final String TABLE_NAME = "movie" ;
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "title";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_RATE = "rate";
        public static final String COLUMN_FAVORITE = "favorite";


    }

}
