package com.baby_controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.baby_controller.src.Child;
import com.baby_controller.src.Day;
import com.baby_controller.src.FeedingHistory;
import com.baby_controller.src.Institution;
import com.baby_controller.src.Manager1;
import com.baby_controller.src.Meal;
import com.baby_controller.src.Parent;
import com.baby_controller.src.User;
import com.baby_controller.src.util.DatabaseManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    public void test(View view){

        TextView textView = (TextView) view;
        TextView display = findViewById(R.id.button);
        String weight = textView.getText().toString();
        Child child = new Child("newBorn",new Double(weight).doubleValue());
        double rec = child.getRecommendedAmountPerMeal();
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
            @Override
            public void onClick(View view) {
                Manager1 man = new Manager1("manager1","mager1password");
                Institution inst = new Institution(man,"institute1");
                inst.addManager(new Manager1("manager2","manager2Password"));
//                inst.addManager(man);
                Parent parent1 = new Parent("parent1", "password 1", User.UserType.PARENT);
                Child child = new Child("first baby",2.5);
//                man.addParent(parent1);
//                man.getParents().get(0).addNewChild(child);
//                man.getParents().get(0).getChildren().get(0).eatingNextMeal(60);
//
//
//
//
//
//
//
                Day day = new Day(child);
                Meal meal = new Meal();
                FeedingHistory feedingHistory = new FeedingHistory();

//                parent1.setInstitution(new Institution());
//                parent1.getInstitution().setName("test");
//                Parent[] perentsOfChild = {parent1};
//                man.setInstitution(inst);
//
//                day.set_carrDate(new Date(System.currentTimeMillis()));
//                feedingHistory.set_curr(day);
//                child.setHistory(feedingHistory);
//                child.eatingNextMeal(60);
//                meal.set_receivedAmount(30);
//                meal.set_receivedAmount(35);
//                meal.setEaten(1);
//                meal.set_whenEaten(new Time(System.currentTimeMillis()));

                DatabaseManager databaseManager = new DatabaseManager();
                Manager1 many = new Manager1("meny1","meny1pas");
//                DatabaseManager.addNewManager(inst,man);
                DatabaseManager.addNewInstitution(new Institution(man,"first insi"));
                DatabaseManager.addNewParent(inst,man ,parent1);
//                DatabaseManager.addNewChild(parent1,child);
//                DatabaseManager.addNewMeal(child,new Meal());

            }
        });
    }

    public void jumpToMain(View view) {
    }
}