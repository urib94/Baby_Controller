package com.baby_controller.src;

import android.bluetooth.BluetoothAdapter;

import com.baby_controller.BluetoothConnectionManager;

import java.sql.Time;

public class Config {
    public static final int NUM_OF_BYTES = 4;

    private static LocalUser CUUR_USER;

    public static double FOOD = 1;

    public static final long TIME_BETWEEN_MEALS = 90000;

    public static final Time DEAFULT_BREAKFAST_TIME = new Time(8,0,0);

    public static final long TEN_MIN = 600000;

    private static BluetoothConnectionManager mBluetoothManger = new BluetoothConnectionManager();

    private static BluetoothAdapter mBluetoothAdapter;


    public Config(){}



    public static synchronized  BluetoothConnectionManager getBluetoothConnectionManger(){
        return  mBluetoothManger;
    }

    public static synchronized void setMBluetoothConnectionManger(BluetoothConnectionManager newMBluetoothManger){
        mBluetoothManger = newMBluetoothManger;
    }

    public static synchronized BluetoothAdapter getmBluetoothAdapter(){
        return mBluetoothAdapter;
    }

    public static synchronized void setmBluetoothAdapter(BluetoothAdapter newMBluetoothAdapter){
        mBluetoothAdapter = newMBluetoothAdapter;
    }

    public static synchronized LocalUser getCurrentUser() {
        return CUUR_USER;
    }

    public static synchronized void setCurrentUser(LocalUser user) {
        CUUR_USER = new LocalUser(user);
    }

//    public static void updateCurrUserData() {
//        FirebaseDatabase.getInstance().getReference().child("Users").child(getCurrentUser().getUid())
//                .setValue(getCurrentUser());
//    }
}
