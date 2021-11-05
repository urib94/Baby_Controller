package com.baby_controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextView email;
    TextView password;
    Button login;
    Button register;
    TextView popupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        mAuth = FirebaseAuth.getInstance();
        setUpButtons();

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }




    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null){

        }
    }

    //setUpButtons()
//Purpose: set up the buttons
    //Parameters: none
    //Returns: none
    public void setUpButtons() {
        email = (TextView) findViewById(R.id.email_adresse);
        password = (TextView) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        popupWindow = (TextView) findViewById(R.id.popup_window);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(v);
                //after register, go to main activity
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
            }



        });

    }

    public void login(View view) {
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("login success", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            MainActivity.setCurrUser(email.getText().toString());
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("login failure", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            popupWindow.setText("error, try diffrent email or password");
                            popupWindow.setVisibility(View.VISIBLE);
                            popupWindow.setTextSize(20);


                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    public void register(View view) {
        // goto register activity
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);

    }

    public void getUserFromFirebase(View view) {
        //get user from firebase
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //update current user


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }




}