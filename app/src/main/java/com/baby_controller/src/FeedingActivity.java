package com.baby_controller.src;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.baby_controller.R;
import com.google.firebase.FirebaseApp;

public class FeedingActivity extends AppCompatActivity {
    private final User user;
    RecyclerView recyclerView;

    public FeedingActivity(User user){
        this.user = user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = new RecyclerView(this);
        recyclerView.setOn

    }

}
