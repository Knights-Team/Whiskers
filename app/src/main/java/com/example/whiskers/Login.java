package com.example.whiskers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.User;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button login = findViewById(R.id.logInButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText userNameID = findViewById(R.id.LoginUsername);
                TextInputEditText passwordID = findViewById(R.id.LoginPassword);
                String email = userNameID.getText().toString();
                String password = passwordID.getText().toString();

                signIn(email,password);
            }
        });

    }


    private void signIn(String email, String password) {
        Amplify.Auth.signIn(
                email,
                password,
                result -> {
                    Log.i("AuthQuickstart", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");

                },
                error -> Log.e("AuthQuickstart", error.toString())
        );
        startActivity(new Intent(Login.this, MainActivity.class));

    }
    public void toSignUp(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}