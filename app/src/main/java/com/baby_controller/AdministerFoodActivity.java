package com.baby_controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.baby_controller.src.Config;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdministerFoodActivity extends AppCompatActivity {

    private static final String TAG = "Administer Food";
    protected TextView measuredWight;
    FeedingThred feedingThred;
    private Switch enterManually;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administer_food_activity);
        configureButtons();

        FirebaseDatabase.getInstance().getReference().child("Users").child(FeedingActivity2.babyToFeed.getParentUid())
                .child(String.valueOf(FeedingActivity2.babyToFeed.getIndexInParent())).child("history")
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (AdministerFoodActivity.this.feedingThred != null) {
                    AdministerFoodActivity.this.feedingThred.feedingComplete = true;
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
        feedingThred.start();
    }

    private void configureButtons() {
        Button feed = (Button) findViewById(R.id.feed_now);
        measuredWight = (TextView) findViewById(R.id.measured_wight);
        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FeedingActivity2.babyToFeed != null) {
                    if(enterManually.isChecked()){
                        FeedingActivity2.babyToFeed.eatingNextMeal(Integer.parseInt(measuredWight.getText().toString()));
                    }
                    else if(!measuredWight.getText().toString().equals("0")){
                        FeedingActivity2.babyToFeed.eatingNextMeal(Integer.parseInt(measuredWight.getText().toString()));
                        toastMessage(FeedingActivity2.babyToFeed.getName() + "ate " + String.valueOf(Config.getFoodAmount()) + " mL");
//                        String[] msg = new String[4];
//                        msg[0] = "Yamm!!";
//                        msg[2] = FeedingActivity2.babyToFeed.getName() + "is about to eat " + String.valueOf(Config.getFoodAmount()) + " mL";
//                        msg[3] = "OPEN_MAIN_ACTIVITY";
//                        NotificationManegerActivity.sendWithOtherThread("token", FeedingActivity2.babyToFeed.getRegistrationToken(),msg,FeedingActivity2.babyToFeed.getRegistrationToken(), AdministerFoodActivity.this);


                        feedingThred = null;
                    }

                    if(feedingThred != null && !feedingThred.isAlive()) {
                        feedingThred.start();
                    }
                }
            }
        });
        feedingThred = new FeedingThred();
        measuredWight.setText(String.valueOf(Config.getFoodAmount()));

        enterManually = findViewById(R.id.manually);


    }
    @Override
    protected void onStop(){
        super.onStop();
        feedingThred = null;
    }

    private class FeedingThred extends Thread{
        public boolean feedingComplete;

        private  FeedingThred(){
            feedingComplete = false;
        }

        @Override
        public void run() {
            super.run();
            while (System.currentTimeMillis() % 500 == 0){
                measuredWight.setText(String.valueOf(Config.getFoodAmount()));
            }
        }




    }



    private void toastMessage(String s) {
        Toast.makeText(AdministerFoodActivity.this ,s,Toast.LENGTH_SHORT).show();
    }
}
