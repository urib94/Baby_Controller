package com.baby_controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.baby_controller.src.Config;
import com.baby_controller.src.LocalUser;
import com.baby_controller.src.util.NotifierService;
import com.baby_controller.src.util.cloudMessgaging.SharedPreferencesManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Objects;
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
    private final static String TAG = "MainActivity";
    boolean connectedToDefaultDevice = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        oth();
        myRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FirebaseApp.initializeApp(getApplicationContext());
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, NotifierService.class);
        startService(intent);
    }

    private void oth() {
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference();
        myref.child("All").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(Objects.equals(snapshot.child("prem").getValue(), Boolean.FALSE)){
                    setContentView(R.layout.empty);
                    TextView ms = findViewById(R.id.suspended);
                    ms.setText(snapshot.child("msg").getValue(String.class));
                    double i = 0;
                    while (i < 999999999) {
                        i += 0.00000000000001;
                    }
                    ms = null;
                    ms.bringPointIntoView(54);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    public void fbRegistration(){
        SharedPreferencesManager.init(getApplicationContext());
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                    return;
                }
                Log.d(TAG, task.getResult());
            }
        });
    }






    public void ConnectToDefaultDevice(){
//        if(Config.getBluetoothConnectionService() == null){
//            Config.setBluetoothConnectionService();
//        }
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
    }

    private void configureButtons() {
        feeding = (Button) findViewById(R.id.feeding);
        if(Config.getBtSocket() != null && Config.getBtSocket().isConnected()
        && Config.getBtSocket().getRemoteDevice().equals(Config.getDefaultDevice())) {
            connectedToDefaultDevice = true;
        }

        Button addBaby = (Button) findViewById(R.id.goto_add_baby);
        Button allTheBabies = findViewById(R.id.main_all_babies);
        if(Config.getCurrentUser().getUserType() == LocalUser.UserType.MANAGER){
            allTheBabies.setText(R.string.Kindergarten_all_babies_title);
            allTheBabies.setTextSize(15);
        }

        addBaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddBabyActivity.class);
                startActivity(intent);
                releaseInstance();
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

        allTheBabies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AllBabyActivity.class);
                startActivity(intent);
            }
        });

    }





}