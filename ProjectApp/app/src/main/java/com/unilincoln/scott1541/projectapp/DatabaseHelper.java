package com.unilincoln.scott1541.projectapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Scott on 26/03/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "cat_track.db"; //cat_track_v1.db
    public static final int DB_VERSION = 1;
    public static final String TBL_NAME = "cat_1";
    public static final String C1 = "ID";
    public static final String C2 = "DATE";
    public static final String C3 = "TIME";
    public static final String C4 = "COUNT";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_NAME + "(ID INT, DATE VARCHAR, TIME VARCHAR, COUNT INT);");
        //db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_NAME + "(ID INT AUTO_INCREMENT, DATE CHAR (10), TIME INT, COUNT INT, PRIMARY KEY (ID));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_NAME);
        onCreate(db);
    }

    public boolean insertData(String date, int time, int count)
    {
        Log.d("DB Helper: ","Passed values: " + date + time + count);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(C2, date);
        contentValues.put(C3, time);
        contentValues.put(C4, count);
        long result = db.insert(TBL_NAME, null, contentValues);

        //db.execSQL("INSERT INTO " + TBL_NAME + " (DATE,TIME,COUNT) VALUES (" + date +"," + time + "," + count + ");" );
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getData(String date)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TBL_NAME, null); //+ " WHERE DATE = " + date, null);
        return data;
    }
}
