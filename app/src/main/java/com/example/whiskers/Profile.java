package com.example.whiskers;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.User;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Array;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Button logout=(Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amplify.Auth.signOut(
                        () -> Log.i("AuthQuickstart", "Signed out successfully"),
                        error -> Log.e("AuthQuickstart", error.toString())
                );
                Amplify.Auth.signOut(
                        AuthSignOutOptions.builder().globalSignOut(true).build(),
                        () -> Log.i("AuthQuickstart", "Signed out globally"),
                        error -> Log.e("AuthQuickstart", error.toString())
                );
                startActivity(new Intent(Profile.this, LandingPage.class));
                finish();
            }
        });
        Button editProfile=(Button) findViewById(R.id.editProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, editProfile.class));


            }
        });

        Amplify.DataStore.query(
                User.class,
                items -> {
                    while (items.hasNext()) {
                        User item = items.next();
                        Log.i("Amplify", "Id " + item.getId());
                        String email=item.getEmail();
                        String fullName=item.getFullName();
                        String phoneNumber=item.getPhoneNumber();
                        String volunteer=item.getVolunteer().toString();
                        System.out.println(email);
                        System.out.println(fullName);
                        System.out.println(phoneNumber);
                        System.out.println(volunteer);
                    }
                },
                failure -> Log.e("Amplify", "Could not query DataStore", failure)
        );
        TextView fullNameID=(TextView) findViewById(R.id.tv_name);
        fullNameID.setText("Full Name");
        TextView emailID=(TextView) findViewById(R.id.emailid);
        emailID.setText("email@gmail.com");
        TextView phoneNumberID=(TextView) findViewById(R.id.phoneNumber);
        phoneNumberID.setText("+1-202-555-0157");
        TextView volnteerID=(TextView) findViewById(R.id.isVolunteer);
        volnteerID.setText("false");

    }

}
