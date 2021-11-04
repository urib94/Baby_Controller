package com.baby_controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.baby_controller.src.Baby;
import com.baby_controller.src.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {
    private User currUser;

    public void test(View view){

        TextView textView = (TextView) view;
        TextView display = findViewById(R.id.button);
        String weight = textView.getText().toString();
        Baby baby = new Baby("newBorn",new Double(weight).doubleValue());
        double rec = baby.getRecommendedAmountPerMeal();
        Snackbar.make(view,textView.getText(),10).show();
        System.out.println("the recommended amount for that baby is " + rec);
        display.setText("the recommended amount of food is" + rec);
        display.setVisibility((int)1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureNextButton();
    }

    private void configureNextButton() {
        Button nextButton = (Button) findViewById(R.id.button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

            }
        });


    }

    public User getCurrUser() {
        return currUser;
    }

    public void setCurrUser(User currUser) {
        this.currUser = currUser;
    }
}