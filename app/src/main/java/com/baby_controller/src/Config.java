package com.baby_controller.src;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.baby_controller.src.util.BluetoothConnectionService;

import java.sql.Time;

public class Config {
    public static final int NUM_OF_BYTES = 4;

    private static LocalUser CUUR_USER;

    public static double FOOD = 0;

    public static final long TIME_BETWEEN_MEALS = 90000;

    public static final Time DEAFULT_BREAKFAST_TIME = new Time(8,0,0);

    public static final long TEN_MIN = 600000;

    private static BluetoothConnectionService mBluetoothConnectionService
            = new BluetoothConnectionService();


    private static BluetoothSocket btSocket;

    public Config(){}



    public static synchronized  BluetoothConnectionService getBluetoothConnectionService(){
        return  mBluetoothConnectionService;
    }

    public static synchronized void setBluetoothConnectionService(BluetoothConnectionService newBluetoothConnectionService){
        mBluetoothConnectionService = newBluetoothConnectionService;
    }


    public static synchronized LocalUser getCurrentUser() {
        if(CUUR_USER != null){
            System.out.println("CurrentUser = " + CUUR_USER.toString());
        }
        else System.out.println("CurrentUser = null");
        return CUUR_USER;
    }

    public static synchronized void setCurrentUser(LocalUser user) {
        CUUR_USER = new LocalUser(user);
    }


    public static synchronized double getFoodAmount(){
        return FOOD;
    }

    public static synchronized void setFoodAmount(double amount){
        FOOD = amount;
    }

    public static synchronized BluetoothSocket getBtSocket() {
        return btSocket;
    }

    public static synchronized void setBtSocket(BluetoothSocket btSocket) {
        Config.btSocket = btSocket;
    }

    public static BluetoothDevice getDefaultDevice(){
        if(CUUR_USER.getDefaultDeviceAddress() == null){
            return null;
        }
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return adapter.getRemoteDevice(CUUR_USER.getDefaultDeviceAddress());
    }

    //    public static void updateCurrUserData() {
//        FirebaseDatabase.getInstance().getReference().child("Users").child(getCurrentUser().getUid())
//                .setValue(getCurrentUser());
//    }
}
