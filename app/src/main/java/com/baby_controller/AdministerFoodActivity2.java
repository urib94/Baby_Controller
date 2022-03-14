package com.baby_controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.baby_controller.src.Config;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class AdministerFoodActivity2 extends AppCompatActivity {
    private BluetoothConnectionManager btManager = new BluetoothConnectionManager();
    private static final String TAG = "Administer Food";
    protected TextView measuredWight;
    private Switch enterManually;
    public BluetoothSocket socket;
    private String address = null;


    private Handler defaultMHandler; // Our main handler that will receive callback notifications
    public Handler mHandler; // Our main handler that will receive callback notifications
    private ConnectedThread mConnectedThread; // bluetooth background worker thread to send and receive data
    private static BluetoothSocket mBTSocket = null; // bi-directional client-to-client data path

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier
    private ArrayAdapter<String> mBTArrayAdapter;

    // #defines for identifying shared types between calling functions
    private final static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status
    private BluetoothAdapter mBTAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administer_food_activity);
        configureButtons();


        FirebaseDatabase.getInstance().getReference().child("Users").child(FeedingActivity2.babyToFeed.getParentUid())
                .child(String.valueOf(FeedingActivity2.babyToFeed.getIndexInParent())).child("history")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

        TextView amount = findViewById(R.id.measured_wight);

        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(android.os.Message msg){
                if(msg.what == MESSAGE_READ){
                    String readMessage = null;
                    readMessage = new String((byte[]) msg.obj, StandardCharsets.UTF_8);
                    measuredWight.setText(readMessage);
//                    String tmp = readMessage;
//                    Config.setFoodAmount(parseMessage(readMessage));
                }
            }
        };

    }

    // Enter here after user selects "yes" or "no" to enabling radio
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent Data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, Data);
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

            }

        }
    }

    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // add the name to the list
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                mBTArrayAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                address = snapshot.child("defaultDevice").getValue(String.class);
                Config.getCurrentUser().setBaseAddress(address);
                System.out.println("address1 = " + address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        Config.getCurrUserRef().addListenerForSingleValueEvent(listener);
        Config.getCurrUserRef().removeEventListener(listener);
        System.out.println("address = " + Config.getCurrentUser().getDefaultDeviceAddress());
        if(Config.getCurrentUser().getDefaultDeviceAddress() != null){
            startConnection(Config.getCurrentUser().getDefaultDeviceAddress(),"baby-controller");
            BluetoothAdapter mBTAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = mBTAdapter.getRemoteDevice(Config.getCurrentUser().getDefaultDeviceAddress());
            try {
                socket = createBluetoothSocket(device);
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert socket != null;
            mConnectedThread = new ConnectedThread(socket);
            mConnectedThread.start();

        }


    }

    private void configureButtons() {
        Button feed = (Button) findViewById(R.id.feed_now);
        measuredWight = (TextView) findViewById(R.id.measured_wight);
        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FeedingActivity2.babyToFeed != null) {
                    if(enterManually.isChecked()){
                        FeedingActivity2.babyToFeed.eatingNextMeal(Integer.parseInt(measuredWight.getText().toString()));
                    }
                    else if(!measuredWight.getText().toString().equals("0")){
                        FeedingActivity2.babyToFeed.eatingNextMeal(Integer.parseInt(measuredWight.getText().toString()));
                        toastMessage(FeedingActivity2.babyToFeed.getName() + "ate " + String.valueOf(Config.getFoodAmount()) + " mL");
//                        String[] msg = new String[4];
//                        msg[0] = "Yamm!!";
//                        msg[2] = FeedingActivity2.babyToFeed.getName() + "is about to eat " + String.valueOf(Config.getFoodAmount()) + " mL";
//                        msg[3] = "OPEN_MAIN_ACTIVITY";
//                        NotificationManegerActivity.sendWithOtherThread("token", FeedingActivity2.babyToFeed.getRegistrationToken(),msg,FeedingActivity2.babyToFeed.getRegistrationToken(), AdministerFoodActivity.this);



                    }
                    mHandler = new Handler(Looper.getMainLooper()){
                        @Override
                        public void handleMessage(android.os.Message msg){
                            if(msg.what == MESSAGE_READ){
                                String readMessage = null;
                                readMessage = new String((byte[]) msg.obj, StandardCharsets.UTF_8);
                                measuredWight.setText(readMessage);
                            }


                        }
                    };
                }
            }
        });

        enterManually = findViewById(R.id.manually);


    }

    public void startConnection(String address, String name) {
        // Spawn a new thread to avoid blocking the GUI one
        new Thread() {

            private final static int CONNECTING_STATUS = 3;
            @Override
            public void run() {
                boolean fail = false;

                BluetoothDevice device;

                device = Config.getDefaultDevice();

                try {

                    assert device != null;
                    socket = createBluetoothSocket(device);
                } catch (IOException e) {
                    fail = true;
                    Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                }
                // Establish the Bluetooth socket connection.
                try {
                    socket.connect();
                } catch (IOException e) {
                    try {
                        fail = true;
                        socket.close();
                        mHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
                                .sendToTarget();
                    } catch (IOException e2) {
                        //insert code to deal with this
                        Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                    }
                }
                if (fail == false) {
                    mConnectedThread = new ConnectedThread(socket);
                    mConnectedThread.start();

                    mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name)
                            .sendToTarget();
                }

            }

        }.start();

    }

    // Spawn a new thread to avoid blocking the GUI one
    public void establishConnection(String address, String name){
        new Thread()
        {
            public void run() {
                boolean fail = false;

                BluetoothDevice device;
                if(Config.getCurrentUser().getDefaultDeviceAddress() == null) {
                    device = mBTAdapter.getRemoteDevice(address);
                }else {
                    device = Config.getDefaultDevice();
                }

                try {
                    mBTSocket = createBluetoothSocket(device);
                } catch (IOException e) {
                    fail = true;
                    Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                }
                // Establish the Bluetooth socket connection.
                try {
                    mBTSocket.connect();
                } catch (IOException e) {
                    try {
                        fail = true;
                        mBTSocket.close();
                        mHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
                                .sendToTarget();
                    } catch (IOException e2) {
                        //insert code to deal with this
                        Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                    }
                }
                if(fail == false) {
                    mConnectedThread = new ConnectedThread(mBTSocket);
                    mConnectedThread.start();

                    mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name)
                            .sendToTarget();
                }
            }
        }.start();
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connection with BT device using UUID
    }

    public class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;


        public ConnectedThread(BluetoothSocket socket){
            mmSocket = socket;
            InputStream tmpIn = null;


            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {

                try {
                    // Read from the InputStream
                    bytes = mmInStream.available();
                    if(bytes != 0) {
                        SystemClock.sleep(100); //pause and wait for rest of data.
                        bytes = mmInStream.available(); // how many bytes are ready to be read?
                        bytes = mmInStream.read(buffer, 0, bytes); // record how many bytes we actually read
                        mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                .sendToTarget(); // Send the obtained bytes to the UI activity
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    break;
                }
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private void toastMessage(String s) {
        Toast.makeText(this ,s,Toast.LENGTH_SHORT).show();
    }
}
