package com.example.whiskers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextInputEditText textInputEditText = findViewById(R.id.LoginUsername);

    }

    public void toSignUp(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}