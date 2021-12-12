package com.example.whiskers;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amplifyframework.auth.AuthUserAttributeKey;

import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

import com.amplifyframework.datastore.generated.model.User;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button signup = (Button) findViewById(R.id.logInButton);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText userNameID = findViewById(R.id.registerUsername);
                TextInputEditText passwordID = findViewById(R.id.registerPassword);
                TextInputEditText emailID = findViewById(R.id.registerEmail);
                TextInputEditText fullNameID = findViewById(R.id.registerName);
                TextInputEditText phoneNumberID = findViewById(R.id.registerPhoneNumber);
                String userName = Objects.requireNonNull(userNameID.getText()).toString();
                String password = Objects.requireNonNull(passwordID.getText()).toString();
                String email = Objects.requireNonNull(emailID.getText()).toString();
                String fullName = Objects.requireNonNull(fullNameID.getText()).toString();
                String phoneNumber = Objects.requireNonNull(phoneNumberID.getText()).toString();
                newUserInAuth(email, password);
                newUser(userName, email, fullName, phoneNumber);

            }


        });

    }

    private void newUser(String userName, String email, String fullName, String phoneNumber) {
        User item = User.builder()
                .userName(userName)
                .email(email)
                .fullName(fullName)
                .phoneNumber(phoneNumber)
                .volunteer(false)
                .build();
        Amplify.DataStore.save(
                item,
                success -> Log.i("Amplify", "Saved item: " + success.item().getUserName()),
                error -> Log.e("Amplify", "Could not save item to DataStore", error)
        );
        startActivity(new Intent(Register.this, EmailConfirmationActivity.class));
    }

    private void newUserInAuth(String email, String password) {
        AuthSignUpOptions options = AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), email)
                .build();
        Amplify.Auth.signUp(email, password, options,
                result -> {
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("whiskers", MODE_PRIVATE);
                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", email);
                    editor.apply();
                },
                error -> Log.e("Amplify", "Sign up failed", error)
        );

    }
}

