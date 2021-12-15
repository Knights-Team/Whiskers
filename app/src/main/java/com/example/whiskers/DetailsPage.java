package com.example.whiskers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Post;

import java.util.ArrayList;
import java.util.List;

public class DetailsPage extends AppCompatActivity {

    String postId;
    public Handler handler= new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_page);
        TextView textView1 = findViewById(R.id.textView5);
        TextView textView2 = findViewById(R.id.textView6);
        TextView textView3 = findViewById(R.id.textView7);
        Button help = findViewById(R.id.help);

        String postTitle = getIntent().getExtras().get("title").toString();
        textView1.setText(postTitle);

        String postLocation = getIntent().getExtras().get("location").toString();
        textView2.setText(postLocation);

        String postDescription = getIntent().getExtras().get("description").toString();
        textView3.setText(postDescription);

         postId = getIntent().getExtras().get("id").toString();


        handler = new Handler(Looper.getMainLooper(),
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message message) {

                        Intent landingPage=new Intent(DetailsPage.this,LandingPage.class);
                        Toast.makeText(getApplicationContext(),"help completed successfully",Toast.LENGTH_LONG).show();
                        startActivity(landingPage);
                        finish();
                        return false;
                    }
                });



        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Amplify.DataStore.query(Post.class, Where.id(postId),
                        matches -> {
                            if (matches.hasNext()) {
                                Post post = matches.next();
                                Amplify.DataStore.delete(post,
                                        deleted -> Log.i("MyAmplifyApp", "Deleted a post."),
                                        failure -> Log.e("MyAmplifyApp", "Delete failed.", failure)
                                );
                            }
                            handler.sendEmptyMessage(2);

                        },
                        failure -> Log.e("MyAmplifyApp", "Query failed.", failure)
                );
            }


        });

    }


}


