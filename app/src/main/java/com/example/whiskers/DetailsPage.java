package com.example.whiskers;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class DetailsPage extends AppCompatActivity {
    private String fileName;
    private URL url;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_page);

        Intent intent = getIntent();

        TextView textView1 = findViewById(R.id.textView5);
        TextView textView2 = findViewById(R.id.textView6);
        TextView textView3 = findViewById(R.id.textView7);

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
    }
}