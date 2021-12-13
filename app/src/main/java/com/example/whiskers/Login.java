package com.example.whiskers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class Login extends AppCompatActivity {

    Handler handler = new Handler();
    TextInputLayout passwordLayout;

    final Runnable logInRunnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(),Empty.class);
            startActivity(intent);
            finish();
        }
    };


    final Runnable notLogInRunnable = new Runnable() {

        @Override
        public void run() {
           // confirm.setText("Your email or password is not valid");
            passwordLayout.setErrorEnabled(true);
            passwordLayout.setError("your email or password is not valid");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        passwordLayout = findViewById(R.id.usernameTextFieldLogin);

        Button login = findViewById(R.id.logInButton);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText userNameID = findViewById(R.id.LoginUsername);
                TextInputEditText passwordID = findViewById(R.id.LoginPassword);
                String email = userNameID.getText().toString();
                String password = passwordID.getText().toString();

                signIn(email,password);
                //to not show the error msg while the user still fill the info in the edit text
                userNameID.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                          passwordLayout.setErrorEnabled(false);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

//                if (email.isEmpty() || !email.contains("@") ){
//
//                    userNameID.setError("Your email is not valid");
//
//                }else if(password.isEmpty() || password.length()<8){
//                    passwordID.setError("password must be 8 character");
//                }
            }
        });

    }


    private void signIn(String email, String password) {
        Amplify.Auth.signIn(
                email,
                password,
                result -> handler.post(logInRunnable),
                error -> handler.post(notLogInRunnable));


    }
    public void toSignUp(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}