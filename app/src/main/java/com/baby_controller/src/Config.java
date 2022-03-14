package com.baby_controller.src;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.baby_controller.src.util.BluetoothConnectionService;
import com.google.firebase.database.DatabaseReference;

import java.sql.Time;

public class Config {
    public static final int NUM_OF_BYTES = 4;
    public static final String BT_NAME = "baby-controller";

    private static LocalUser CUUR_USER = null;

    private static Institution CURR_INST;

    private static String DEV_ADD = null;

    private static Parent CURR_PARENT;

    public static DatabaseReference CurrUserRef = null;

    public static double FOOD = 0;

    public static final long TIME_BETWEEN_MEALS = 90000 * 60;

    public static final long DEFAULT_BREAKFAST_TIME = new Time(8,0,0).getTime();

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

        if(CURR_PARENT != null){
            return CURR_PARENT;
        }
        return CUUR_USER;
    }

    public static synchronized void setCurrentUser(LocalUser user) {

        if(user == null){
            System.out.println("attempt to assigned null user");
        }
        if(user instanceof Parent ){
            CURR_PARENT = (Parent) user;
        }else CUUR_USER = new LocalUser(user);
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
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(CURR_PARENT != null){
            if(CURR_PARENT.getDefaultDeviceAddress() == null){
                return null;
            }
            return adapter.getRemoteDevice(CURR_PARENT.getDefaultDeviceAddress());
        }
        if(CUUR_USER.getDefaultDeviceAddress() == null){
            return null;
        }

        return adapter.getRemoteDevice(CUUR_USER.getDefaultDeviceAddress());
    }

    public static synchronized Institution getCurrInst() {
        return CURR_INST;
    }

    public static synchronized void setCurrInst(Institution currInst) {
        CURR_INST = currInst;
    }

    public static void setBaseAddress(String deviceAddress) {
        DEV_ADD = deviceAddress;
        System.out.println("add is " + deviceAddress);
        if(CUUR_USER != null) {
            CUUR_USER.setDefaultDeviceAddress(deviceAddress);
        }else if(CURR_PARENT != null){
            CURR_PARENT.setDefaultDeviceAddress(deviceAddress);
        }
    }

    public static String getDevAdd() {
        return DEV_ADD;
    }

    public static void setDevAdd(String devAdd) {
        DEV_ADD = devAdd;
    }

    public static void setBaseName(String deviceName) {
        if(CUUR_USER != null) {
            CUUR_USER.setBaseName(deviceName);
        }else if(CURR_PARENT != null){
            CURR_PARENT.setBaseName(deviceName);
        }
    }

    public static DatabaseReference getCurrUserRef() {
        return CurrUserRef;
    }

    public static void setCurrUserRef(DatabaseReference currUserRef) {
        CurrUserRef = currUserRef;
    }


//    public static void updateCurrUserData() {
//        FirebaseDatabase.getInstance().getReference().child("Users").child(getCurrentUser().getUid())
//                .setValue(getCurrentUser());
//    }

    public static Parent getCurrParent() {
        return CURR_PARENT;
    }
}
