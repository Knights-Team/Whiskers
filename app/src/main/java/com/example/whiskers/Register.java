package com.example.whiskers;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.auth.AuthUserAttributeKey;

import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

import com.amplifyframework.datastore.generated.model.User;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button signup = (Button) findViewById(R.id.logInButton);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView passworderr=findViewById(R.id.helperpassword);
                TextView fullnameerr =findViewById(R.id.helperfullname);
                TextView phoneerr =findViewById(R.id.helperphone);
                TextView emailerr=findViewById(R.id.helperemail);
                passworderr.setHint(null);
                fullnameerr.setHint(null);
                phoneerr.setHint(null);
                emailerr.setHint(null);


                TextInputEditText userNameID = findViewById(R.id.registerEmail);
                TextInputEditText passwordID = findViewById(R.id.registerPassword);
                TextInputEditText emailID = findViewById(R.id.registerEmail);
                TextInputEditText fullNameID = findViewById(R.id.registerName);
                TextInputEditText phoneNumberID = findViewById(R.id.registerPhoneNumber);


                if (emailID.getText().toString().trim().equals("")) {
                    emailID.setError("Email is required!");
                    emailerr.setHint("please enter email");
                } else if (!emailID.getText().toString().contains("@") || !emailID.getText().toString().contains(".com") ){
                    emailID.setError("Invalid email");
                    emailerr.setHint("please enter valid email");
                } else if (passwordID.getText().toString().trim().equals("")) {
                    passworderr.setHint("please enter password");
                } else if (isValidPassword(passwordID.getText().toString())== false) {
                    passworderr.setHint("password must contains at least 8 characters,digit,upper case,special character and no white space");
                } else if (fullNameID.getText().toString().trim().equals("")) {
                    fullNameID.setError("fullName is required!");
                    fullnameerr.setHint("please enter your full name");
                } else if (phoneNumberID.getText().toString().trim().equals("")) {
                    phoneNumberID.setError("phone number is required!");
                    phoneerr.setHint("please enter your nember phone");
                } else if (isValidPhoneNumber(phoneNumberID.getText().toString())== false) {
                    phoneerr.setHint("please enter your nember ");
                } else {
                    String userName = userNameID.getText().toString();
                    String password = passwordID.getText().toString();
                    String email =emailID.getText().toString();
                    String fullName = fullNameID.getText().toString();
                    String phoneNumber = phoneNumberID.getText().toString();
                    newUserInAuth(email, password);
                    newUser(userName, email, fullName, phoneNumber);

                }

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
    public static boolean isValidPassword(String password) {

        // Regex to check valid password.
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,16}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the password is empty
        // return false
        if (password == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given password
        // and regular expression.
        Matcher m = p.matcher(password);

        // Return if the password
        // matched the ReGex
        return m.matches();
    }
    public static boolean isValidPhoneNumber(String phone) {

        // Regex to check valid password.
        String regex = "(00962)(7|8|9|)([0-9]{8})";


        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the password is empty
        // return false
        if (phone == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given password
        // and regular expression.
        Matcher m = p.matcher(phone);

        // Return if the password
        // matched the ReGex
        return m.matches();
    }

}

