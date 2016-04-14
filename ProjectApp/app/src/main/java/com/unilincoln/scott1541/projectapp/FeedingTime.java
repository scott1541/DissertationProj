package com.unilincoln.scott1541.projectapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class FeedingTime extends AppCompatActivity {

    int catWeight = 5;
    int actValue = 250;  //Arbitrary value for activity since database does not work
    int enPerkg = 1;  //Amount of energy required per kg of body weight, basic value used as proof of concept due to database issues
    int totalEnerg;
    String foodQuant;
    TextView food;
    EditText getWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeding_time);
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

         food = (TextView) findViewById(R.id.textView5);
         getWeight = (EditText) findViewById(R.id.editText);

        totalEnerg = (actValue * enPerkg) * catWeight;  //Calculation to work out energy

        //foodQuant = Integer.toString(totalEnerg);

        //public void calcFood (View view){
            //catWeight = Integer.parseInt(getWeight.getText().toString());
            //catWeight = Integer.parseInt(catWeightS);

            //food.setText(totalEnerg + "kJ");

    //}

}
}
