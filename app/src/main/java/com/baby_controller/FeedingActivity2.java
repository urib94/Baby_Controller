package com.baby_controller;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.baby_controller.src.Baby;
import com.baby_controller.src.BabyListAdapter;
import com.baby_controller.src.Config;
import com.baby_controller.src.Institution;
import com.baby_controller.src.LocalUser;
import com.baby_controller.src.Parent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FeedingActivity2 extends AppCompatActivity {
    //static variables
    private static final String TAG = "Feeding Activity";
    public static Baby babyToFeed;

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    //buttons
    private Button btnDiscovere;

    private Button feed;

    Button btnOnOFf;

    Button btPairedDevices;

    CheckBox chooseDefault;

    ListView reallyHungryBabies;

    ListView hungryBabies;


    //bt
    BluetoothDevice mBTDevice;

    //data
    public ArrayList<Baby> mHungryBabies = new ArrayList<>();
    public ArrayList<Baby> mReallyHungryBabies = new ArrayList<>();
    public BabyListAdapter mReallyHungryBabyListAdapter;
    public BabyListAdapter mHungryBabyListAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choos_baby_to_feed);
        configureBabyChooserButtons();
        babyListsMaker();
    }

    public void what (DataSnapshot snapshot){
        for(DataSnapshot snap :snapshot.getChildren()){
            System.out.println("curr key = " + snap.getKey()  + " - " + (snap.getValue() instanceof String));
            if(snap.getValue() instanceof ArrayList || snap.getValue() instanceof List){
                for (DataSnapshot dataSnapshot: snap.getChildren()){
                    System.out.println("curr key = " + dataSnapshot.getKey()  + " - " + (dataSnapshot.getValue() instanceof String));
                    if(snap.getValue() instanceof ArrayList || snap.getValue() instanceof List) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            System.out.println("curr key = " + dataSnapshot1.getKey() + " - " + (dataSnapshot1.getValue() instanceof String));
                        }
                    }
                }
            }
        }
    }

    public void babyListsMaker(){
        System.out.println(Config.getCurrentUser().toString());
        //get the institute of the user from the database
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Institutions")
                .child(Config.getCurrentUser().getInstitutionName());

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.toString());
                what(dataSnapshot);
                Institution institution = dataSnapshot.getValue(Institution.class);
                if(institution != null && institution.getParents() != null){
                    for(Parent parent : institution.getParents()){
                        mHungryBabies.addAll(parent.getBabiesNeedToFeed());
                    }
                    mReallyHungryBabyListAdapter = new BabyListAdapter(FeedingActivity2.this,R.layout.baby_adapter_view, mHungryBabies);
                    reallyHungryBabies.setAdapter(mReallyHungryBabyListAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //the same for the babies that are not hungry
        DatabaseReference mDatabaseReference2 = FirebaseDatabase.getInstance().getReference().child("Institutions")
                .child(Config.getCurrentUser().getInstitutionName());

        mDatabaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Institution institution = dataSnapshot.getValue(Institution.class);
                if(institution != null){
                    for(LocalUser parent : institution.getParents()){
                        mHungryBabies.addAll(((Parent)parent).getBabiesNotNeedToFeed());
                    }
                    mHungryBabyListAdapter = new BabyListAdapter(FeedingActivity2.this,R.layout.baby_adapter_view, mHungryBabies);
                    hungryBabies.setAdapter(mReallyHungryBabyListAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void configureBabyChooserButtons() {
        reallyHungryBabies = (ListView) findViewById(R.id.really_hungry_list);
        hungryBabies = (ListView) findViewById(R.id.hungry_list);

        reallyHungryBabies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                babyToFeed = mReallyHungryBabies.get(position);
                Intent intent  = new Intent(FeedingActivity2.this,AdministerFoodActivity.class);
                startActivity(intent);
            }
        });

        hungryBabies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                babyToFeed = mHungryBabies.get(position);
                Intent intent  = new Intent(FeedingActivity2.this,AdministerFoodActivity.class);
                startActivity(intent);
            }
        });

    }




    private void toastMessage(String s) {
        Toast.makeText(FeedingActivity2.this ,s,Toast.LENGTH_SHORT).show();
    }
}
