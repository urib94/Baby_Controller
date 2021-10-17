package com.baby_controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baby_controller.src.Child;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    public void test(View view){

        TextView textView = (TextView) view;
        TextView display = findViewById(R.id.textView2);
        String weight = textView.getText().toString();
        Child child = new Child(new Double(weight).doubleValue());
        double rec = child.getRecommendedAmountPerMeal();
        Snackbar.make(view,textView.getText(),10).show();
        System.out.println("the recommended amount for that baby is " + rec);
        display.setText("the recommended amount of food is" + rec);
        display.setVisibility((int)1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


}