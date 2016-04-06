package com.unilincoln.scott1541.projectapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
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
    //SQLiteDatabase db = new catDb.getWritableDatabase();

    private static final String TAG = "bluetooth2";

    Button btnOn, btnOff;
    TextView txtArduino;
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

    // MAC-address of Bluetooth module (you must edit this line)
    private static String address = "98:D3:31:70:78:3F";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //final Calendar c = Calendar.getInstance();


        catDb = new DatabaseHelper(this);

        setContentView(R.layout.activity_main);

        //btnOn = (Button) findViewById(R.id.viewAct);					// button LED ON
        //btnOff = (Button) findViewById(R.id.feedTime);				// button LED OFF
        txtArduino = (TextView) findViewById(R.id.textView);		// for display the received data from the Arduino


        btAdapter = BluetoothAdapter.getDefaultAdapter();		// get Bluetooth adapter
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
                            //txtArduino.setText("Data from Arduino: " + sbprint); 	        // update TextView
                            //btnOff.setEnabled(true);
                            //btnOn.setEnabled(true);
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

                                    Log.d(TAG, "INSERTING INTO DB: " + curDate + "  " + tempHour + "  " + countVal); /*
                                    boolean isInserted = catDb.insertData(curDate, tempHour, countVal);
                                    if (isInserted = true) {
                                        Log.d(TAG, "...Inserted to database  " + curDate + "  " + tempHour + "  " + countVal);
                                        Toast.makeText(MainActivity.this, "Data stored", Toast.LENGTH_LONG);
                                        countVal = 0;
                                    } else {
                                        Log.d(TAG, "...Error inserting to database");
                                        Toast.makeText(MainActivity.this, "Error storing data!", Toast.LENGTH_LONG);
                                    }*/

                                    countVal = 0;
                                    start = false;
                                }
                                    /*
                                if (prevHour == splitInc[1]) {
                                    countVal += recVal;
                                    Log.d(TAG, "Count++... " + countVal);
                                } else {

                                    boolean isInserted = catDb.insertData(splitInc[0], splitInc[1], countVal);

                                    if (isInserted = true) {
                                        Log.d(TAG, "...Inserted to database  " + splitInc[0] + "  " + splitInc[1] + "  " + countVal);
                                        Toast.makeText(MainActivity.this, "Data stored", Toast.LENGTH_LONG);
                                        countVal = 0;
                                    } else {
                                        Log.d(TAG, "...Error inserting to database");
                                        Toast.makeText(MainActivity.this, "Error storing data!", Toast.LENGTH_LONG);
                                    }

                                    prevHour = splitInc[1];
                                } */
                            } catch (Exception e){
                                Log.d(TAG, "Error recieving data... " + e);
                            }

                            //prevHour = splitInc[1];

                            //int countVal = Integer.parseInt(splitInc[2]);

                        }
                        //Log.d(TAG, "...String:"+ sb.toString() +  "Byte:" + msg.arg1 + "...");
                        break;
                }
            };
        };
    }

    public void viewActivity (View view){
        Intent intent = new Intent(this, ActivityGraph.class);
        startActivity(intent);
    }

    public void feedingTime (View view){
        Intent intent = new Intent(this, FeedingTime.class);
        startActivity(intent);
    }

    public void tempIns (View view){
        //Intent intent = new Intent(this, FeedingTime.class);
        //startActivity(intent);
        test++;
        catDb.insertData("04-04-2016", test, 887);
        //db.execSQL("INSERT INTO " + "cat_1" + " (DATE, TIME, COUNT) VALUES " + "(" + "24-12-1001" +", " + "12" + ", " + "911" + ");");
        Log.d(TAG, "...Inserted to database  ");
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

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

    /*try {
      btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
    } catch (IOException e) {
      errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
    }*/

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
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

        // Create a data stream so we can talk to server.
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
        // Emulator doesn't support Bluetooth and will return null
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

            // Get the input and output streams, using temp objects because
            // member streams are final
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
                    bytes = mmInStream.read(buffer);		// Get number of bytes and message in "buffer"
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();		// Send to message queue Handler
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String message) {
            Log.d(TAG, "...Data to send: " + message + "...");
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Log.d(TAG, "...Error data send: " + e.getMessage() + "...");
            }
        }
    }
}