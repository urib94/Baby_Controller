package com.baby_controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baby_controller.src.Baby;
import com.baby_controller.src.BabyListAdapter;
import com.baby_controller.src.Config;
import com.baby_controller.src.LocalUser;
import com.baby_controller.src.Parent;

import java.util.ArrayList;

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
        if(Config.getCurrentUser().getUserType() == LocalUser.UserType.MANAGER){
            TextView title = findViewById(R.id.all_the_babies);
            title.setText(R.string.Kindergarten_all_babies_title);
        }
        babies = (ListView) findViewById(R.id.all_babies_list_of_babies);
        listMaker();
    }

    private void listMaker() {

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
}
