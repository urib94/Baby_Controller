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

import com.baby_controller.src.Baby;
import com.baby_controller.src.Config;
import com.baby_controller.src.LocalUser;
import com.baby_controller.src.Parent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
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
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "LoginActivity";


    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    String userID;


    //buttons
    TextView email;
    TextView password;
    Button login;
    Button register;
    TextView popupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("Users");
        FirebaseUser user = mAuth.getCurrentUser();
//        userID = user.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user != null){
                    //user is signed in
                    Log.d(TAG, "onAuthStateChanged: signed in" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail());

                }else {
                    //user is signed out
                    if(userID != null) {
                        Log.d(TAG, "onAuthStateChanged : signed out" + user.getUid());
                        toastMessage("Successfully signed out with: " + user.getEmail());
                    }
                }
            }
        };


    }



    private void showData(DataSnapshot dataSnapshot){
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            if(ds.child(userID).getValue(LocalUser.class) != null) {
                if (ds.child(userID).getValue(LocalUser.class).getEmail().equals(email.getText().toString())) {
                    MainActivity.currLocalUser = new LocalUser();
                    MainActivity.currLocalUser.setUserName(ds.child(userID).getValue(LocalUser.class).getUserName()); //set the user name
                    MainActivity.currLocalUser.setEmail(ds.child(userID).getValue(LocalUser.class).getEmail());
                    MainActivity.currLocalUser.setPassword(ds.child(userID).getValue(LocalUser.class).getPassword());
                    MainActivity.currLocalUser.setUserType(ds.child(userID).getValue(LocalUser.class).getUserType());
                    MainActivity.currLocalUser.setDefaultDevice(ds.child(userID).getValue(LocalUser.class).getDefaultDevice());
                    MainActivity.currLocalUser.setInstitutionName(ds.child(userID).getValue(LocalUser.class).getInstitute().getName());
                    MainActivity.currLocalUser.setInstitutionName(ds.child(userID).getValue(LocalUser.class).getInstitutionName());
                    MainActivity.currLocalUser.setReference(ds.child(userID).getValue(LocalUser.class).getReference());
                    MainActivity.currLocalUser.setUid(ds.child(userID).getValue(LocalUser.class).getUid());

                    if (MainActivity.currLocalUser.getUserType() == LocalUser.UserType.PARENT) {
                        for (Baby baby : ds.child(userID).getValue(Parent.class).getChildren()) {
                            ((Parent) MainActivity.currLocalUser).addNewChild(baby);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        setUpButtons();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListener != null){
          mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    private void updateUI(FirebaseUser currentUser) {
        LoginActivity.this.onStop();
        finish();
    }

    public void setUpButtons() {
        email = (TextView) findViewById(R.id.email_adresse);
        password = (TextView) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        popupWindow = (TextView) findViewById(R.id.popup_window);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Read from the database
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        if(dataSnapshot != null) {
                            showData(dataSnapshot);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
                login(v);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(v);
            }



        });

    }

    private void toastMessage(String s) {
        Toast.makeText(LoginActivity.this ,s,Toast.LENGTH_SHORT).show();
    }

    public void login(View view) {
        String mail = email.getText().toString();
        String pass = password.getText().toString();
        if (!mail.equals("") && !pass.equals("")){
            mAuth.signInWithEmailAndPassword(mail, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("login success", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                setCurrUser(getUserFromFirebase(email.getText().toString()));
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "error, try different email or password",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            toastMessage("You didn't fill in all the fields");
        }
    }


    public void register(View view) {
        // goto register activity
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public LocalUser getUserFromFirebase(String userName) {
        //get user from firebase
        final LocalUser[] tmp = {new LocalUser()};
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(userName);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //update current user
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getKey().equals(mAuth.getCurrentUser().getUid())) {
                        tmp[0] = ds.getValue(LocalUser.class);
                        break;
                    }
                }
                System.out.println(Config.CUUR_USER.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ref.addListenerForSingleValueEvent(valueEventListener);
        return tmp[0];
    }

    public void setCurrUser(LocalUser localUser) {
        Config.CUUR_USER  = localUser;
        System.out.println("\n\n\n"+ Config.CUUR_USER.toString() + "\n\n\n\n\n");
    }

}