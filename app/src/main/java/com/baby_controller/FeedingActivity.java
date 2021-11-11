package com.baby_controller;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.baby_controller.src.Baby;
import com.baby_controller.src.BabyListAdapter;
import com.baby_controller.src.Config;
import com.baby_controller.src.DeviceListAdapter;
import com.baby_controller.src.Institution;
import com.baby_controller.src.LocalUser;
import com.baby_controller.src.Parent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class FeedingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "Feeding Activity";

    public static double foodWight = 0;
//    BluetoothAdapter mBluetoothAdapter;
    Button btnDiscovere;

//    BluetoothConnectionService mBluetoothConnection;

    private Button feed;
    public static Baby babyToFeed;
    //todo check if its making problems
    private TextView measuredWight;

    Button btnOnOFf;

    Button btPairedDevices;

    CheckBox chooseDefault;





    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    BluetoothDevice mBTDevice;

    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();

    public DeviceListAdapter mDeviceListAdapter;

    public DeviceListAdapter mPairedDeviceListAdapter;
    ListView lvNewDevices;


    public ArrayList<Baby> mHungryBabies = new ArrayList<>();
    public ArrayList<Baby> mReallyHungryBabies = new ArrayList<>();

    public BabyListAdapter mReallyHungryBabyListAdapter;
    public BabyListAdapter mHungryBabyListAdapter;

    private Set<BluetoothDevice> mPairedDevices;


    // baby chooser buttons
    ListView reallyHungryBabies;
    ListView hungryBabies;


    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    /**
     * Broadcast Receiver for changes made to bluetooth states such as:
     * 1) Discoverability mode on/off or expire.
     */
    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadcastReceiver2: Connected.");
                        startFeeding();
                        break;
                }

            }
        }
    };

    //redundant
    private void startFeeding() {
        setContentView(R.layout.choos_baby_to_feed);
        configureBabyChooserButtons();
        babyListsMaker();
    }

    //copied
    private void configureBabyChooserButtons() {
        reallyHungryBabies = (ListView) findViewById(R.id.really_hungry_list);
        hungryBabies = (ListView) findViewById(R.id.hungry_list);

        reallyHungryBabies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                babyToFeed = mReallyHungryBabies.get(position);
                Intent intent  = new Intent(FeedingActivity.this,AdministerFoodActivity.class);
                startActivity(intent);
            }
        });

        hungryBabies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                babyToFeed = mHungryBabies.get(position);
                Intent intent  = new Intent(FeedingActivity.this,AdministerFoodActivity.class);
                startActivity(intent);
            }
        });

    }



    /**
     * Broadcast Receiver for listing devices that are not yet paired
     * -Executed by btnDiscover() method.
     */
    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                lvNewDevices.setAdapter(mDeviceListAdapter);
            }
        }
    };

    //copied
    public void babyListsMaker(){
        //get the institute of the user from the database
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Institutions")
                .child(Config.getCurrentUser().getInstitutionName());

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Institution institution = dataSnapshot.getValue(Institution.class);
                if(institution != null){
                    for(LocalUser parent : institution.getParents()){
                        mHungryBabies.addAll(((Parent)parent).getBabiesNeedToFeed());
                    }
                    mReallyHungryBabyListAdapter = new BabyListAdapter(FeedingActivity.this,R.layout.baby_adapter_view, mHungryBabies);
                    reallyHungryBabies.setAdapter(mReallyHungryBabyListAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //the same for the babies that are not hungry
        DatabaseReference mDatabaseReference2 = FirebaseDatabase.getInstance().getReference().child("Institutions")
                .child(Config.getCurrentUser().getInstitutionName());

        mDatabaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Institution institution = dataSnapshot.getValue(Institution.class);
                if(institution != null){
                    for(LocalUser parent : institution.getParents()){
                        mHungryBabies.addAll(((Parent)parent).getBabiesNotNeedToFeed());
                    }
                    mHungryBabyListAdapter = new BabyListAdapter(FeedingActivity.this,R.layout.baby_adapter_view, mHungryBabies);
                    hungryBabies.setAdapter(mReallyHungryBabyListAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    

    /**
     * Broadcast Receiver that detects bond state changes (Pairing status changes)
     */
    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case1: bonded already
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
                    //inside BroadcastReceiver4
                    mBTDevice = mDevice;
                }
                //case2: creating a bone
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");
                }
                //case3: breaking a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "BroadcastReceiver: BOND_NONE.");
                }
            }
        }
    };



    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called.");
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);
        unregisterReceiver(mBroadcastReceiver2);
        unregisterReceiver(mBroadcastReceiver3);
        unregisterReceiver(mBroadcastReceiver4);
        //mBluetoothAdapter.cancelDiscovery();
    }

    public void configureBtButtons(){
        btnOnOFf = (Button) findViewById(R.id.off);
        btnDiscovere = (Button) findViewById(R.id.discover);
        lvNewDevices = (ListView) findViewById(R.id.devicesListView);
        btPairedDevices = (Button) findViewById(R.id.paired_btn);
        chooseDefault = (CheckBox) findViewById(R.id.choose_defult);

        btnDiscovere.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(!Config.getmBluetoothAdapter().isEnabled()){
                    enableDisableBT();
                }
                btnDiscover(v);
            }
        });

        btnOnOFf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: enabling/disabling bluetooth.");
                enableDisableBT();
                if(Config.getmBluetoothAdapter().isEnabled()){
                    btnOnOFf.setText(R.string.bluetooth_off);
                }else btnOnOFf.setText(R.string.bluetooth_on);
            }
        });

        btPairedDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPairedDevices(v);
            }
        });




        lvNewDevices.setOnItemClickListener(FeedingActivity.this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //todo change the content view to bluetooth connection (and make one)
        if(Config.getCurrentUser().getDefaultDevice() == null) {
            setContentView(R.layout.bluetooth_activity);
            configureBtButtons();
        }else {
            setContentView(R.layout.choos_baby_to_feed);
            startBTConnection(Config.getCurrentUser().getDefaultDevice(),MY_UUID_INSECURE);
            setContentView(R.layout.choos_baby_to_feed);
            configureBabyChooserButtons();
            startFeeding();
        }


        //Broadcasts when bond state changes (ie:pairing)
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, filter);

        Config.setmBluetoothAdapter(BluetoothAdapter.getDefaultAdapter());


    }

    //create method for starting connection
