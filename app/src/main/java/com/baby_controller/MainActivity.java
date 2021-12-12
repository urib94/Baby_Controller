package com.baby_controller;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.baby_controller.src.Config;
import com.baby_controller.src.LocalUser;
import com.baby_controller.src.util.cloudMessgaging.MyFirebaseMessagingService1;
import com.baby_controller.src.util.cloudMessgaging.SharedPreferencesManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.endpoints.pubsub.Publish;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult;
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult;
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult;
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult;
import com.pubnub.api.models.consumer.push.payload.PushPayloadHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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

    public static MyFirebaseMessagingService1 messagingService = new MyFirebaseMessagingService1();


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
//    ConnectToDefaultDevice();

}

    @Override
    protected void onRestart() {
        super.onRestart();
        setContentView(R.layout.activity_main);
        configureButtons();
//        ConnectToDefaultDevice();
        createChannel();

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

    public void updatePushNotificationsOnChannels(String[] channels, String deviceToken, String oldToken) {
        if (oldToken != null) {
            LoginActivity.pubnub.removeAllPushNotificationsFromDeviceWithPushToken()
                    .pushType(PNPushType.FCM)
                    .deviceId(oldToken)
                    .async(new PNCallback<PNPushRemoveAllChannelsResult>() {
                        @Override public void onResponse(PNPushRemoveAllChannelsResult result, PNStatus status) {
                            // Handle Response
                        }
                    });
        }

        LoginActivity.pubnub.addPushNotificationsOnChannels()
                .pushType(PNPushType.FCM)
                .deviceId(deviceToken)
                .channels(Arrays.asList(channels))
                .async(new PNCallback<PNPushAddChannelResult>() {
                    @Override public void onResponse(PNPushAddChannelResult result, PNStatus status) {
                        // Handle Response
                    }
                });
    }

    public void registerNewChannels(String[] channels){
        String cachedToken = SharedPreferencesManager.readDeviceToken();

        LoginActivity.pubnub.addPushNotificationsOnChannels()
                .pushType(PNPushType.FCM)
                .deviceId(cachedToken)
                .channels(Arrays.asList(channels))
                .async(new PNCallback<PNPushAddChannelResult>() {
                    @Override
                    public void onResponse(PNPushAddChannelResult result, PNStatus status) {
                        // Handle Response
                    }
                });
    }

    public void listRegisteredChannels(){
        String cachedToken = SharedPreferencesManager.readDeviceToken();
        LoginActivity.pubnub.auditPushChannelProvisions()
                .pushType(PNPushType.FCM)
                .deviceId(cachedToken)
                .async(new PNCallback<PNPushListProvisionsResult>() {
                    @Override
                    public void onResponse(PNPushListProvisionsResult result, PNStatus status) {
                        // handle response.
                    }
                });
    }

    public void removeExistingRegistrations(){
        String cachedToken = SharedPreferencesManager.readDeviceToken();

        LoginActivity.pubnub.removePushNotificationsFromChannels()
                .pushType(PNPushType.FCM)
                .deviceId(cachedToken)
                .channels(Arrays.asList("ch1", "ch2", "ch3"))
                .async(new PNCallback<PNPushRemoveChannelResult>() {
                    @Override
                    public void onResponse(PNPushRemoveChannelResult result, PNStatus status) {
                        // handle response.
                    }
                });
    }

    public void publishPushNotification(String title, String body, String channel){
        PushPayloadHelper pushPayloadHelper = new PushPayloadHelper();
        PushPayloadHelper.FCMPayload fcmPayload = new PushPayloadHelper.FCMPayload();
        PushPayloadHelper.FCMPayload.Notification fcmNotification =
                new PushPayloadHelper.FCMPayload.Notification()
                        .setTitle(title)
                        .setBody(body);

        fcmPayload.setNotification(fcmNotification);
        pushPayloadHelper.setFcmPayload(fcmPayload);

        Map<String, Object> commonPayload = new HashMap<>();
        commonPayload.put("text", "John invited you to chat");
        pushPayloadHelper.setCommonPayload(commonPayload);

        Map<String, Object> pushPayload = pushPayloadHelper.build();

        Publish publish = LoginActivity.pubnub.publish();
                publish.channel(channel);
                publish.message(pushPayload);

                publish.async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        // Handle Response
                    }
                });

    }

    public void publish(String channel, String title, String body){
        PushPayloadHelper pushPayloadHelper = new PushPayloadHelper();
        PushPayloadHelper.FCMPayload fcmPayload = new PushPayloadHelper.FCMPayload();
        PushPayloadHelper.FCMPayload.Notification fcmNotification =
                new PushPayloadHelper.FCMPayload.Notification()
                        .setTitle(title)
                        .setBody(body)
                        .setImage(String.valueOf(R.drawable.ic_stat_ic_notification));

        fcmPayload.setNotification(fcmNotification);
        pushPayloadHelper.setFcmPayload(fcmPayload);

        Map<String, Object> commonPayload = new HashMap<>();
        commonPayload.put("text", "John invited you to chat");
        pushPayloadHelper.setCommonPayload(commonPayload);

        Map<String, Object> pushPayload = pushPayloadHelper.build();

        LoginActivity.pubnub.publish()
                .message(fcmPayload)
                .channel(channel)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        //todo: check if ther is a need to handle the response
                    }
                });
    }


    public void createChannel() {
        //  Notification channel should only be created for devices running Android API level 26+.
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel chan1 = new NotificationChannel(
                "Notifications",
                "Notifications",
                NotificationManager.IMPORTANCE_DEFAULT);

        chan1.setLightColor(Color.TRANSPARENT);
        chan1.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
        notificationManager.createNotificationChannel(chan1);
    }

    public void initPubnub() {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey("pub-c-8ab55658-b7b2-4ac8-b099-cda6fdacf791");
        pnConfiguration.setSubscribeKey("sub-c-35c44032-5a46-11ec-a2d9-0639f9732331");
        pnConfiguration.setSecure(true);

        LoginActivity.pubnub = new PubNub(pnConfiguration);
    }
    private void sendRegistrationToPubNub(String token/*, LocalUser user*/) {
        System.out.println("new token received");

        LoginActivity.pubnub.addPushNotificationsOnChannels()
                .pushType(PNPushType.FCM)
                .channels(Arrays.asList("Notifications"))
                .deviceId(token)
                .async(new PNCallback<PNPushAddChannelResult>() {
                    @Override
                    public void onResponse(PNPushAddChannelResult result, PNStatus status) {
                        Log.d("PUBNUB", "-->PNStatus.getStatusCode = " + status.getStatusCode());
                    }
                });
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


    }



}