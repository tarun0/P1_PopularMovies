package com.example.p1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Tarun on 31/01/2016.
 * References taken from https://github.com/codepath/android_guides/wiki/Local-Databases-with-SQLiteOpenHelper
 */
public class PostsDatabaseHelper extends SQLiteOpenHelper {

    //Database Info
    private static final String DATABASE_NAME = "favouritesDatabase";
    private static final int DATABASE_VERSION = 1;

    //Table Info
    private static final String TABLE_FAVOURITES = "favouritesTable";

    //Column Detail
    private static final String KEY_ID = "id";
    private static final String KEY_SYNOPSIS = "synopsis";
    private static final String KEY_POSTER_LINK = "posterLink";
    private static final String KEY_RELEASE_DATE = "relDate";
    private static final String KEY_RATING = "rating";
    private static final String KEY_TITLE = "title";

    public PostsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE = "create table " + TABLE_FAVOURITES + "(" +
                KEY_ID + " string " + "primary key, " +
                KEY_SYNOPSIS + " string, " + KEY_RELEASE_DATE + " string ," +
                KEY_RATING + " string, " + KEY_TITLE + " string, " + KEY_POSTER_LINK + " string)";

        Log.e("Create Query", CREATE_TABLE);
        db.execSQL(CREATE_TABLE);
        Log.e("TABLE CREATED", "*************");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int previousVersion, int newVersion){
        if (previousVersion != newVersion){
            //Drop previous table and create new table
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_FAVOURITES);
            onCreate(db);
        }
    }

    public void insertMovies(String id, String synopsis, String rel_date, String rating, String title, String poster){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, id);
        contentValues.put(KEY_SYNOPSIS, synopsis);
        contentValues.put(KEY_RELEASE_DATE, rel_date);
        contentValues.put(KEY_RATING, rating);
        contentValues.put(KEY_TITLE, title);
        contentValues.put(KEY_POSTER_LINK, poster);

        Log.e("Before inserting", "**********");

         db.insert(TABLE_FAVOURITES, "null", contentValues);

        Log.e("After inserting", "*********");
    }

    public void deleteMovie(String movieID){
        SQLiteDatabase db = this.getWritableDatabase();

         db.delete(TABLE_FAVOURITES, " ID = ?", new String[]{movieID});
        Log.e("DELETED", "$$$$$$$$$$$$");
    }

    public Cursor getMovieValues(String title){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + TABLE_FAVOURITES + " WHERE TITLE = ? ", new String[]{title});
        return cursor;
    }

    public ArrayList<String> getValues(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT TITLE FROM " + TABLE_FAVOURITES , null);

        ArrayList<String> array_list = new ArrayList<String>();

        res.moveToFirst();

        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex(KEY_TITLE)));
            res.moveToNext();
        }
        res.close();
        return array_list;

    }

    public ArrayList<String> getID(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT ID FROM " + TABLE_FAVOURITES , null);

        ArrayList<String> array_list = new ArrayList<String>();

        res.moveToFirst();

        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex(KEY_ID)));
            res.moveToNext();
        }
        res.close();
        return array_list;

    }

    public boolean isEmpty(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_FAVOURITES);
        return (!(numRows>0));
    }

    public boolean ispresent(String movieId){

        String Query = "Select * from " + TABLE_FAVOURITES + " where " + KEY_ID + " = " + movieId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }
}
