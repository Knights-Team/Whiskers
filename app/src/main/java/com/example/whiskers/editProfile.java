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
import android.widget.Toast;

import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Text;

public class editProfile extends AppCompatActivity {
  String myId;
  User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Button update = (Button) findViewById(R.id.update);
        TextView Email=findViewById(R.id.registerEmail);
        TextView Name=findViewById(R.id.registerName);
        TextView PhoneNumber=findViewById(R.id.registerPhoneNumber);
        TextView Password = findViewById(R.id.registerPassword);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("User",MODE_PRIVATE);

        String email=sharedPreferences.getString("email","Unknown");
        String name=sharedPreferences.getString("fullName", "Unknown");
        String phone=sharedPreferences.getString("phoneNumber", "Unknown");
        myId = sharedPreferences.getString("userID","Unknown");

        Email.setText(email);
        Name.setText(name);
        PhoneNumber.setText(phone);
        Password.setText("**********");

        Email.setEnabled(false);
        Password.setEnabled(false);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Amplify.DataStore.query(
//                        User.class,User.EMAIL.contains(email),
//                        items -> {
//                            while (items.hasNext()) {
//                                user = items.next();
//                                Log.i("Amplify", "Id " + user.getId());
//                            }
                            Amplify.DataStore.query(User.class, Where.id(myId),
                                    matches -> {
                                        if (matches.hasNext()) {
                                            String newEmail = Email.getText().toString();
                                            String newName = Name.getText().toString();
                                            String newPhone = PhoneNumber.getText().toString();
                                            User original = matches.next();
                                            User updatedItem =original.copyOfBuilder()
                                                    .email(newEmail)
                                                    .fullName(newName)
                                                    .phoneNumber(newPhone)
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
//                        },
//                        failure -> Log.e("Amplify", "Could not query DataStore", failure)
//                );

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