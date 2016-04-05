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

    public static final String DB_NAME = "cat_track1"; //cat_track_v1.db
    public static final int DB_VERSION = 1;
    public static final String TBL_NAME = "cat_1";
    public static final String Col1 = "id";
    public static final String Col2 = "date";
    public static final String Col3 = "time";
    public static final String Col4 = "count";
    public int index = 0;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String init = "CREATE TABLE IF NOT EXISTS " + TBL_NAME + " (id INTEGER AUTOINCREMENT, date DATE, time INTEGER, count INTEGER);";
        db.execSQL(init);
       // db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_NAME + "(ID INT AUTO_INCREMENT, DATE CHAR (10), TIME INT, COUNT INT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_NAME);
        onCreate(db);
    }

    public void insertData(String dateV, int timeV, int countV)
    {
        Log.d("DB Helper: ","Passed values: " + dateV + timeV + countV);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        //cValues.put(Col1, index);
        cValues.put(Col2, dateV);
        cValues.put(Col3, timeV);
        cValues.put(Col4, countV);
        db.insert(TBL_NAME, null, cValues);

    }

    public Cursor getData(String date)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TBL_NAME + " WHERE date = " + date, null);
        return data;
    }
}
