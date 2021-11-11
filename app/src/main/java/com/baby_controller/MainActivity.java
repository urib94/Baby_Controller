package com.baby_controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.baby_controller.src.Baby;
import com.baby_controller.src.Config;
import com.baby_controller.src.LocalUser;
import com.baby_controller.src.Manager1;
import com.baby_controller.src.Parent;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static LocalUser currLocalUser;
    public FirebaseAuth mAuth;
    private Button btMenu;
    private Button feeding;
    private Button blogout;
    private Context context;
    DatabaseReference myRef;
    private ArrayList<LocalUser> localUsers = new ArrayList<>();
    private String uid;
    boolean showwwww = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FirebaseApp.initializeApp(getApplicationContext());
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
    }

@Override
protected void onStart() {
    super.onStart();
    configureButtons();
    mAuth = FirebaseAuth.getInstance();
    uid = mAuth.getUid();
    if (mAuth.getCurrentUser() != null) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

    @Override
    protected void onRestart() {
        super.onRestart();
        myRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showData1(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        setContentView(R.layout.activity_main);
        configureButtons();
        getUserFromFirebase(mAuth.getCurrentUser().getUid());
    }


    private void showData(DataSnapshot dataSnapshot){
        showwwww = true;
        System.out.println("sd.toString: " + dataSnapshot.child(uid).getRef().toString() +
                " uid:  "+ uid);
        Object tmpUserObject = dataSnapshot.child(uid).getValue(Object.class);
        System.out.println(tmpUserObject);
        String [] check = {tmpUserObject.toString()};

        Config.setCurrentUser(new LocalUser(LocalUser.LocalUserFromString(tmpUserObject.toString())));
        System.out.println("curr user" + Config.getCurrentUser().toString());

        LocalUser test = new LocalUser(LocalUser.LocalUserFromString(tmpUserObject.toString()));

        System.out.println("test = " + test.toString());
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            LocalUser localUser = ds.child(uid).getValue(LocalUser.class);
            LocalUser tmpUser = null;
            if(localUser != null) {
                if (localUser.getUserType() == LocalUser.UserType.MANAGER) {
                    Config.setCurrentUser(ds.child(uid).getValue(Manager1.class));
                } else {
                    Config.setCurrentUser(ds.child(uid).getValue(Parent.class));
                }
            }
        }
        System.out.println(mAuth.getUid());
        for(DataSnapshot ds : dataSnapshot.getChildren()){


//            String uid2 = ds.child(mAuth.getUid()).getValue(LocalUser.class).getUid();
//            System.out.println("uid2 = " + uid2);

            if(LocalUser.getUserTypeFromString(tmpUserObject.toString()) != null) {

                if (ds.child(mAuth.getUid()).getValue(LocalUser.class).getUid().equals(mAuth.getUid())) {
                    Config.setCurrentUser(new LocalUser(LocalUser.LocalUserFromString(tmpUserObject.toString())));
                    System.out.println("curr user" + Config.getCurrentUser().toString());
//                    Config.getCurrentUser().setUserName(ds.child(mAuth.getUid()).getValue(LocalUser.class).getUserName()); //set the user name
//                    Config.getCurrentUser().setEmail(ds.child(mAuth.getUid()).getValue(LocalUser.class).getEmail());
//                    Config.getCurrentUser().setPassword(ds.child(mAuth.getUid()).getValue(LocalUser.class).getPassword());
//                    Config.getCurrentUser().setUserType(ds.child(mAuth.getUid()).getValue(LocalUser.class).getUserType());
//                    Config.getCurrentUser().setDefaultDevice(ds.child(mAuth.getUid()).getValue(LocalUser.class).getDefaultDevice());
//                    Config.getCurrentUser().setInstitutionName(ds.child(mAuth.getUid()).getValue(LocalUser.class).getInstitute().getName());
//                    Config.getCurrentUser().setInstitutionName(ds.child(mAuth.getUid()).getValue(LocalUser.class).getInstitutionName());
//                    Config.getCurrentUser().setReference(ds.child(mAuth.getUid()).getValue(LocalUser.class).getReference());
//                    Config.getCurrentUser().setUid(ds.child(mAuth.getUid()).getValue(LocalUser.class).getUid());

                    if (Config.getCurrentUser().getUserType() == LocalUser.UserType.PARENT) {
                        for (Baby baby : ds.child(mAuth.getUid()).getValue(Parent.class).getChildren()) {
                            ((Parent) MainActivity.currLocalUser).addNewChild(baby);
                        }
                    }
                }
            }
        }
        if(Config.getCurrentUser() != null) {
            Toast.makeText(MainActivity.this, Config.getCurrentUser().getUid(), Toast.LENGTH_LONG);
        }else {
            Toast.makeText(MainActivity.this, "curr user is null", Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //mAuth.signOut();
    }

    private void configureButtons() {
        feeding = (Button) findViewById(R.id.feeding);
        if(Config.getBluetoothConnectionManger() != null && Config.getBluetoothConnectionManger().getmBTSocket() != null
                && Config.getBluetoothConnectionManger().getmBTSocket().isConnected()) {
            feeding.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, FeedingActivity2.class);
                    startActivity(intent);
                }
            });
        }

        btMenu = (Button) findViewById(R.id.bt_menu);

        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,BluetoothConnectionManager.class);
                startActivity(intent);
            }
        });
        feeding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FeedingActivity2.class);
                startActivity(intent);
            }
        });

        blogout = (Button) findViewById(R.id.logout);
        blogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                FirebaseUser us = mAuth.getCurrentUser();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }
        });


    }

    public void starter(){
        startActivity(new Intent(this,LoginActivity.class));
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

    public void getUserFromFirebase(String userName) {
        //get user from firebase
        final LocalUser[] tmp = {new LocalUser()};
        tmp[0].setUserName("tmp");
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
    }






    private void showData1(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            LocalUser uInfo = new LocalUser();
            uInfo.setPassword(ds.child(mAuth.getUid()).getValue(LocalUser.class).getPassword()); //set the name
            uInfo.setEmail(ds.child(mAuth.getUid()).getValue(LocalUser.class).getEmail()); //set the email
            uInfo.setUserName(ds.child(mAuth.getUid()).getValue(LocalUser.class).getUserName()); //set the phone_num


            ArrayList<String> array  = new ArrayList<>();
            array.add(uInfo.getPassword());
            array.add(uInfo.getEmail());
            array.add(uInfo.getUserName());
            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,array);

        }
    }

}