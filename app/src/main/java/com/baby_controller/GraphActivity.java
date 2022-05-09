package com.baby_controller;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;

public class GraphActivity extends AppCompatActivity {
    private TextView babyName;
    private Button history;
    private Button wight;
    GraphView gWight;
    GraphView gHistory;


    public void onCreate(){
        setContentView(R.layout.graph_layout);
        configureButtons();
    }

    private void configureButtons() {
        history = findViewById(R.id.meal_graph_button);
        wight = findViewById(R.id.wight_graph_button);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo start the graph
            }
        });
        wight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo start the graph

            }
        });


    }


}