//***remember the conncction will fail and app will crash if you haven't paired first
    public void startConnection(){
        startBTConnection(mBTDevice,MY_UUID_INSECURE);
    }

    /**
     * starting chat service method
     */
    public void startBTConnection(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");
        if(chooseDefault.isChecked() && Config.getCurrentUser().getDefaultDevice() == null){
            Config.getCurrentUser().setDefaultDevice(device);
            FirebaseDatabase.getInstance().getReference().child("Users").child(Config.getCurrentUser().getUid())
                    .child("defaultDevice").setValue(device);
        }
//        Config.getBluetoothConnectionManger().startClient(device,uuid);
    }

    private void listPairedDevices(View view){
        mPairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        if(BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            // put it's one to the adapter
            for (BluetoothDevice device : mPairedDevices) {
                mPairedDeviceListAdapter.add(device);
            }

            lvNewDevices.setAdapter(mPairedDeviceListAdapter);
        }
        else
            toastMessage("Bluetooth is off");
    }


    public void enableDisableBT(){

        if(BluetoothAdapter.getDefaultAdapter() == null){
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");
        }
        if(!BluetoothAdapter.getDefaultAdapter().isEnabled()){
            Log.d(TAG, "enableDisableBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if(BluetoothAdapter.getDefaultAdapter().isEnabled()){
            Log.d(TAG, "enableDisableBT: disabling BT.");
            BluetoothAdapter.getDefaultAdapter().disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }

    }

    private void toastMessage(String s) {
        Toast.makeText(FeedingActivity.this ,s,Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void btnDiscover(View view) {
        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");

        if(BluetoothAdapter.getDefaultAdapter().isDiscovering()){
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling discovery.");
            btnDiscovere.setText(R.string.discover_new_devices);

            //check BT permissions in manifest
            checkBTPermissions();

            BluetoothAdapter.getDefaultAdapter().startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }else {
            btnDiscovere.setText(R.string.stop_discovering);
        }
        if(!BluetoothAdapter.getDefaultAdapter().isDiscovering()){

            //check BT permissions in manifest
            checkBTPermissions();
            BluetoothAdapter.getDefaultAdapter().startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
    }

    /**
     * This method is required for all devices running API23+
     * Android must programmatically check the permissions for bluetooth. Putting the proper permissions
     * in the manifest is not enough.
     *
     * NOTE: This will only execute on versions > LOLLIPOP because it is not needed otherwise.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(view.equals(lvNewDevices)) {
            //first cancel discovery because its very memory intensive.
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

            Log.d(TAG, "onItemClick: You Clicked on a device.");
            String deviceName = mBTDevices.get(i).getName();
            String deviceAddress = mBTDevices.get(i).getAddress();


            Log.d(TAG, "onItemClick: deviceName = " + deviceName);
            Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);

            //create the bond.
            //NOTE: Requires API 17+? I think this is JellyBean
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                Log.d(TAG, "Trying to pair with " + deviceName);
                mBTDevices.get(i).createBond();
                mBTDevice = mBTDevices.get(i);
//                mBluetoothConnection = new BluetoothConnectionService(FeedingActivity.this);
                startConnection();
            }
        }else if (view.equals(reallyHungryBabies)){
            babyToFeed = mReallyHungryBabies.get(i);
            giveFood();
        }else {
            babyToFeed = mHungryBabies.get(i);
            giveFood();
        }
    }

    private void giveFood() {
        setContentView(R.layout.administer_food_activity);

    }

    public static synchronized void setFoodWight(double newFoodWight){
        foodWight = newFoodWight;
    }

    public static synchronized double getFoodWight(){
        return foodWight;
    }

}
