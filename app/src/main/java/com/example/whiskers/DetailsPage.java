package com.example.whiskers;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

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

import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Post;

import java.util.ArrayList;
import java.util.List;

import com.amplifyframework.core.Amplify;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class DetailsPage extends AppCompatActivity {
    private String fileName;
    private URL url;
    @RequiresApi(api = Build.VERSION_CODES.O)
    String postId;
    public Handler handler= new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_page);

        Intent intent = getIntent();

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


        fileName = intent.getExtras().getString("image");

        Amplify.Storage.getUrl(
                fileName,
                result -> {
                    Log.i("MyAmplifyApp", "Successfully generated: " + result.getUrl());
                    url= result.getUrl();

                },
                error -> Log.e("MyAmplifyApp", "URL generation failure", error)
        );

        ImageView imageView = findViewById(R.id.myImage);
        Amplify.Storage.downloadFile(
                fileName,
                new File(getApplicationContext().getFilesDir() +"/"+ fileName),

                result -> {
                    Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getPath());
                    String fileType = null;

                    try {
                        fileType = Files.probeContentType(result.getFile().toPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (fileType.split("/")[0].equals("image")){
                        imageView.setImageBitmap(BitmapFactory.decodeFile(result.getFile().getPath()));
                    }
//                    else {
//                        String linkedText = String.format("<a href=\"%s\">download File</a> ", url);
//
//                        TextView test = findViewById(R.id.taskLink);
//                        test.setText(Html.fromHtml(linkedText, HtmlCompat.FROM_HTML_MODE_LEGACY));
//                        test.setMovementMethod(LinkMovementMethod.getInstance());
//                    }
                },
                error -> Log.e("MyAmplifyApp",  "Download Failure ",error)
        );
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


