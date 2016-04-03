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

import java.util.ArrayList;

public class ActivityGraph extends AppCompatActivity {

    private static final String TAG = "catactivity";
    String date = "30:03:2016";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DatabaseHelper catDb = new DatabaseHelper(this);
        Cursor data = catDb.getData(date);

        if (data.getCount() == 0) {
            Log.d(TAG, "Returned no data.");
            //Show message to user
        } else {
            Log.d(TAG, "Database contains " + data.getCount() + " values.");
        }
        String[] time = new String[data.getCount()];
        int[] count = new int[data.getCount()];

        if (data.moveToNext()) {
            for (int i = 0; i < data.getCount(); i++) {
                time[i] = data.getString(2);
                count[i] = data.getInt(3);
            }
        }
/*
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int j = 0; j < time.length; j++)
        {
            entries.add(new BarEntry(count[j]), count[j]);
        } */

        Log.d(TAG, "SUCCESS!!!!!!!!" + time[150] + count[150]);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(12f, 3));
        entries.add(new BarEntry(18f, 4));
        entries.add(new BarEntry(9f, 5));
        entries.add(new BarEntry(4f, 6));
        entries.add(new BarEntry(8f, 7));
        entries.add(new BarEntry(6f, 8));
        entries.add(new BarEntry(12f, 9));
        entries.add(new BarEntry(18f, 10));
        entries.add(new BarEntry(9f, 11));
        entries.add(new BarEntry(10f, 12));
        entries.add(new BarEntry(4f, 13));
        entries.add(new BarEntry(8f, 14));
        entries.add(new BarEntry(6f, 15));
        entries.add(new BarEntry(12f, 16));
        entries.add(new BarEntry(18f, 17));
        entries.add(new BarEntry(9f, 18));
        entries.add(new BarEntry(4f, 19));
        entries.add(new BarEntry(8f, 20));
        entries.add(new BarEntry(6f, 21));
        entries.add(new BarEntry(12f, 22));
        entries.add(new BarEntry(18f, 23));

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

        BarChart chart = new BarChart(this);
        setContentView(chart);

        BarData data2 = new BarData(labels, dataset);
        chart.setData(data2);
        chart.setDescription("# of cat steps taken...");
    }

}
