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

import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.User;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Array;

public class Profile extends AppCompatActivity {
    AuthUser authUser = Amplify.Auth.getCurrentUser();
    String authEmail =authUser.getUsername();
    String myId;
    String s1 , s2 , s3;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Button logout=(Button)findViewById(R.id.logout);
        sharedPreferences = getApplicationContext().getSharedPreferences("User", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String location = sharedPreferences.getString("location","No Location");
        TextView locationText = findViewById(R.id.tv_address);
        locationText.setText(location);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amplify.Auth.signOut(
                        () -> {  startActivity(new Intent(Profile.this, MainActivity.class));
                            finish();},
                        error -> Log.e("AuthQuickstart", error.toString())
                );
                Amplify.Auth.signOut(
                        AuthSignOutOptions.builder().globalSignOut(true).build(),
                        () -> Log.i("AuthQuickstart", "Signed out globally"),
                        error -> Log.e("AuthQuickstart", error.toString())
                );

            }
        });
        Button editProfile=(Button) findViewById(R.id.editProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("email",s1);
                editor.putString("fullName",s2);
                editor.putString("phoneNumber",s3);
                editor.putString("userID",myId);
                editor.apply();
                startActivity(new Intent(Profile.this, editProfile.class));


            }
        });
        Button volunteerBtn = findViewById(R.id.becomeAVolunteer);
        volunteerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(myId);
                Amplify.DataStore.query(User.class, Where.id(myId),
                        matches -> {
                            if (matches.hasNext()) {
                                User original = matches.next();
                                User updatedItem =original.copyOfBuilder()
                                        .email(s1)
                                        .fullName(s2)
                                        .phoneNumber(s3)
                                        .volunteer(!original.getVolunteer())
                                        .build();
                                Amplify.DataStore.save(updatedItem,
                                        updated -> Log.i("MyAmplifyApp", "Updated a post."),
                                        failure -> Log.e("MyAmplifyApp", "Update failed.", failure)
                                );
                            }
                        },
                        failure -> Log.e("MyAmplifyApp", "Query failed.", failure)

                );
//                startActivity(new Intent(Profile.this, Profile.class));
                finish();
            }


        });


//        TextView myEmail = findViewById(R.id.emailid);
//        String myEmaill = myEmail.getText().toString();
//        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
//
//        // Creating an Editor object to edit(write to the file)
//        SharedPreferences.Editor myEdit = sharedPreferences.edit();
//        myEdit.putString("emaill" ,myEmaill );



        Amplify.DataStore.query(
        User.class,User.EMAIL.contains(authEmail),
        items -> {
        while (items.hasNext()) {
        User item = items.next();
//        if(item.getEmail().equals(authEmail)){
            myId = item.getId();
            Log.i("Amplify", "Id " + item.getId());
            String email=item.getEmail();
            s1= email;
            String name=item.getFullName();
            s2= name;
            String phone=item.getPhoneNumber();
            s3 = phone;
            Boolean volunteer=item.getVolunteer();
            volunteerBtn.setText(volunteer? "Become Non Volunteer :(": "Become Volunteer");
//            volunteerBtn.setBackgroundColor(volunteer? R.color.black: R.color.light_orange);
            System.out.println(email);
            System.out.println(name);
            System.out.println(phone);
            System.out.println(volunteer);
            showProfile(name ,email ,phone ,volunteer.toString());


        }
        },
        failure -> Log.e("Amplify", "Could not query DataStore", failure)
        );


    }


 private void showProfile(String name , String email , String phone , String volunteer){
     System.out.println(phone);
     System.out.println(name);
     System.out.println(volunteer);
     TextView fullNameID=(TextView) findViewById(R.id.tv_name);
     fullNameID.setText(name);
     TextView emailID=(TextView) findViewById(R.id.emailid);
     emailID.setText(email);
     TextView phoneNumberID=(TextView) findViewById(R.id.phoneNumber);
     phoneNumberID.setText(phone);
     TextView volnteerID=(TextView) findViewById(R.id.isVolunteer);
     volnteerID.setText(volunteer);
 }
}

