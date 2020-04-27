package com.example.newhangman;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newhangman.R;

class resultActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView textView= (TextView) findViewById(R.id.resultlabel);
        TextView totalScoreLabel = ( TextView) findViewById(R.id.totalScoreLabel);

        //int score = 0;


            //Intent intent = getIntent();
            //textView.setText("You have "+intent.getStringExtra("tries") + "tries left");

        }
    }





