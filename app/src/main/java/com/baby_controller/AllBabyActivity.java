package com.baby_controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baby_controller.src.Baby;
import com.baby_controller.src.BabyListAdapter;
import com.baby_controller.src.Config;
import com.baby_controller.src.LocalUser;
import com.baby_controller.src.Parent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

public class AllBabyActivity extends AppCompatActivity {
    public static Baby theChosenOne = null;
    ListView babies;
    public ArrayList<Baby> babiesList = new ArrayList<>();
    public BabyListAdapter babyListAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.all_baby_actitivity);
        babies = (ListView) findViewById(R.id.all_babies_list_of_babies);
    }


    @Override
    protected void onStart(){
        super.onStart();
        setContentView(R.layout.all_baby_actitivity);
//        if(Config.getCurrentUser().getUserType() == LocalUser.UserType.MANAGER){
//            TextView title = findViewById(R.id.all_the_babies);
//            title.setText(R.string.Kindergarten_all_babies_title);
//        }
        babies = (ListView) findViewById(R.id.all_babies_list_of_babies);
        listMaker();
    }

    private void listMaker() {
        LinkedList<Parent> parents = new LinkedList<Parent>();
//        getParentsList(parents);
        System.out.println("parents are " + parents.toString());
        babiesList.clear();
        if(Config.getCurrentUser().getUserType() == LocalUser.UserType.MANAGER){
            for(Parent parent : Config.getCurrInst().getParents()){
                babiesList.addAll(parent.getChildren());
            }
        }else{
            babiesList.addAll(((Parent)Config.getCurrentUser()).getChildren());
        }
        babyListAdapter = new BabyListAdapter(this,R.layout.baby_adapter_view,babiesList);
        babies.setAdapter(babyListAdapter);
        babies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                theChosenOne = babiesList.get(position);
                Intent intent = new Intent(AllBabyActivity.this,BabyInfoCardActivity.class);
                startActivity(intent);
            }
        });
    }

//    private void getParentsList(LinkedList<Parent> list) {
//        LinkedList<Parent> par= new LinkedList<>();
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().getRoot();
//        if(Config.getCurrentUser().getUserType() == LocalUser.UserType.MANAGER){
//            ref.child("Institutions").child(Config.getCurrentUser().getInstitutionName()).child("parents")
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            int i = 0;
//                            Parent tmp;
//                            do {
//                                tmp = snapshot.child(String.valueOf(i++)).getValue(Parent.class);
//                                list.add(tmp);
//                            }while (tmp != null);
//                            System.out.println("par list is  " + list.toString() + "  time  " + String.valueOf(System.currentTimeMillis()));
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//        }else {
//            ref.child("Institutions").child(Config.getCurrentUser().getInstitutionName()).child("parents")
//                    .child(Config.getCurrentUser().getName()).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            list.add(snapshot.getValue(Parent.class));
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//        }
//        System.out.println("par list is the  " + par.toString() + "  time  " + String.valueOf(System.currentTimeMillis()));
//    }

}
