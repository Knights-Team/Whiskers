package com.example.whiskers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button getStarted = findViewById(R.id.getStartedButton);
        ProgressBar progressBar = findViewById(R.id.progressBar);
    }

    public void getStarted(View view) {
        Log.i("MYApp", "The button is clicked");
    }
}