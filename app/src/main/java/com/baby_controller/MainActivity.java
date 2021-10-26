package com.baby_controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.baby_controller.src.Baby;
import com.baby_controller.src.Day;
import com.baby_controller.src.FeedingHistory;
import com.baby_controller.src.Institution;
import com.baby_controller.src.InstitutionHandler;
import com.baby_controller.src.Manager1;
import com.baby_controller.src.Meal;
import com.baby_controller.src.Parent;
import com.baby_controller.src.User;
import com.baby_controller.src.util.DatabaseManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

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
        DatabaseManager.dbRef.child("asdasdasd");

        configureNextButton();
    }

    private void configureNextButton() {
        Button nextButton = (Button) findViewById(R.id.button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Manager1 man = new Manager1("motherLOver","manager1","mager1password");
                Institution inst = new Institution(man,"institute1");
                //InstitutionHandler.addInstitution(inst);
                inst.addManager(new Manager1("many2","manager2","manager2Password"));
//                inst.addManager(man);
                Parent parent1 = new Parent("parent1", "password 1", User.UserType.PARENT);
                Baby baby2 = new Baby("first baby2",2.5);
                //inst.uploadToDb();
                parent1.setInstitutionName(inst);

                Baby baby1 = new Baby("mother fucker");
                baby1.set_weight(3.5);
                baby1.setParent(parent1);
                baby1.set_weight(3.5);
                parent1.uploadToDb();
                baby1.eatingNextMeal(60);
                inst.uploadToDb();
            }
        });
    }

    public void jumpToMain(View view) {
    }
}