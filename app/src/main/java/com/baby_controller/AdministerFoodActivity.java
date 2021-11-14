package com.baby_controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baby_controller.src.Config;

public class AdministerFoodActivity extends AppCompatActivity {

    private static final String TAG = "Administer Food";
    private Button feed;
    private TextView measuredWight;
    FeedingThred feedingThred;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administer_food_activity);

        configureButtons();
    }

    private void configureButtons() {
        feed = (Button) findViewById(R.id.feed_now);
        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FeedingActivity2.babyToFeed != null) {
                    feedingThred.start();
                }
            }
        });
        feedingThred = new FeedingThred();
        measuredWight.setText(String.valueOf(Config.getFoodAmount()));


    }
    @Override
    protected void onStop(){
        super.onStop();
        feedingThred = null;
    }

    private class FeedingThred extends Thread{
        private boolean feedingComplete;

        private  FeedingThred(){
            feedingComplete = false;
        }

        @Override
        public void run() {
            super.run();
            while (!feedingComplete){
                feedBaby();
            }
        }



        public void feedBaby(){
            if(Config.getFoodAmount() != 0) {
                FeedingActivity2.babyToFeed.eatingNextMeal((int) Config.getFoodAmount());
                toastMessage(FeedingActivity2.babyToFeed.getName() + "ate " + String.valueOf(Config.getFoodAmount()) + " mL");
                feedingComplete = true;
                //todo add a visual comformetion to the user that the meal is given
            }
        }
    }



    private void toastMessage(String s) {
        Toast.makeText(AdministerFoodActivity.this ,s,Toast.LENGTH_SHORT).show();
    }
}
