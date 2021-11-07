package com.baby_controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.baby_controller.src.Baby;
import com.baby_controller.src.LocalUser;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    public static LocalUser currLocalUser;
    public FirebaseAuth mAuth;
    private Button btMenu;
    private Button feeding;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureButtons();

        mAuth = FirebaseAuth.getInstance();
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.signOut();
    }

    private void configureButtons() {
        feeding = (Button) findViewById(R.id.feeding);
        feeding.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FeedingActivity.class);
                startActivity(intent);
                }
        });

        btMenu = (Button) findViewById(R.id.bt_menu);

        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,BluetoothManager.class);
                startActivity(intent);
            }
        });



    }



    public void starter(){
        startActivity(new Intent(this,LoginActivity.class));
    }
    public LocalUser getCurrUser() {
        return currLocalUser;
    }



    public void test(View view){

        TextView textView = (TextView) view;

        TextView display = findViewById(R.id.bt_menu);
        String weight = textView.getText().toString();
        Baby baby = new Baby("newBorn",new Double(weight).doubleValue());
        double rec = baby.getRecommendedAmountPerMeal();
        Snackbar.make(view,textView.getText(),10).show();
        System.out.println("the recommended amount for that baby is " + rec);
        display.setText("the recommended amount of food is" + rec);
        display.setVisibility((int)1);
    }
}