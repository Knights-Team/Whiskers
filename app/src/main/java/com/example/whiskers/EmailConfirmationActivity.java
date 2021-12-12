package com.example.whiskers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.core.Amplify;

public class EmailConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_confirmation);


    }
    public void verifyEmail(View view) {
        EditText confirmationCode = findViewById(R.id.verification_code_input);
        String verificationCode = confirmationCode.getText().toString();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("whiskers",MODE_PRIVATE);
        String email = sharedPreferences.getString("email",null);
        Amplify.Auth.confirmSignUp(
                email,
                verificationCode,
                result -> {
                    Log.i("TAG",
                            result.isSignUpComplete() ?
                                    "Confirm signUp succeeded" :
                                    "Confirm sign up not complete");

                    startActivity(new Intent(EmailConfirmationActivity.this, Login.class));
                },
                error -> Log.e("TAG", error.toString())
        );
    }
}