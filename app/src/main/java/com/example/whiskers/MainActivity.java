package com.example.whiskers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.view.View;
import android.widget.Button;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.User;


public class MainActivity extends AppCompatActivity {

    Button getStarted;
    ProgressBar progressBar;
    Handler handler = new Handler();

    final Runnable signedInRunnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(),Empty.class);
            startActivity(intent);
            finish();
        }
    };


    final Runnable notSignedInRunnable = new Runnable() {
        @Override
        public void run() {
            progressBar.setVisibility(View.GONE);
            getStarted.setVisibility(View.VISIBLE);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getStarted = findViewById(R.id.getStartedButton);
        progressBar = findViewById(R.id.progressBar);
        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());
            Log.i("Amplify", "Initialized Amplify");
        } catch (AmplifyException failure) {
            Log.e("Amplify", "Could not initialize Amplify", failure);
        }
        Amplify.Auth.fetchAuthSession(
                result -> {
                    if (result.isSignedIn()) handler.post(signedInRunnable);

                     else handler.post(notSignedInRunnable);
                },
                error -> Log.e("Amplify", error.toString())
        );




    }

    public void getStarted(View view) {
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }
}