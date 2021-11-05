package com.baby_controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.baby_controller.src.Baby;
import com.baby_controller.src.Config;
import com.baby_controller.src.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private User currUser;
    public FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        startActivity(new Intent(this,LoginActivity.class));
        configureNextButton();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser == null){
//            startActivity(new Intent(this,LoginActivity.class));
//        }

        //go to login activity
        
    }

    private void configureNextButton() {
        Button nextButton = (Button) findViewById(R.id.button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                String ma = currentUser.getEmail();
                if(Config.CUUR_USER == null) {
                    starter();
                }
            }
        });


    }



    public void starter(){
        startActivity(new Intent(this,LoginActivity.class));
    }
    public User getCurrUser() {
        return currUser;
    }


    public static void setCurrUser(String userName) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        User result = null;
        //get the user from the db
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Config.CUUR_USER  = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        reference.addValueEventListener(postListener);
        //this.currUser = reference.child(userName).get().getResult().getValue(User.class);
    }

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
}