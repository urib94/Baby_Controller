package com.baby_controller;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.recyclerview.widget.RecyclerView;

import com.baby_controller.src.Baby;
import com.baby_controller.src.Config;
import com.baby_controller.src.LocalUser;
import com.baby_controller.src.util.MyBluetoothService;
import com.google.firebase.FirebaseApp;

import java.io.IOException;


public class FeedingActivity extends BluetoothManager {
    RecyclerView recyclerView;
    private ListView hungryBabies;
    private ArrayAdapter<Baby> babies;
    MyBluetoothService myBluetoothService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeding_activity);
        recyclerView = new RecyclerView(this);
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        System.out.println(btAdapter.getBondedDevices());
        babies = new ArrayAdapter<Baby>(this,android.R.layout.simple_list_item_1);
        LocalUser us = Config.CUUR_USER;
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    public boolean feedBaby() {
        new Thread() {
            public void run() {
                double amout = 0;
                byte[] data = new byte[4];
                try {
                    mBTSocket.getInputStream().read(data, 0, Config.NUM_OF_BYTES);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (data[0] + data[1] + data[2] + data[3] != 0) {
                    amout = bytesToDouble(data);

                }
            }
        };
        return  true;
    }

    //conect to bluetooth device named "baby-controller"
    public void connectToBluetoothDevice(){
        //connect to bluetooth device named "baby-controller"
        //make a bluetooth device object
        //connect to the device
        //set the device as the current device

    }

}
