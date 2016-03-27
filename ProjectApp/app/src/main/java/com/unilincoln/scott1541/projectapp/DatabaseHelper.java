package com.unilincoln.scott1541.projectapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Scott on 26/03/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "cat_track.db";
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_NAME);
        onCreate(db);
    }

    public boolean insertData(String date, String time, int count)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(C2,date);
        contentValues.put(C3,time);
        contentValues.put(C4,count);
        long result = db.insert(TBL_NAME, null, contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }
}
