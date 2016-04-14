package com.unilincoln.scott1541.projectapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.unilincoln.scott1541.projectapp/databases/";

    private static String DB_NAME = "catapp.db";
    public static final String TBL_NAME = "catact";
    public static final String Col1 = "_id";
    public static final String Col2 = "Date";
    public static final String Col3 = "Time";
    public static final String Col4 = "Count";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    public DatabaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
        //DB_PATH = myContext.getDatabasePath(DB_NAME).getPath();
    }

    public void createDataBase() throws IOException{

        boolean dbExist = checkDataBase();

        if(dbExist){
            Log.d("DB Helper: ", "Database already exists");
        }else{

            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            Log.d("DB Helper: ", "Database doesn't exist yet");

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException{

        InputStream myInput = myContext.getAssets().open(DB_NAME);


        String outFileName = DB_PATH + DB_NAME;

        OutputStream myOutput = new FileOutputStream(outFileName);


        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {

        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_NAME);
        onCreate(db);
    }

    public Cursor getData(String date)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TBL_NAME  +  " WHERE Date = '" + date + "';", null);
        return data;
    }

    public void insertData(String dateV, int timeV, int countV)
    {
        Log.d("DB Helper: ","Inserting values: " + dateV + timeV + countV);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        //cValues.put(Col1, indV);
        cValues.put(Col2, dateV);
        cValues.put(Col3, timeV);
        cValues.put(Col4, countV);
        db.insert(TBL_NAME, null, cValues);
        db.close();
        //db.execSQL("INSERT INTO " + TBL_NAME + " (DATE, TIME, COUNT) VALUES " + "(" + dateV +", " + timeV + ", " + countV + ");");
    }

}