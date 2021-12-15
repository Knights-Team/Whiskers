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

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.User;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;

public class DetailsPage extends AppCompatActivity {
    private String fileName;
    User user;
    private static final String TAG = "FCM";
    private static final String urlFcm = "https://fcm.googleapis.com/fcm/send";
    private RequestQueue requestQueue;
    private URL url;
    @RequiresApi(api = Build.VERSION_CODES.O)
    String postId;
    public Handler handler = new Handler();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_page);

        Intent intent = getIntent();
        requestQueue = Volley.newRequestQueue(this);
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
                    url = result.getUrl();

                },
                error -> Log.e("MyAmplifyApp", "URL generation failure", error)
        );

        ImageView imageView = findViewById(R.id.myImage);
        Amplify.Storage.downloadFile(
                fileName,
                new File(getApplicationContext().getFilesDir() + "/" + fileName),

                result -> {
                    Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getPath());
                    String fileType = null;

                    try {
                        fileType = Files.probeContentType(result.getFile().toPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (fileType.split("/")[0].equals("image")) {
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
                error -> Log.e("MyAmplifyApp", "Download Failure ", error)
        );
        postId = getIntent().getExtras().get("id").toString();


        handler = new Handler(Looper.getMainLooper(),
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message message) {

                        Intent landingPage = new Intent(DetailsPage.this, LandingPage.class);
                        Toast.makeText(getApplicationContext(), "help completed successfully", Toast.LENGTH_LONG).show();
                        startActivity(landingPage);
                        finish();
                        return false;
                    }
                });


        AuthUser authUser = Amplify.Auth.getCurrentUser();
        String email = authUser.getUsername();
        Amplify.DataStore.query(
                User.class, User.EMAIL.contains(email),
                items -> {
                    while (items.hasNext()) {
                        user = items.next();
                        Log.i("Amplify", "Id " + user.getId());
                    }
                    help.setVisibility(user.getVolunteer() ? View.VISIBLE : View.GONE);
                },
                failure -> Log.e("Amplify", "Could not query DataStore", failure)
        );
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Amplify.DataStore.query(Post.class, Where.id(postId),
                        matches -> {
                            if (matches.hasNext()) {
                                Post post = matches.next();
                                Amplify.DataStore.delete(post,
                                        deleted -> {
                                            try {
                                                JSONObject main = new JSONObject();
                                                main.put("to", "/topics/" + post.getUserId());
                                                JSONObject scnd = new JSONObject();
                                                scnd.put("title", "Animal Saved");
                                                scnd.put("body", "Your Animal " + post.getTitle() + " is now in secure hand (ɔ◔‿◔)ɔ♥️");
                                                main.put("notification", scnd);

                                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlFcm, main, response -> {

                                                }, error -> {

                                                }) {
                                                    @Override
                                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                                        Map<String, String> header = new HashMap<>();
                                                        header.put("content-type", "application/json");
                                                        header.put("authorization", "key=AAAACRlVeiw:APA91bGbzNnr14bbIgqj0H1BuidSv-3vg9P_gD6A2qvpeTWEeVIoNbtYCb_tBVnSAE5DquKawef5ff9bPeGRE5j1w5X-_QkqvNss6byesuBw2YZV1WVDm9rt-jG26myZxXZqe0NkAtri");
                                                        return header;
                                                    }
                                                };

                                                requestQueue.add(jsonObjectRequest);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        },
                                        failure -> Log.e("MyAmplifyApp", "Delete failed.", failure)
                                );
//                                Amplify.API.mutate(ModelMutation.delete(post),
//                                        response -> Log.i("MyAmplifyApp", "Todo with id: " ),
//                                        error -> Log.e("MyAmplifyApp", "Create failed", error)
//                                );
                            }
                            handler.sendEmptyMessage(2);

                        },
                        failure -> Log.e("MyAmplifyApp", "Query failed.", failure)
                );
            }


        });

    }


}


