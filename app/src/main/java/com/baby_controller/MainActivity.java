package com.baby_controller;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baby_controller.src.Config;
import com.baby_controller.src.LocalUser;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    public static LocalUser currLocalUser;
    public FirebaseAuth mAuth;
    private Button btMenu;
    private Button feeding;
    private Button blogout;
    private Context context;
    DatabaseReference myRef;
    private ArrayList<LocalUser> localUsers = new ArrayList<>();
    private String uid;
    boolean showwwww = false;
    private final static String TAG = "MainActivity";
    boolean connectedToDefaultDevice = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FirebaseApp.initializeApp(getApplicationContext());
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
    }

@Override
protected void onStart() {
    super.onStart();
    configureButtons();
    mAuth = FirebaseAuth.getInstance();
    uid = mAuth.getUid();
    ConnectToDefaultDevice();

}

    @Override
    protected void onRestart() {
        super.onRestart();
        setContentView(R.layout.activity_main);
        configureButtons();
        ConnectToDefaultDevice();
    }


    public void ConnectToDefaultDevice(){

        if(Config.getCurrentUser() != null) {
            System.out.println("cur user in main activity" + Config.getCurrentUser().toString());
            if (Config.getDefaultDevice() != null) {
                try {
                    Config.getBluetoothConnectionService().startClient(Config.getDefaultDevice(),
                            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66"));
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //mAuth.signOut();
    }

    private void configureButtons() {
        feeding = (Button) findViewById(R.id.feeding);
        if(Config.getBtSocket() != null && Config.getBtSocket().isConnected()
        && Config.getBtSocket().getRemoteDevice().equals(Config.getDefaultDevice())) {
            connectedToDefaultDevice = true;
        }

        Button addBaby = (Button) findViewById(R.id.goto_add_baby);

        addBaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddBabyActivity.class);
                startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    releaseInstance();
                }else {
                    finish();
                }
            }
        });

        btMenu = (Button) findViewById(R.id.bt_menu);

        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BluetoothConnectionManager.class);
                startActivity(intent);
            }
        });

        feeding.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(connectedToDefaultDevice) {
                    Intent intent = new Intent(MainActivity.this, FeedingActivity2.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this,getString
                            (R.string.user_need_to_connect_to_bt),Toast.LENGTH_SHORT).show();
                }
            }
        });
        feeding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FeedingActivity2.class);
                startActivity(intent);
            }
        });

        blogout = (Button) findViewById(R.id.logout);
        blogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                FirebaseUser us = mAuth.getCurrentUser();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }
        });


    }


}