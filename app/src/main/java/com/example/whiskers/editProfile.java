package com.example.whiskers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.User;
import com.google.android.material.textfield.TextInputEditText;

public class editProfile extends AppCompatActivity {
  String myId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Button update = (Button) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView Email=findViewById(R.id.registerEmail);
                TextView Name=findViewById(R.id.registerName);
                TextView PhoneNumber=findViewById(R.id.registerPhoneNumber);
                String email=Email.getText().toString();
                String name=Name.getText().toString();
                String phone=PhoneNumber.getText().toString();
                Amplify.DataStore.query(User.class, Where.id(myId),
                        matches -> {
                            if (matches.hasNext()) {
                                User original = matches.next();
                                User updatedItem =original.copyOfBuilder()
                                        .email(email)
                                        .fullName(name)
                                        .phoneNumber(phone)
                                        .volunteer(true)
                                        .build();
                                Amplify.DataStore.save(updatedItem,
                                        updated -> Log.i("MyAmplifyApp", "Updated a post."),
                                        failure -> Log.e("MyAmplifyApp", "Update failed.", failure)
                                );
                            }
                        },
                        failure -> Log.e("MyAmplifyApp", "Query failed.", failure)

                );
                startActivity(new Intent(editProfile.this, Profile.class));
                finish();
            }
        });


    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(editProfile.this);
//        String myName = sharedPreferences.getString("emaill" , "the email not provided");
//        TextInputEditText textView = findViewById(R.id.registerEmail);
//        textView.setText(myName);
//    }
}