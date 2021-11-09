package com.baby_controller;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.baby_controller.src.Baby;
import com.baby_controller.src.BabyListAdapter;
import com.baby_controller.src.Institution;
import com.baby_controller.src.LocalUser;
import com.baby_controller.src.Parent;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManegeInstituteActivity extends AppCompatActivity {

    public ArrayList<Baby> mBabies = new ArrayList<>();

    public BabyListAdapter mBabyListAdapter;

    ListView lvBabies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(getApplicationContext());
        super.onCreate(savedInstanceState);
        if(MainActivity.currLocalUser.getUserType() == LocalUser.UserType.MANAGER ){
            setContentView(R.layout.manager_mange_institute);
            MangerBabyListMaker();
        }else{
            setContentView(R.layout.parent_manege_institute);

        }
    }

    public void MangerBabyListMaker(){
        //get the institute of the user from the database
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Institutions")
                .child(MainActivity.currLocalUser.getInstitutionName());

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Institution institution = dataSnapshot.getValue(Institution.class);
                if(institution != null){
                    for(LocalUser parent : institution.getParents()){
                        mBabies.addAll(((Parent)parent).getBabiesNeedToFeed());
                    }
                    mBabyListAdapter = new BabyListAdapter(ManegeInstituteActivity.this,R.layout.baby_adapter_view,mBabies);
                    lvBabies.setAdapter(mBabyListAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void ParentBabyListMaker(){
        //get the institute of the user from the database
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Institutions")
                .child(MainActivity.currLocalUser.getInstitutionName());

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Institution institution = dataSnapshot.getValue(Institution.class);
                if(institution != null){
                    for(LocalUser parent : institution.getParents()){
                        mBabies.addAll(((Parent)parent).getBabiesNeedToFeed());
                    }
                    mBabyListAdapter = new BabyListAdapter(ManegeInstituteActivity.this,R.layout.baby_adapter_view,mBabies);
                    lvBabies.setAdapter(mBabyListAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
