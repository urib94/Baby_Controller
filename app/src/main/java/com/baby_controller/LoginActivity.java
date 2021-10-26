package com.baby_controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        jumpToMain(findViewById(R.id.login));
        setUpButtons();

    }

    private void setUpButtons() {
        Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void testSheets(View view){

    }

    public void jumpToMain(View view) {
        finish();
//        Button toMaim = (Button) findViewById(R.id.login);
//
//        toMaim.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            }
//        });
//        toMaim.callOnClick();
    }


//    public void login(View view){
//        if(/*checkLogin*/ true){
//            jumpToMain(view);
//        }
//    }
//
//
//    public void test(View view) {
//    }
}
