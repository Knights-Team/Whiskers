package com.example.whiskers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.User;

public class editProfile extends AppCompatActivity {

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
                User updatedItem = User.builder()
                        .email(email)
                        .fullName(name)
                        .phoneNumber(phone)
                        .volunteer(false)
                        .build();
                Amplify.DataStore.save(
                        updatedItem,
                        success -> Log.i("Amplify", "Item updated: " + success.item().getId()),
                        error -> Log.e("Amplify", "Could not save item to DataStore", error)
                );
                startActivity(new Intent(editProfile.this, Profile.class));
                finish();
            }
        });


    }
}