package com.unilincoln.scott1541.projectapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ActivityGraph extends AppCompatActivity {

    private static final String TAG = "catactivity";
    //String date = "04-04-2016";
    DatabaseHelper catDb = new DatabaseHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_graph);


        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(c.getTime());

        //ReturnChart(date);

    }

    public void ReturnChart(String rDate){

        Cursor data = catDb.getData(rDate);

        if (data.getCount() == 0) {
            Log.d(TAG, "Returned no data.");
            //Show message to user
        } else {
            Log.d(TAG, "Database contains " + data.getCount() + " values.");
        }
        String[] datee = new String[data.getCount()];
        String[] time = new String[data.getCount()];
        int[] count = new int[24];


        if (data.moveToFirst()) {
            for (int i = 0; i < data.getCount(); i++) {
                datee[i] = data.getString(1);
                time[i] = data.getString(2);
                count[i] = data.getInt(3);
                Log.d(TAG, "DATA: " + data.getString(0) + " " + data.getString(1) + " " + data.getString(2) + " " + data.getString(3));
            }
        }


        try {
            Log.d(TAG, "SUCCESS!!!!!!!!" + datee[3] + time[3] + count[3]);

            BarChart bChar = (BarChart) findViewById(R.id.chart);

            ArrayList<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(count[0], 0));
            entries.add(new BarEntry(count[1], 1));
            entries.add(new BarEntry(count[2], 2));
            entries.add(new BarEntry(count[3], 3));
            entries.add(new BarEntry(count[4], 4));
            entries.add(new BarEntry(count[5], 5));
            entries.add(new BarEntry(count[6], 6));
            entries.add(new BarEntry(count[7], 7));
            entries.add(new BarEntry(count[8], 8));
            entries.add(new BarEntry(count[9], 9));
            entries.add(new BarEntry(count[10], 10));
            entries.add(new BarEntry(count[11], 11));
            entries.add(new BarEntry(count[12], 12));
            entries.add(new BarEntry(count[13], 13));
            entries.add(new BarEntry(count[14], 14));
            entries.add(new BarEntry(count[14], 15));
            entries.add(new BarEntry(count[16], 16));
            entries.add(new BarEntry(count[17], 17));
            entries.add(new BarEntry(count[18], 18));
            entries.add(new BarEntry(count[19], 19));
            entries.add(new BarEntry(count[20], 20));
            entries.add(new BarEntry(count[21], 21));
            entries.add(new BarEntry(count[22], 22));
            entries.add(new BarEntry(count[23], 23));

            BarDataSet dataset = new BarDataSet(entries, "# of Steps");

            ArrayList<String> labels = new ArrayList<String>();
            labels.add("00:00");
            labels.add("01:00");
            labels.add("02:00");
            labels.add("03:00");
            labels.add("04:00");
            labels.add("05:00");
            labels.add("06:00");
            labels.add("07:00");
            labels.add("08:00");
            labels.add("09:00");
            labels.add("10:00");
            labels.add("11:00");
            labels.add("12:00");
            labels.add("13:00");
            labels.add("14:00");
            labels.add("15:00");
            labels.add("16:00");
            labels.add("17:00");
            labels.add("18:00");
            labels.add("19:00");
            labels.add("20:00");
            labels.add("21:00");
            labels.add("22:00");
            labels.add("23:00");

            BarData data2 = new BarData(labels, dataset);
            bChar.setData(data2);
            bChar.setDescription("Number of steps taken by Felix");
        } catch (Exception e) {
            Log.d(TAG, "Error!!! " + e);
        }


    }

    public void todayAc(View view) {   //Today
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(c.getTime());

        ReturnChart(date);
    }

    public void yesterdayAc(View view){   //Yesterday

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(c.getTime());

        ReturnChart(date);
    }

    public void befyesterdayAc(View view) {  //THIS BUTTON SHOWS HARD-CODED DATA FOR DEMO PURPOSES

        try{
        BarChart bChar = (BarChart) findViewById(R.id.chart);

            ArrayList<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(20, 0));
            entries.add(new BarEntry(20, 1));
            entries.add(new BarEntry(50, 2));
            entries.add(new BarEntry(60, 3));
            entries.add(new BarEntry(100, 4));
            entries.add(new BarEntry(120, 5));
            entries.add(new BarEntry(50, 6));
            entries.add(new BarEntry(90, 7));
            entries.add(new BarEntry(10, 8));
            entries.add(new BarEntry(10, 9));
            entries.add(new BarEntry(15, 10));
            entries.add(new BarEntry(75, 11));
            entries.add(new BarEntry(25, 12));
            entries.add(new BarEntry(50, 13));
            entries.add(new BarEntry(50, 14));
            entries.add(new BarEntry(80, 15));
            entries.add(new BarEntry(100, 16));
            entries.add(new BarEntry(110, 17));
            entries.add(new BarEntry(80, 18));
            entries.add(new BarEntry(50, 19));
            entries.add(new BarEntry(20, 20));
            entries.add(new BarEntry(10, 21));
            entries.add(new BarEntry(5, 22));
            entries.add(new BarEntry(5, 23));

            BarDataSet dataset = new BarDataSet(entries, "# of Steps");

            ArrayList<String> labels = new ArrayList<String>();
            labels.add("00:00");
            labels.add("01:00");
            labels.add("02:00");
            labels.add("03:00");
            labels.add("04:00");
            labels.add("05:00");
            labels.add("06:00");
            labels.add("07:00");
            labels.add("08:00");
            labels.add("09:00");
            labels.add("10:00");
            labels.add("11:00");
            labels.add("12:00");
            labels.add("13:00");
            labels.add("14:00");
            labels.add("15:00");
            labels.add("16:00");
            labels.add("17:00");
            labels.add("18:00");
            labels.add("19:00");
            labels.add("20:00");
            labels.add("21:00");
            labels.add("22:00");
            labels.add("23:00");

            BarData data2 = new BarData(labels, dataset);
            bChar.setData(data2);
            bChar.setDescription("Number of steps taken by Felix");
        } catch (Exception e) {
            Log.d(TAG, "Error!!! " + e);
        }


    }

}
