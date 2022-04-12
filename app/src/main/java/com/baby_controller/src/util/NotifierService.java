package com.baby_controller.src.util;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.baby_controller.R;
import com.baby_controller.src.Baby;
import com.baby_controller.src.Config;
import com.baby_controller.src.Institution;
import com.baby_controller.src.LocalUser;
import com.baby_controller.src.Manager1;
import com.baby_controller.src.Parent;
import com.baby_controller.src.util.cloudMessgaging.Notify;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class NotifierService extends Service {
    private Notify notify;
    private String[][] alerts;
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;



    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            addListeners();


            while (true) {
                updateCurrUser();
                notifyToEat();
                notifyEaten();
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    // Restore interrupt status.
                    Thread.currentThread().interrupt();
                }
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job

        }

        private void addListeners() {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("Institutions").child(Config.getCurrentUser().getInstitutionName()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Institution tmp = null;
                    try{
                        tmp = snapshot.getValue(Institution.class);
                    }catch (Exception ignored){}
                    if(tmp != null) {
                        Config.setCurrInst(tmp);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });

            ref.child("Users").child(Config.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    LocalUser tmp = null;
                    if(Config.getCurrentUser().getUserType().equals(LocalUser.UserType.PARENT)){
                        try{
                            tmp = snapshot.getValue(Parent.class);
                        }catch (Exception ignored){}
                    }else{
                        try{
                            tmp = snapshot.getValue(Manager1.class);
                        }catch (Exception ignored){}
                        Config.setCurrentUser(tmp);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        private void updateCurrUser() {

        }

        private void notifyEaten() {

            if(Config.getCurrentUser().getUserType().equals(LocalUser.UserType.PARENT)){
                for(int i = 0; i < alerts.length; i++){
                    if (Objects.equals(alerts[i][0], ((Parent)Config.getCurrentUser()).getChildren().get(i).getName() + Config.getCurrentUser().getUid())
                    && !(String.valueOf(((Parent)Config.getCurrentUser()).getChildren().get(i).getHistory().size()).equals(alerts[i][2]))){
                        notifyAfterMeal(((Parent)Config.getCurrentUser()).getChildren().get(i));
                        alerts[i][2] = String.valueOf(((Parent) Config.getCurrentUser()).getChildren().get(i).getHistory().size());
                    }
                }
            }else{
//                int i = 0;
//                for(Parent parent: Config.getCurrInst().getParents()){
//                    for(int j = 0; j < parent.getChildren().size(); j++) {
//                        if (Objects.equals(alerts[i][0], (parent.getChildren().get(j).getName() + Config.getCurrentUser().getUid()))
//                                && !(String.valueOf(parent.getChildren().get(j).getHistory().size()).equals(alerts[i][2]))){
//                            notifyAfterMeal(((Parent) Config.getCurrentUser()).getChildren().get(j));
//                            alerts[i++][2] = String.valueOf(parent.getChildren().get(j).getHistory().size());
//                        }
//                    }
//                }
            }

        }



        private void notifyToEat() {
            startAlerts();
            if (Config.getCurrentUser().getUserType() == LocalUser.UserType.PARENT) {
                for (Baby baby : ((Parent) Config.getCurrentUser()).getChildren()) {
                    for (int i = 0; i < alerts.length; i++) {
                        if (Objects.equals(alerts[i][0], baby.getName() + Config.getCurrentUser().getUid())) {
                            if (!baby.needToEat()) {
                                alerts[i][1] = "n";
                            } else if (alerts[i][1].equals("n")) {
                                notifyNeedToEat(baby);
                                alerts[i][1] = "y";
                            }
                        }
                    }
                }
            }else {
                for (Parent parent : Config.getCurrInst().getParents()) {
                    for (Baby baby : parent.getChildren()) {
                        for (int i = 0; i < alerts.length; i++) {
                            if (Objects.equals(alerts[i][0], baby.getName() + Config.getCurrentUser().getUid())) {
                                if (!baby.needToEat()) {
                                    alerts[i][1] = "n";
                                } else if (alerts[i][1].equals("n")) {
                                    notifyNeedToEat(baby);
                                    alerts[i][1] = "y";
                                }
                            }
                        }
                    }
                }
            }
        }
        private void notifyAfterMeal(Baby baby) {
            Notify.build(getApplicationContext())
                    .setTitle("Yamm!")
                    .setContent(baby.getName() + " just ate " + baby.getHistory().get(baby.getHistory().size()).getReceivedAmount() + "mL")
                    .setSmallIcon(R.drawable.ic_stat_ic_notification)
                    .setLargeIcon(R.drawable.ic_stat_ic_notification)
                    .largeCircularIcon()
                    .setColor(R.color.browser_actions_divider_color)
                    .show();
        }
        private void notifyNeedToEat(Baby baby) {
                Notify.build(getApplicationContext())
                        .setTitle(baby.getName() + " Is Hungry")
                        .setContent("Its time for " + baby.getName() + " to eat\n " + baby.getRecommendedAmountPerMeal() +" mL is recommended" )
                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
                        .setLargeIcon(R.drawable.ic_stat_ic_notification)
                        .largeCircularIcon()
                        .setColor(R.color.browser_actions_divider_color)
                        .show();
        }




    }

    private void startAlerts() {
        if(alerts != null) return;
        if(Config.getCurrentUser().getUserType() == LocalUser.UserType.PARENT) {
            alerts = new String[((Parent) Config.getCurrentUser()).getChildren().size()][3];
            for(int i = 0; i < alerts.length; i++){
                alerts[i][0] = ((Parent) Config.getCurrentUser()).getChildren().get(i).getName() +
                        Config.getCurrentUser().getUid();
                alerts[i][1] = "n";
                alerts[i][2] = String.valueOf(((Parent) Config.getCurrentUser()).getChildren().get(i).getHistory().size());

            }
        }else {
            int amount = 0;
            for(Parent parent: Config.getCurrInst().getParents()){
                amount += parent.getChildren().size();
            }
            alerts = new String[amount][3];
            int i = 0;
            for(Parent parent: Config.getCurrInst().getParents()) {
                int j = 0;
                for (; i < alerts.length; i++) {
                    alerts[i][0] = parent.getChildren().get(j++).getName() + parent.getUid();
                    alerts[i][1] = "n";
                    alerts[i][2] = String.valueOf(parent.getChildren().get(j++).getHistory().size());
                }
            }
        }

    }



    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

}
