package com.baby_controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baby_controller.src.Baby;
import com.baby_controller.src.Config;
import com.baby_controller.src.Institution;
import com.baby_controller.src.LocalUser;
import com.baby_controller.src.Manager1;
import com.baby_controller.src.Meal;
import com.baby_controller.src.Parent;
import com.baby_controller.src.util.cloudMessgaging.Notify;
import com.baby_controller.src.util.cloudMessgaging.SharedPreferencesManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pubnub.api.PubNub;

import java.sql.Time;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "LoginActivity";
    public static PubNub pubnub; // PubNub instance

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    String userUID;


    //buttons
    TextView email;
    TextView password;
    Button login;
    Button register;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        SharedPreferencesManager.init(this);
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
                    if(userUID != null) {
                        Log.d(TAG, "onAuthStateChanged : signed out");
                        toastMessage("Successfully signed out with: ");
                    }
                }
            }
        };
//
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                if (dataSnapshot != null && mAuth.getCurrentUser() != null) {
//                    showData(dataSnapshot);
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
    }





    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        setUpButtons();
    }




    private void updateUI() {

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();

    }

    private void setChildrenListeners() {
        if(Config.getCurrentUser().getUserType() == LocalUser.UserType.PARENT){
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
            for(Baby baby:((Parent)Config.getCurrentUser()).getChildren()) {

                myRef.child("Users").child(userUID).child("children").child(String.valueOf(baby.getIndexInParent()))
                        .child("history").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Meal meal;
                        try {
                            meal = snapshot.getValue(Meal.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                        assert meal != null;
                        Time time = new Time(meal.getWhenEaten());
                        Notify.build(getApplicationContext())
                                .setTitle("Yamm!")
                                .setContent(baby.getName() + " just eat " + meal.getReceivedAmount() +
                                        "at " + time.getHours() + ":" + time.getMinutes())
                                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                                .setLargeIcon(R.drawable.ic_stat_ic_notification)
                                .largeCircularIcon()
                                .setColor(R.color.browser_actions_divider_color)
                                .show();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        }
    }

    public void setUpButtons() {
        email = (TextView) findViewById(R.id.email_adresse);
        password = (TextView) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
                    // Read from the database
                    login(v);
                }
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
                        userUID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                        myRef.getRoot().child("Users").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                boolean found = false;
                                System.out.println(snapshot.toString());
                                for (DataSnapshot snap : snapshot.getChildren()) {
                                    System.out.println("key in login loop = " + snap.getKey());
                                    if (Objects.equals(snap.getKey(), userUID)) {
                                        try {
                                            LocalUser localUser = snap.getValue(LocalUser.class);
                                            if (snap.getValue(LocalUser.class).getUserType() == LocalUser.UserType.MANAGER) {
                                                Config.setCurrentUser(snap.getValue(Manager1.class));
                                                Log.i(TAG, "update local user to a manger");
                                                found = true;
                                            } else {
                                                Parent parent = snap.getValue(Parent.class);
                                                Config.setCurrentUser(parent);
                                                found = true;
                                                Log.i(TAG, "update local user to a parent");
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                            @Override
                           public void onCancelled(@NonNull DatabaseError error) { }


                   });
                        myRef.getRoot().child("Users").addValueEventListener(new ValueEventListener() {


                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot snap : snapshot.getChildren()){
                                    if(Objects.equals(snap.getKey(), Config.getCurrentUser().getInstitutionName())){
                                        Institution tmp = null;
                                        try {
                                            tmp = snap.getValue(Institution.class);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                        if(tmp != null) {
                                            Config.setCurrInst(snap.getValue(Institution.class));
                                        }
                                        System.out.println(tmp.toString());
                                    }
                                }
                                setChildrenListeners();
                                myRef = FirebaseDatabase.getInstance().getReference();
                                updateUI();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "error, try different email or password",
                            Toast.LENGTH_SHORT).show();
                }
                    myRef.getRoot().child("Users").child("trigger").setValue(" ");
                    myRef.getRoot().child("Users").child("trigger").setValue(null);
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
        tmp[0].setName("tmp");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userName);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LocalUser localUser = new LocalUser("s", "s", "s", LocalUser.UserType.MANAGER);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (Objects.equals(ds.getKey(), Objects.requireNonNull(mAuth.getCurrentUser()).getUid())) {
                        Config.setCurrentUser(ds.getValue(LocalUser.class));
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };


        ref.addValueEventListener(valueEventListener);
        return Config.getCurrentUser();
    }




}