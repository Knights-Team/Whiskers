package com.example.whiskers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Post;
import com.amplifyframework.datastore.generated.model.User;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Register extends AppCompatActivity {
   String Posts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button signup=(Button) findViewById(R.id.logInButton);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText userNameID=findViewById(R.id.registerUsername);
                TextInputEditText passwordID=findViewById(R.id.registerPassword);
                TextInputEditText emailID=findViewById(R.id.registerEmail);
                TextInputEditText fullNameID=findViewById(R.id.registerName);
                TextInputEditText phoneNumberID=findViewById(R.id.registerPhoneNumber);
                CheckBox volunteerID=findViewById(R.id.checkBox);

                String userName = userNameID.getText().toString();
                String password = passwordID.getText().toString();
                String email = emailID.getText().toString();
                String fullName = fullNameID.getText().toString();
                String phoneNumber = phoneNumberID.getText().toString();
                String volunteer = volunteerID.getText().toString();
                newUserInAuth(email,password);
                newUser(userName,email,fullName,phoneNumber);

            }
        });



    }
    private void newUser(String userName,String email,String fullName,String phoneNumber ){
        User item = User.builder()
                .userName(userName)
                .email(email)
                .fullName(fullName)
                .phoneNumber(phoneNumber)
                .volunteer(true)
                .build();
        Amplify.DataStore.save(
                item,
                success -> Log.i("Amplify", "Saved item: " + success.item().getUserName()),
                error -> Log.e("Amplify", "Could not save item to DataStore", error)
        );
//        Toast.makeText(getApplicationContext(),  "submitted!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Register.this, EmailConfirmationActivity.class));
//        finish();
    }
    private void newUserInAuth(String email, String password){
        AuthSignUpOptions options = AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), email)
                .build();
        Amplify.Auth.signUp(email, password, options,
                result -> {
                    Log.i("","Result: " + result.toString());
                },
                error -> Log.e("", "Sign up failed", error)
        );
//        Toast.makeText(getApplicationContext(),  "submitted!", Toast.LENGTH_SHORT).show();
//        finish();

    }
}

