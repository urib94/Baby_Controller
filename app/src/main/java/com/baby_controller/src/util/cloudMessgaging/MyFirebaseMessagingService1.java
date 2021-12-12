//package com.baby_controller.src.util.cloudMessgaging;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.graphics.Color;
//import android.os.Build;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.NotificationCompat;
//
//import com.baby_controller.LoginActivity;
//import com.baby_controller.R;
//import com.baby_controller.src.Config;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.annotations.NotNull;
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//import com.pubnub.api.PNConfiguration;
//import com.pubnub.api.PubNub;
//import com.pubnub.api.callbacks.PNCallback;
//import com.pubnub.api.callbacks.SubscribeCallback;
//import com.pubnub.api.enums.PNPushType;
//import com.pubnub.api.models.consumer.PNPublishResult;
//import com.pubnub.api.models.consumer.PNStatus;
//import com.pubnub.api.models.consumer.message_actions.PNMessageAction;
//import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult;
//import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult;
//import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
//import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
//import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
//import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
//import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
//import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;
//import com.pubnub.api.models.consumer.push.PNPushAddChannelResult;
//import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult;
//import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult;
//import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult;
//import com.pubnub.api.models.consumer.push.payload.PushPayloadHelper;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//public class MyFirebaseMessagingService1 extends FirebaseMessagingService {
//    private String token;
//    private static final String TAG = "MyFirebaseMessaging";
//
//
//    @Override
//    public void onNewToken(String tok) {
//        super.onNewToken(token);
//        String oldToken = SharedPreferencesManager.readDeviceToken();
//        if (tok.equals(oldToken)) { return; }
//        SharedPreferencesManager.writeDeviceToken(token);
//
//        updatePushNotificationsOnChannels(new String[]{"notifications"}, token, oldToken);
//    }
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//
//        // If the application is in the foreground handle or display both data and notification FCM messages here.
//        // Here is where you can display your own notifications built from a received FCM message.
//        super.onMessageReceived(remoteMessage);
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//        }
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
////         displayNotification(remoteMessage.getNotification().getBody());
//    }
//
//    private void sendRegistrationToPubNub(String token/*, LocalUser user*/) {
//        // Configure PubNub push notifications.
//        LoginActivity.pubnub.addPushNotificationsOnChannels()
//                .pushType(PNPushType.FCM)
////                .channels(Collections.singletonList(user.getUid() + "_channel"))
//                .channels(Collections.singletonList("_channel"))
//
////                .deviceId(user.getRegistrationToken())
//                .deviceId(token)
//                .async(new PNCallback<PNPushAddChannelResult>() {
//                    @Override
//                    public void onResponse(PNPushAddChannelResult result, PNStatus status) {
//                        Log.d("PUBNUB", "-->PNStatus.getStatusCode = " + status.getStatusCode());
//                    }
//                });
//    }
//
//
//    public void getCurrToken(){
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        token = task.getResult();
//                        // Log and toast
//                        Log.d(TAG, token);
//                        Config.getCurrentUser().setRegistrationToken(token);
//                        Config.getCurrentUser().updateInDb();
//
//                    }
//                });
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel");
//                builder.setSmallIcon(R.drawable.ic_stat_ic_notification);
//                builder.setContentTitle("textTitle");
//                builder.setContentText("textContent");
//
//                builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
//        Notification notification = builder.build();
//
//
//    }
//
//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "getString(R.string.channel_name)";
//            String description = "getString(R.string.channel_description)";
//
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//    public void initPubnub() {
//        PNConfiguration pnConfiguration = new PNConfiguration();
//        pnConfiguration.setPublishKey("pub-c-8ab55658-b7b2-4ac8-b099-cda6fdacf791");
//        pnConfiguration.setSubscribeKey("sub-c-35c44032-5a46-11ec-a2d9-0639f9732331");
//        pnConfiguration.setSecure(true);
//        pnConfiguration.setUuid(Config.getCurrentUser().getUid());
//        LoginActivity.pubnub = new PubNub(pnConfiguration);
//    }
//
//
//    public void addPubnubListeners(){
//        LoginActivity.pubnub.addListener(new SubscribeCallback() {
//            // PubNub status
//            @Override
//            public void status(PubNub pubnub, PNStatus status) {
//                switch (status.getOperation()) {
//                    // combine unsubscribe and subscribe handling for ease of use
//                    case PNSubscribeOperation:
//                    case PNUnsubscribeOperation:
//                        // Note: subscribe statuses never have traditional errors,
//                        // just categories to represent different issues or successes
//                        // that occur as part of subscribe
//                        switch (status.getCategory()) {
//                            case PNConnectedCategory:
//                                // No error or issue whatsoever.
//                            case PNUnexpectedDisconnectCategory:
//                                // Usually an issue with the internet connection.
//                                // This is an error: handle appropriately.
//                                pubnub.reconnect();
//
//
//                        }
//
//                    case PNHeartbeatOperation:
//                        // Heartbeat operations can in fact have errors, so it's important to check first for an error.
//                        // For more information on how to configure heartbeat notifications through the status
//                        // PNObjectEventListener callback, refer to
//                        // /docs/sdks/java/android/api-reference/configuration#configuration_basic_usage
//                        if (status.isError()) {
//                            // There was an error with the heartbeat operation, handle here
//                        } else {
//                            // heartbeat operation was successful
//                        }
//                    default: {
//                        // Encountered unknown status type
//                    }
//                }
//            }
//
//            // Messages
//            @Override
//            public void message(PubNub pubnub, PNMessageResult message) {
//                String messagePublisher = message.getPublisher();
//                System.out.println("Message publisher: " + messagePublisher);
//                System.out.println("Message Payload: " + message.getMessage());
//                System.out.println("Message Subscription: " + message.getSubscription());
//                System.out.println("Message Channel: " + message.getChannel());
//                System.out.println("Message timetoken: " + message.getTimetoken());
//            }
//
//            // Presence
//            @Override
//            public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult presence) {
//                System.out.println("Presence Event: " + presence.getEvent());
//                // Can be join, leave, state-change or timeout
//
//                System.out.println("Presence Channel: " + presence.getChannel());
//                // The channel to which the message was published
//
//                System.out.println("Presence Occupancy: " + presence.getOccupancy());
//                // Number of users subscribed to the channel
//
//                System.out.println("Presence State: " + presence.getState());
//                // User state
//
//                System.out.println("Presence UUID: " + presence.getUuid());
//                // UUID to which this event is related
//
//                presence.getJoin();
//                // List of users that have joined the channel (if event is 'interval')
//
//                presence.getLeave();
//                // List of users that have left the channel (if event is 'interval')
//
//                presence.getTimeout();
//                // List of users that have timed-out off the channel (if event is 'interval')
//
//                presence.getHereNowRefresh();
//                // Indicates to the client that it should call 'hereNow()' to get the
//                // complete list of users present in the channel.
//            }
//
//            // Signals
//            @Override
//            public void signal(PubNub pubnub, PNSignalResult pnSignalResult) {
//            }
//
//            @Override
//            public void uuid(@org.jetbrains.annotations.NotNull PubNub pubnub, @org.jetbrains.annotations.NotNull PNUUIDMetadataResult pnUUIDMetadataResult) {
//
//            }
//
//            @Override
//            public void channel(@org.jetbrains.annotations.NotNull PubNub pubnub, @org.jetbrains.annotations.NotNull PNChannelMetadataResult pnChannelMetadataResult) {
//
//            }
//
//            @Override
//            public void membership(@org.jetbrains.annotations.NotNull PubNub pubnub, @org.jetbrains.annotations.NotNull PNMembershipResult pnMembershipResult) {
//
//            }
//
//            // Message actions
//            @Override
//            public void messageAction(PubNub pubnub, PNMessageActionResult pnActionResult) {
//                PNMessageAction pnMessageAction = pnActionResult.getMessageAction();
//                System.out.println("Message action type: " + pnMessageAction.getType());
//                System.out.println("Message action value: " + pnMessageAction.getValue());
//                System.out.println("Message action uuid: " + pnMessageAction.getUuid());
//                System.out.println("Message action actionTimetoken: " + pnMessageAction.getActionTimetoken());
//                System.out.println("Message action messageTimetoken: " + pnMessageAction.getMessageTimetoken());
//                System.out.println("Message action subscription: " + pnActionResult.getSubscription());
//                System.out.println("Message action channel: " + pnActionResult.getChannel());
//                System.out.println("Message action timetoken: " + pnActionResult.getTimetoken());
//            }
//
//            // files
//            @Override
//            public void file(PubNub pubnub, PNFileEventResult pnFileEventResult) {
//                System.out.println("File channel: " + pnFileEventResult.getChannel());
//                System.out.println("File publisher: " + pnFileEventResult.getPublisher());
//                System.out.println("File message: " + pnFileEventResult.getMessage());
//                System.out.println("File timetoken: " + pnFileEventResult.getTimetoken());
//                System.out.println("File file.id: " + pnFileEventResult.getFile().getId());
//                System.out.println("File file.name: " + pnFileEventResult.getFile().getName());
//                System.out.println("File file.url: " + pnFileEventResult.getFile().getUrl());
//            }
//        });
//
//    }
//
//
//    public void subscribe(String channel){
//        LoginActivity.pubnub.subscribe()
//                .channels(Arrays.asList(channel)) // subscribe to channels
//                .execute();
//    }
//
//    public void publish(String channel, String title, String body){
//        PushPayloadHelper pushPayloadHelper = new PushPayloadHelper();
//        PushPayloadHelper.FCMPayload fcmPayload = new PushPayloadHelper.FCMPayload();
//        PushPayloadHelper.FCMPayload.Notification fcmNotification =
//                new PushPayloadHelper.FCMPayload.Notification()
//                        .setTitle(title)
//                        .setBody(body)
//                        .setImage(String.valueOf(R.drawable.ic_stat_ic_notification));
//
//        fcmPayload.setNotification(fcmNotification);
//        pushPayloadHelper.setFcmPayload(fcmPayload);
//
//        Map<String, Object> commonPayload = new HashMap<>();
//        commonPayload.put("text", "John invited you to chat");
//        pushPayloadHelper.setCommonPayload(commonPayload);
//
//        Map<String, Object> pushPayload = pushPayloadHelper.build();
//
//        LoginActivity.pubnub.publish()
//                .message(Arrays.asList("hello", "there"))
//                .channel(channel)
//                .async(new PNCallback<PNPublishResult>() {
//                    @Override
//                    public void onResponse(PNPublishResult result, PNStatus status) {
//                        //todo: check if ther is a need to handle the response
//                    }
//                });
//    }
//
//    public void createChannel(Context context) {
//        //  Notification channel should only be created for devices running Android API level 26+.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//            NotificationChannel chan1 = new NotificationChannel(
//                    "default",
//                    "default",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//
//            chan1.setLightColor(Color.TRANSPARENT);
//            chan1.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
//            notificationManager.createNotificationChannel(chan1);
//        }
//    }
//
//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
//
//    public void updatePushNotificationsOnChannels(String[] channels, String deviceToken, String oldToken) {
//        if (oldToken != null) {
//            LoginActivity.pubnub.removeAllPushNotificationsFromDeviceWithPushToken()
//                    .pushType(PNPushType.FCM)
//                    .deviceId(oldToken)
//                    .async(new PNCallback<PNPushRemoveAllChannelsResult>() {
//                        @Override public void onResponse(PNPushRemoveAllChannelsResult result, PNStatus status) {
//                            // Handle Response
//                        }
//                    });
//        }
//
//        LoginActivity.pubnub.addPushNotificationsOnChannels()
//                .pushType(PNPushType.FCM)
//                .deviceId(deviceToken)
//                .channels(Arrays.asList(channels))
//                .async(new PNCallback<PNPushAddChannelResult>() {
//                    @Override public void onResponse(PNPushAddChannelResult result, PNStatus status) {
//                        // Handle Response
//                    }
//                });
//    }
//
//    public void registerNewChannels(String[] channels){
//        String cachedToken = SharedPreferencesManager.readDeviceToken();
//
//        LoginActivity.pubnub.addPushNotificationsOnChannels()
//                .pushType(PNPushType.FCM)
//                .deviceId(cachedToken)
//                .channels(Arrays.asList(channels))
//                .async(new PNCallback<PNPushAddChannelResult>() {
//                    @Override
//                    public void onResponse(PNPushAddChannelResult result, PNStatus status) {
//                        // Handle Response
//                    }
//                });
//    }
//
//    public void listRegisteredChannels(){
//        String cachedToken = SharedPreferencesManager.readDeviceToken();
//        LoginActivity.pubnub.auditPushChannelProvisions()
//                .pushType(PNPushType.FCM)
//                .deviceId(cachedToken)
//                .async(new PNCallback<PNPushListProvisionsResult>() {
//                    @Override
//                    public void onResponse(PNPushListProvisionsResult result, PNStatus status) {
//                        // handle response.
//                    }
//                });
//    }
//
//    public void removeExistingRegistrations(){
//        String cachedToken = SharedPreferencesManager.readDeviceToken();
//
//        LoginActivity.pubnub.removePushNotificationsFromChannels()
//                .pushType(PNPushType.FCM)
//                .deviceId(cachedToken)
//                .channels(Arrays.asList("ch1", "ch2", "ch3"))
//                .async(new PNCallback<PNPushRemoveChannelResult>() {
//                    @Override
//                    public void onResponse(PNPushRemoveChannelResult result, PNStatus status) {
//                        // handle response.
//                    }
//                });
//    }
//
//    public void publishPushNotification(String title, String body, String channel){
//        PushPayloadHelper pushPayloadHelper = new PushPayloadHelper();
//        PushPayloadHelper.FCMPayload fcmPayload = new PushPayloadHelper.FCMPayload();
//        PushPayloadHelper.FCMPayload.Notification fcmNotification =
//                new PushPayloadHelper.FCMPayload.Notification()
//                        .setTitle(title)
//                        .setBody(body);
//
//        fcmPayload.setNotification(fcmNotification);
//        pushPayloadHelper.setFcmPayload(fcmPayload);
//
//        Map<String, Object> commonPayload = new HashMap<>();
//        commonPayload.put("text", "John invited you to chat");
//        pushPayloadHelper.setCommonPayload(commonPayload);
//
//        Map<String, Object> pushPayload = pushPayloadHelper.build();
//
//        LoginActivity.pubnub.publish()
//                .channel(channel)
//                .message(pushPayload)
//                .async(new PNCallback<PNPublishResult>() {
//                    @Override
//                    public void onResponse(PNPublishResult result, PNStatus status) {
//                        // Handle Response
//                    }
//                });
//
//    }
//}
