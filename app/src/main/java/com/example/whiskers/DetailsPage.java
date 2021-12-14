package com.example.whiskers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DetailsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_page);
        TextView textView1 = findViewById(R.id.textView5);
        TextView textView2 = findViewById(R.id.textView6);
        TextView textView3 = findViewById(R.id.textView7);

        String postTitle = getIntent().getExtras().get("title").toString();
        textView1.setText(postTitle);

        String postLocation = getIntent().getExtras().get("location").toString();
        textView2.setText(postLocation);

        String postDescription = getIntent().getExtras().get("description").toString();
        textView3.setText(postDescription);
    }
}