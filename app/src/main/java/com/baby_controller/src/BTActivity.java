//package com.baby_controller.src;
//
//import android.Manifest;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.baby_controller.R;
//import com.baby_controller.src.util.BluetoothConnectionService;
//
//import java.util.ArrayList;
//import java.util.Set;
//import java.util.UUID;
//
//public class BTActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
//    private static final String TAG = "Bluetooth Activity";
//    BluetoothAdapter mBluetoothAdapter;
//    public Button btnEnableDisable_Discoverable;
//
//    BluetoothConnectionService mBluetoothConnection;
//
//    Button btnStartConnection;
//    Button btnSend;
//    Button btnONOFF;
//    private Button mListPairedDevicesBtn;
//
//
//    private Set<BluetoothDevice> mPairedDevices;
//
//    EditText etSend;
//
//    private static final UUID MY_UUID_INSECURE =
//            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
//
//    public BluetoothDevice mBTDevice;
//
//    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
//
//    public DeviceListAdapter mDeviceListAdapter;
//
//
//
//    ListView lvNewDevices;
//
//    private CheckBox chooseDefault;
//
//    @Override
//    protected void onDestroy() {
//        Log.d(TAG, "onDestroy: called.");
//        super.onDestroy();
//        unregisterReceiver(mBroadcastReceiver1);
//        unregisterReceiver(mBroadcastReceiver2);
//        unregisterReceiver(mBroadcastReceiver3);
//        unregisterReceiver(mBroadcastReceiver4);
//        //mBluetoothAdapter.cancelDiscovery();
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.bluetooth_activity);
//        btnONOFF = (Button) findViewById(R.id.off);
//        mListPairedDevicesBtn = (Button)findViewById(R.id.paired_btn);
//        btnEnableDisable_Discoverable = (Button) findViewById(R.id.discover);
//        lvNewDevices = (ListView) findViewById(R.id.devicesListView);
//        mBTDevices = new ArrayList<>();
//        mDeviceListAdapter = new DeviceListAdapter(BTActivity.this, R.layout.device_adapter_view, mBTDevices);
//        chooseDefault = (CheckBox)findViewById(R.id.choose_defult);
//
//
//        //Broadcasts when bond state changes (ie:pairing)
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//
//        registerReceiver(mBroadcastReceiver4, filter);
//
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        lvNewDevices.setOnItemClickListener(BTActivity.this);
//
//        mListPairedDevicesBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                listPairedDevices(v);
//            }
//        });
//
//        btnONOFF.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: enabling/disabling bluetooth.");
//                enableDisableBT();
//                if(mBluetoothAdapter.isEnabled()){
//                    btnONOFF.setText(R.string.bluetooth_off);
//                }else {
//                    btnONOFF.setText(R.string.bluetooth_on);
//                }
//            }
//        });
//
//        btnEnableDisable_Discoverable.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btnDiscover(v);
//                System.out.println("click on discover");
//            }
//        });
//
//    }
//
//
//    // Create a BroadcastReceiver for ACTION_FOUND
//    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            // When discovery finds a device
//            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
//                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);
//
//                switch(state){
//                    case BluetoothAdapter.STATE_OFF:
//                        Log.d(TAG, "onReceive: STATE OFF");
//                        break;
//                    case BluetoothAdapter.STATE_TURNING_OFF:
//                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
//                        break;
//                    case BluetoothAdapter.STATE_ON:
//                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
//                        break;
//                    case BluetoothAdapter.STATE_TURNING_ON:
//                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
//                        break;
//                }
//            }
//        }
//    };
//
//    /**
//     * Broadcast Receiver for changes made to bluetooth states such as:
//     * 1) Discoverability mode on/off or expire.
//     */
//    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            final String action = intent.getAction();
//
//            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
//
//                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);
//
//                switch (mode) {
//                    //Device is in Discoverable Mode
//                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
//                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Enabled.");
//                        break;
//                    //Device not in discoverable mode
//                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
//                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
//                        break;
//                    case BluetoothAdapter.SCAN_MODE_NONE:
//                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
//                        break;
//                    case BluetoothAdapter.STATE_CONNECTING:
//                        Log.d(TAG, "mBroadcastReceiver2: Connecting....");
//                        break;
//                    case BluetoothAdapter.STATE_CONNECTED:
//                        Log.d(TAG, "mBroadcastReceiver2: Connected.");
//                        finish();
//                        break;
//                }
//
//            }
//        }
//    };
//
//
//
//
//    /**
//     * Broadcast Receiver for listing devices that are not yet paired
//     * -Executed by btnDiscover() method.
//     */
//    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            mDeviceListAdapter.clear();
//            mDeviceListAdapter.notifyDataSetChanged();
//            final String action = intent.getAction();
//            Log.d(TAG, "onReceive: ACTION FOUND.");
//
//            if (action.equals(BluetoothDevice.ACTION_FOUND)){
//                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
//                mBTDevices.add(device);
//                System.out.println("mBTDevices = " + mBTDevices.toString());
//                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
//                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
//                mDeviceListAdapter.notifyDataSetChanged();
//                lvNewDevices.setAdapter(mDeviceListAdapter);
//            }
//        }
//    };
//
//    /**
//     * Broadcast Receiver that detects bond state changes (Pairing status changes)
//     */
//    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            final String action = intent.getAction();
//
//            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
//                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                //3 cases:
//                //case1: bonded already
//                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
//                    Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
//                    //inside BroadcastReceiver4
//                    mBTDevice = mDevice;
//
//                }
//                //case2: creating a bone
//                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
//                    Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");
//                }
//                //case3: breaking a bond
//                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
//                    Log.d(TAG, "BroadcastReceiver: BOND_NONE.");
//                }
//            }
//        }
//    };
//
//
//
//
//
//    //create method for starting connection
////***remember the conncction will fail and app will crash if you haven't paired first
//    public void startConnection(){
//        startBTConnection(mBTDevice,MY_UUID_INSECURE);
//    }
//
//    /**
//     * starting chat service method
//     */
//    public void startBTConnection(BluetoothDevice device, UUID uuid){
//        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");
//
//        mBluetoothConnection.startClient(device,uuid);
//    }
//
//
//
//    public void enableDisableBT(){
//        if(mBluetoothAdapter == null){
//            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");
//        }
//        if(!mBluetoothAdapter.isEnabled()){
//            Log.d(TAG, "enableDisableBT: enabling BT.");
//            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivity(enableBTIntent);
//
//            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
//            registerReceiver(mBroadcastReceiver1, BTIntent);
//        }
//        if(mBluetoothAdapter.isEnabled()){
//            Log.d(TAG, "enableDisableBT: disabling BT.");
//            mBluetoothAdapter.disable();
//
//            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
//            registerReceiver(mBroadcastReceiver1, BTIntent);
//        }
//
//    }
//
//
//    public void btnEnableDisable_Discoverable(View view) {
//        Log.d(TAG, "btnEnableDisable_Discoverable: Making device discoverable for 300 seconds.");
//
//        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
//        startActivity(discoverableIntent);
//
//        IntentFilter intentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
//        registerReceiver(mBroadcastReceiver2,intentFilter);
//
//    }
//
//    public void btnDiscover(View view) {
//        mDeviceListAdapter.clear();
//        mDeviceListAdapter.notifyDataSetChanged();
//        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");
//        System.out.println("mBluetoothAdapter is discovering ?" + mBluetoothAdapter.isDiscovering());
//        if(mBluetoothAdapter.isDiscovering()){
//            mBluetoothAdapter.cancelDiscovery();
//            Log.d(TAG, "btnDiscover: Canceling discovery.");
//            btnEnableDisable_Discoverable.setText(R.string.discover_new_devices);
//        }
//        if(!mBluetoothAdapter.isDiscovering()){
//            btnEnableDisable_Discoverable.setText(R.string.stop_discovering);
//            //check BT permissions in manifest
//            checkBTPermissions();
//            Log.d(TAG, "btnDiscover: started discovery.");
//
//            if(mBluetoothAdapter.startDiscovery()){
//                System.out.println("discovery error");
//            }
//            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
//        }
//    }
//
//    /**
//     * This method is required for all devices running API23+
//     * Android must programmatically check the permissions for bluetooth. Putting the proper permissions
//     * in the manifest is not enough.
//     *
//     * NOTE: This will only execute on versions > LOLLIPOP because it is not needed otherwise.
//     */
//    private void checkBTPermissions() {
//        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
//            int permissionCheck = 0;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
//            }
//            if (permissionCheck != 0) {
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
//                }
//            }
//        }else{
//            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
//        }
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        //first cancel discovery because its very memory intensive.
//        mBluetoothAdapter.cancelDiscovery();
//
//        Log.d(TAG, "onItemClick: You Clicked on a device.");
//        String deviceName = mBTDevices.get(i).getName();
//        String deviceAddress = mBTDevices.get(i).getAddress();
//
//        Log.d(TAG, "onItemClick: deviceName = " + deviceName);
//        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);
//
//        //create the bond.
//        //NOTE: Requires API 17+? I think this is JellyBean
//        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
//            Log.d(TAG, "Trying to pair with " + deviceName);
//            mBTDevices.get(i).createBond();
//
//            mBTDevice = mBTDevices.get(i);
//            mBluetoothConnection = new BluetoothConnectionService(BTActivity.this);
//            startConnection();
//            if(chooseDefault.isChecked()) {
//                Config.getCurrentUser().setDefaultDeviceAddress(mBTDevice.getAddress());
//            }
//        }
//    }
//
//    private void listPairedDevices(View view){
//        mPairedDevices = mBluetoothAdapter.getBondedDevices();
//        System.out.println("mBluetoothAdapter = " + mBluetoothAdapter.toString());
//        if(mBluetoothAdapter.isEnabled()) {
//            // put it's one to the adapter
//            for (BluetoothDevice device : mPairedDevices) {
//                if(mDeviceListAdapter.getPosition(device) == -1) {
//                    mDeviceListAdapter.add(device);
//                }
//            }
//            mDeviceListAdapter.notifyDataSetChanged();
//            lvNewDevices.setAdapter(mDeviceListAdapter);
//            Toast.makeText(getApplicationContext(), "Show Paired Devices", Toast.LENGTH_SHORT).show();
//        }
//        else
//            Toast.makeText(getApplicationContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
//    }
//}