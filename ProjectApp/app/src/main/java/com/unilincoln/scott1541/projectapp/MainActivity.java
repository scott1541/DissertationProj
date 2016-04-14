package com.unilincoln.scott1541.projectapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    DatabaseHelper catDb;
    //DatabaseHelper catDb = new DatabaseHelper();
    //catDb = new DatabaseHelper(this);
    //SQLiteDatabase db = new catDb.getWritableDatabase();

    private static final String TAG = "bluetooth2";
    Button btnOn, btnOff;
    TextView txtArduino;
    TextView countvalue;
    TextView txtshow;
    Handler h;

    boolean start = false;
    int prevHour;
    String curDate;
    int recVal = 0;
    int countVal = 0;
    int test = 1;

    final int RECIEVE_MESSAGE = 1;		// Status  for Handler
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder sb = new StringBuilder();

    private ConnectedThread mConnectedThread;

    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-address of Bluetooth module
    private static String address = "98:D3:31:70:78:3F";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        catDb = new DatabaseHelper(this);

        try {

            catDb.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }
        try {

            catDb.openDataBase();

        } catch(SQLException sqle){

            //throw sqle;

        }



        setContentView(R.layout.activity_main);

        txtArduino = (TextView) findViewById(R.id.textView);   //Cat status
        countvalue = (TextView) findViewById(R.id.textView6);  //Count value for showing data
        txtshow = (TextView) findViewById(R.id.textView7);   //Data show label
        countvalue.setVisibility(View.GONE);
        txtshow.setVisibility(View.GONE);

        btAdapter = BluetoothAdapter.getDefaultAdapter();  // Get Bluetooth adapter
        checkBTState();

        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case RECIEVE_MESSAGE:													// if receive massage
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);					// create string from bytes array
                        sb.append(strIncom);												// append string
                        int endOfLineIndex = sb.indexOf("\r\n");							// determine the end-of-line
                        if (endOfLineIndex > 0) { 											// if end-of-line,
                            String sbprint = sb.substring(0, endOfLineIndex);				// extract string
                            sb.delete(0, sb.length());										// and clear
                            countvalue.setText(sbprint); 	        // update TextView
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            curDate = sdf.format(c.getTime());
                            try {
                                if (!start) {
                                    prevHour = c.get(Calendar.HOUR_OF_DAY);
                                    start = true;
                                }
                                String[] splitInc = sbprint.split("\\:");  //Split incoming string into 3 seperate strings for date, time and count
                                txtArduino.setText("Your cat is within 10m!");

                                recVal = Integer.parseInt(splitInc[1]);

                                Log.d(TAG, "Count++... " + countVal + "  " + curDate);

                                countVal = countVal + recVal;
                                int tempHour = c.get(Calendar.HOUR_OF_DAY);
                                tempHour = tempHour - 1;
                                if (prevHour == tempHour){

                                    Log.d(TAG, "INSERTING INTO DB: " + curDate + "  " + tempHour + "  " + countVal);
                                    catDb.insertData(curDate, tempHour, countVal);

                                    countVal = 0;
                                    start = false;
                                }

                            } catch (Exception e){
                                Log.d(TAG, "Error recieving data... " + e);
                            }


                        }
                        break;
                }
            };
        };
    }

    public void viewActivity (View view){
        Intent intent = new Intent(this, ActivityGraph.class);
        startActivity(intent);
        Log.d(TAG, "Act View button pressed!  ");
    }

    public void feedingTime (View view){
        Intent intent = new Intent(this, FeedingTime.class);
        startActivity(intent);
        Log.d(TAG, "Feeding button pressed!  ");
    }

    public void tempIns (View view){
        countvalue.setVisibility(View.VISIBLE);
        txtshow.setVisibility(View.VISIBLE);
        Log.d(TAG, "Data button pressed!  ");
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method  m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "...onResume - try connect...");
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(TAG, "...Connecting...");
        try {
            btSocket.connect();
            Log.d(TAG, "....Connection ok...");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream
        Log.d(TAG, "...Create Socket...");

        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        try     {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }

    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        if(btAdapter==null) {
            errorExit("Fatal Error", "Bluetooth not support");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);		// Get number of bytes and message in buffer
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();		// Send to message queue Handler
                } catch (IOException e) {
                    break;
                }
            }
        }

    }
}