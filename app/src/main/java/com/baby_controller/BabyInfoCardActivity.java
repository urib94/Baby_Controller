package com.baby_controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baby_controller.src.Baby;
import com.baby_controller.src.Config;
import com.baby_controller.src.LocalUser;
import com.baby_controller.src.Meal;
import com.baby_controller.src.MealListAdapter;
import com.baby_controller.src.Parent;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.GraphView;

import java.sql.Time;
import java.text.MessageFormat;
import java.util.ArrayList;

public class BabyInfoCardActivity extends AppCompatActivity {
    private ListView vMealsHistory;
    private TextView tWight;
    private TextView tName;
    private TextView tTimeBetween;
    private TextView tAmount;
    private TextInputEditText wightFill;
    private TextView nameFill;
    private TextView timeBetweenFill;
    private TextView amountFill;
    private GraphView mealG;
    private GraphView wightG;
    private int hourInMil = 3600000;
    private ArrayList<Meal> mealArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baby_card_layout);
        configureButtons();


    }



    private void configureButtons() {
        System.out.println("configureButtons");
        vMealsHistory = findViewById(R.id.meal_history);
        tWight = findViewById(R.id.cared_wight);
        tName = findViewById(R.id.card_baby_name);
        tTimeBetween = findViewById(R.id.card_between);
        tAmount = findViewById(R.id.card_recommended_amount);
        Button bBack = findViewById(R.id.card_back);
        Button bSave = findViewById(R.id.save_changes);
        vMealsHistory = findViewById(R.id.meal_history);

        wightFill = findViewById(R.id.card_wight_fill);
        nameFill = findViewById(R.id.cared_name_fill);
        timeBetweenFill = findViewById(R.id.card_between_fill);
        amountFill = findViewById(R.id.card_amount_fill);

        wightFill.setText(String.valueOf(AllBabyActivity.theChosenOne.getWeight()));
        nameFill.setText(AllBabyActivity.theChosenOne.getName());
        Time tmpT = new Time(AllBabyActivity.theChosenOne.getTimeBetweenMeals());
        timeBetweenFill.setText(MessageFormat.format("{0}:{1}", String.valueOf((AllBabyActivity.theChosenOne.getTimeBetweenMeals() / hourInMil)), String.valueOf((AllBabyActivity.theChosenOne.getTimeBetweenMeals() % hourInMil) / 100000) ));
        amountFill.setText(String.valueOf(AllBabyActivity.theChosenOne.getRecommendedAmountPerMeal()));
        System.out.println("configureButtons1");

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Baby tmpBaby = AllBabyActivity.theChosenOne;
                boolean diff = false;
                String[] tmp = timeBetweenFill.getText().toString().split(":");
                long between = Long.parseLong(tmp[0]) * 1000 * 60 * 60 + Long.parseLong(tmp[1]) * 1000 * 60;
                if(!tmpBaby.getName().equals(tName.getText().toString())){
                    tmpBaby.setName(tName.getText().toString());
                    diff = true;
                }
                if (tmpBaby.getWeight() != Double.parseDouble(wightFill.getText().toString())){
                    tmpBaby.setWeight(Double.parseDouble(wightFill.getText().toString()));
                    diff = true;
                }
                if (between != tmpBaby.getTimeBetweenMeals()){
                    tmpBaby.setTimeBetweenMeals(between);
                    diff = true;
                }
                if (Integer.parseInt(amountFill.getText().toString()) != tmpBaby.getRecommendedAmountPerMeal()){
                    tmpBaby.setRecommendedAmountPerMeal(Integer.parseInt(amountFill.getText().toString()));
                    diff = true;
                }
                if(diff){
                    if(Config.getCurrentUser().getUserType() == LocalUser.UserType.PARENT){
                        ((Parent)Config.getCurrentUser()).getChildren().set(tmpBaby.getIndexInParent(),tmpBaby);
                    }
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
                    myRef.child("Institutions").child(Config.getCurrentUser().getInstitutionName()).child("parents")
                            .child(String.valueOf(tmpBaby.getIndexInInstitute())).child("children")
                            .child(String.valueOf(tmpBaby.getIndexInParent())).setValue(tmpBaby);
                    myRef.child("Users").child(tmpBaby.getParentUid()).child("children").child(String.valueOf(tmpBaby.getIndexInParent())).setValue(tmpBaby);

                }
            }
        });


        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BabyInfoCardActivity.this, AllBabyActivity.class);
                startActivity(intent);
                finish();
            }
        });
        System.out.println("configureButtons2");
        historyListMaker();
    }

    private void historyListMaker() {
        System.out.println("the chosen one == null ? " + AllBabyActivity.theChosenOne == null);
        if(AllBabyActivity.theChosenOne == null) return;
        if(vMealsHistory == null){
            vMealsHistory = findViewById(R.id.meal_history);
        }
        mealArrayList.addAll(AllBabyActivity.theChosenOne.getHistory());
        MealListAdapter mealListAdapter = new MealListAdapter(this, R.layout.meal_adapter_view, mealArrayList);
        vMealsHistory.setAdapter(mealListAdapter);
    }

}
