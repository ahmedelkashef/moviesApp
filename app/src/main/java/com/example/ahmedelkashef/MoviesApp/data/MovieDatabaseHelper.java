package com.example.ahmedelkashef.MoviesApp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ahmedelkashef.MoviesApp.MovieContext;


public class MovieDatabaseHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + MoviesContract.MovieEntry.TABLE_NAME + " ( " +
            MoviesContract.MovieEntry.COLUMN_ID + " INTEGER PRIMARY KEY," + MoviesContract.MovieEntry.COLUMN_NAME + TEXT_TYPE + COMA_SEP +
            MoviesContract.MovieEntry.COLUMN_IMAGE + TEXT_TYPE + COMA_SEP + MoviesContract.MovieEntry.COLUMN_OVERVIEW + TEXT_TYPE  +
            COMA_SEP + MoviesContract.MovieEntry.COLUMN_DATE  + TEXT_TYPE + COMA_SEP + MoviesContract.MovieEntry.COLUMN_RATE +
            TEXT_TYPE + COMA_SEP + MoviesContract.MovieEntry.COLUMN_FAVORITE + TEXT_TYPE +
            " )" ;
    private static final String SQL_DELETE_ENTRIES  = " DROP TABLE IF EXISTS " + MoviesContract.MovieEntry.TABLE_NAME;

    public static final int DATBASE_VERSION =2 ;
    public static final String DATABASE_NAME = "Movies.db";

    public MovieDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATBASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("SQL Table"," " + SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);

    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public void  insert (MovieContext m){

        SQLiteDatabase db =  this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MoviesContract.MovieEntry.COLUMN_ID, m.getID());
        values.put(MoviesContract.MovieEntry.COLUMN_NAME, m.getTitle());
        values.put(MoviesContract.MovieEntry.COLUMN_IMAGE, m.getImageURL());
        values.put(MoviesContract.MovieEntry.COLUMN_OVERVIEW, m.getOverview());
        values.put(MoviesContract.MovieEntry.COLUMN_DATE, m.getReleaseDate());
        values.put(MoviesContract.MovieEntry.COLUMN_RATE, m.getRate());
        values.put(MoviesContract.MovieEntry.COLUMN_FAVORITE , m.getIsmoviefavaourite());


        long newid = db.insert(MoviesContract.MovieEntry.TABLE_NAME ,null , values);
    }
    public void delete(MovieContext m){

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(MoviesContract.MovieEntry.TABLE_NAME ,MoviesContract.MovieEntry.COLUMN_ID + "=" + m.getID(),null);


    }
    public boolean isTableEmpty(){
        boolean isEmpty = true;
        SQLiteDatabase   db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + MoviesContract.MovieEntry.TABLE_NAME, null);

        if(cursor != null && cursor.getCount() > 0){
            isEmpty = false;
            cursor.close();
        }

        return isEmpty;
    }

    public Cursor getdata (){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(MoviesContract.MovieEntry.TABLE_NAME ,null,null , null , null , null ,null ,null );
        return c;
    }

    public void update(MovieContext m){

        SQLiteDatabase db =  this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MoviesContract.MovieEntry.COLUMN_ID, m.getID());
        values.put(MoviesContract.MovieEntry.COLUMN_NAME, m.getTitle());
        values.put(MoviesContract.MovieEntry.COLUMN_IMAGE, m.getImageURL());
        values.put(MoviesContract.MovieEntry.COLUMN_OVERVIEW, m.getOverview());
        values.put(MoviesContract.MovieEntry.COLUMN_DATE, m.getReleaseDate());
        values.put(MoviesContract.MovieEntry.COLUMN_RATE, m.getRate());
        values.put(MoviesContract.MovieEntry.COLUMN_FAVORITE , m.getIsmoviefavaourite());


        long newid = db.update(MoviesContract.MovieEntry.TABLE_NAME , values , MoviesContract.MovieEntry.COLUMN_ID +" = ?",
                new String[] {String.valueOf(m.getID())} );

    }
    public Cursor getFavoriteData (){

        SQLiteDatabase db = this.getReadableDatabase();
        String Selection = MoviesContract.MovieEntry.COLUMN_FAVORITE + " = ?";
        String[] Args = {"YES"};

        Cursor c = db.query(MoviesContract.MovieEntry.TABLE_NAME ,null,Selection , Args , null , null ,null ,null );
        return c;

    }

    public Cursor getTopratedData(){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + MoviesContract.MovieEntry.TABLE_NAME + " ORDER BY " + MoviesContract.MovieEntry.COLUMN_RATE
                + " DESC",null );
        //db.query(MoviesContract.MovieEntry.TABLE_NAME ,null,null , null , null , null , null ,
        //MoviesContract.MovieEntry.COLUMN_RATE + " DESC"  );
        return c;
    }


}


