//package com.baby_controller.src.util;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.NotificationCompat;
//
//import com.baby_controller.R;
//import com.google.firebase.appindexing.builders.MessageBuilder;
//import com.google.firebase.messaging.ApnsConfig;
//import com.google.firebase.messaging.Aps;
//import com.google.firebase.messaging.Notification;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Scanner;
//
//
//public class NotificationManegerActivity extends AppCompatActivity{
//    private static final String AUTH_KEY = "AIzaSyAE36Q4qX84XsMwf8-5g7tY1_WWxWwncT8";
//    private TextView mTextView;
//    private String token;
//    public Context[] context = {this};
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        mTextView = findViewById(R.id.back);
//
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            String tmp = "";
//            for (String key : bundle.keySet()) {
//                Object value = bundle.get(key);
//                tmp += key + ": " + value + "\n\n";
//            }
//            mTextView.setText(tmp);
//        }
//
////        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener() {
////            @Override
////            public void onComplete(@NonNull Task task) {
////                if (!task.isSuccessful()) {
////                    token = Objects.requireNonNull(task.getException()).getMessage();
////                    Log.w("FCM TOKEN Failed", task.getException());
////                } else {
////                    token = task.getResult().toString();
////                    Log.i("FCM TOKEN", token);
////                }
////            }
////        });
//    }
//
//    public void showToken(View view) {
//        mTextView.setText(token);
//    }
//
////        public void subscribe(View view) {
////            FirebaseMessaging.getInstance().subscribeToTopic("news");
////            mTextView.setText(R.string.subscribed);
////        }
//
////        public void unsubscribe(View view) {
////            FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
////            mTextView.setText(R.string.unsubscribed);
////        }
//
////        public void sendToken(View view) {
////            sendWithOtherThread("token");
////        }
////
////        public void sendTokens(View view) {
////            sendWithOtherThread("tokens");
////        }
////
////        public void sendTopic(View view) {
////            sendWithOtherThread("topic");
////        }
//    /*
//        param: msg[0] - title, msg[1] - body, msg[3] - click_action
//     */
//
//
//    private void sendWithOtherThread(final String type, String target, String[] msg) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                pushNotification(type, target, msg);
//            }
//        }).start();
//    }
//
//    /*
//        param: msg[0] - title, msg[1] - body, msg[3] - click_action
//     */
//    private void pushNotification(String type, String target, String[] msg ) {
//        JSONObject jPayload = new JSONObject();
//        JSONObject jNotification = new JSONObject();
//        JSONObject jData = new JSONObject();
//        try {
//            jNotification.put("title", msg[0]);
//            jNotification.put("body", msg[1]);
//            jNotification.put("sound", "default");
//            jNotification.put("badge", "1");
//            jNotification.put("click_action", msg[3]);
//            jNotification.put("icon", "ic_stat_ic_notification");
//
//            jData.put("picture", "https://miro.medium.com/max/1400/1*QyVPcBbT_jENl8TGblk52w.png");
//
//            switch(type) {
//                case "tokens":
//                    JSONArray ja = new JSONArray();
//                    ja.put(target);
//                    ja.put(token);
//                    jPayload.put("registration_ids", ja);
//                    break;
//                case "topic":
//                    jPayload.put("to", "/topics/" + target);
//                    break;
//                case "condition":
//                    jPayload.put("condition", "'sport' in topics || 'news' in topics");
//                    break;
//                default:
//                    jPayload.put("to", token);
//            }
//
//            jPayload.put("priority", "high");
//            jPayload.put("notification", jNotification);
//            jPayload.put("data", jData);
//
//            URL url = new URL("https://fcm.googleapis.com/fcm/send");
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Authorization", AUTH_KEY);
//            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setDoOutput(true);
//
//            // Send FCM message content.
//            OutputStream outputStream = conn.getOutputStream();
//            outputStream.write(jPayload.toString().getBytes());
//
//            // Read FCM response.
//            InputStream inputStream = conn.getInputStream();
//            final String resp = convertStreamToString(inputStream);
//
//            Handler h = new Handler(Looper.getMainLooper());
//            h.post(new Runnable() {
//                @Override
//                public void run() {
//                    mTextView.setText(resp);
//                }
//            });
//        } catch (JSONException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public static void sendWithOtherThread(final String type, String target, String[] msg, String tok, Context context) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                pushNotification(type, target, msg, tok, context);
//            }
//        }).start();
//    }
//
//    private static void pushNotification(String type, String target, String[] msg, String tok, Context context) {
//        JSONObject jPayload = new JSONObject();
//        JSONObject jNotification = new JSONObject();
//        JSONObject jData = new JSONObject();
//        try {
//            jNotification.put("title", msg[0]);
//            jNotification.put("body", msg[1]);
//            jNotification.put("sound", "default");
//            jNotification.put("badge", "1");
//            jNotification.put("click_action", msg[3]);
//            jNotification.put("icon", "ic_stat_ic_notification");
//
//            jData.put("picture", "https://miro.medium.com/max/1400/1*QyVPcBbT_jENl8TGblk52w.png");
//
//            switch(type) {
//                case "tokens":
//                    JSONArray ja = new JSONArray();
//                    ja.put("c5pBXXsuCN0:APA91bH8nLMt084KpzMrmSWRS2SnKZudyNjtFVxLRG7VFEFk_RgOm-Q5EQr_oOcLbVcCjFH6vIXIyWhST1jdhR8WMatujccY5uy1TE0hkppW_TSnSBiUsH_tRReutEgsmIMmq8fexTmL");
//                    ja.put(tok);
//                    jPayload.put("registration_ids", ja);
//                    break;
//                case "topic":
//                    jPayload.put("to", "/topics/" + target);
//                    break;
//                case "condition":
//                    jPayload.put("condition", "'sport' in topics || 'news' in topics");
//                    break;
//                default:
//                    jPayload.put("to", tok);
//            }
//
//            jPayload.put("priority", "high");
//            jPayload.put("notification", jNotification);
//            jPayload.put("data", jData);
//
//            URL url = new URL("https://fcm.googleapis.com/fcm/send");
//
//
//
//
//            MessageBuilder messageBuilder =  new NotificationCompat.MessagingStyle.Message("sd".subSequence(0,1),(long)12,);
//                    .setNotification(new Notification(msg[0],msg[1]))
//                    .setAndroidConfig(AndroidConfig.builder()
//                            .setTtl(3600 * 1000)
//                            .setNotification(AndroidNotification.builder()
//                                    .setIcon("stock_ticker_update")
//                                    .setColor("#f45342")
//                                    .build())
//                            .build())
//                    .setApnsConfig(ApnsConfig.builder()
//                            .setAps(Aps.builder()
//                                    .setBadge(42)
//                                    .build())
//                            .build())
//                    .setTopic("industry-tech")
//                    .build();
//
//
//
////            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
////            conn.setRequestMethod("POST");
////            conn.setRequestProperty("Authorization", AUTH_KEY);
////            conn.setRequestProperty("Content-Type", "application/json");
////            conn.setDoOutput(true);
////
////            // Send FCM message content.
////            OutputStream outputStream = conn.getOutputStream();
////            outputStream.write(jPayload.toString().getBytes());
////
////            // Read FCM response.
////            InputStream inputStream = conn.getInputStream();
////            final String resp = convertStreamToString(inputStream);
////
////            Handler h = new Handler(Looper.getMainLooper());
////            h.post(new Runnable() {
////                @Override
////                public void run() {
////                    Toast.makeText(context,resp ,Toast.LENGTH_SHORT).show();
////
////                }
////            });
//        } catch (JSONException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private static String convertStreamToString(InputStream is) {
//        Scanner s = new Scanner(is).useDelimiter("\\A");
//        return s.hasNext() ? s.next().replace(",", ",\n") : "";
//    }
//
//    public void toastMessage(String message){
//        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
//    }
//
//}
