package com.example.whiskers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.core.Amplify;
import com.google.android.material.textfield.TextInputLayout;

public class EmailConfirmationActivity extends AppCompatActivity {

    Handler handler = new Handler();
    TextInputLayout verficConfirm;
    final Runnable SignUpRunnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(),Empty.class);
            startActivity(intent);
            finish();
        }
    };



    final Runnable notSignUpRunnable = new Runnable() {

        @Override
        public void run() {
            verficConfirm.setErrorEnabled(true);
            verficConfirm.setError("your code is not correct");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_confirmation);
        verficConfirm = findViewById(R.id.verficationConfirm);

    }
    public void verifyEmail(View view) {
        EditText confirmationCode = findViewById(R.id.verification_code_input);
        String verificationCode = confirmationCode.getText().toString();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("whiskers",MODE_PRIVATE);
        String email = sharedPreferences.getString("email",null);
        Amplify.Auth.confirmSignUp(
                email,
                verificationCode,


                result -> handler.post(SignUpRunnable),
                error -> handler.post(notSignUpRunnable));
          
    }
}