package com.example.whiskers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LandingPage extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "FCM";
    private static final String url = "https://fcm.googleapis.com/fcm/send";
    MaterialToolbar topAppBar;
    List<Post> posts = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    private RequestQueue requestQueue;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        requestQueue = Volley.newRequestQueue(this);
//        FirebaseOptions options = null;
//        try {
//            options = FirebaseOptions.builder()
//                    .setCredentials(GoogleCredentials.getApplicationDefault())
//                    .build();
//            FirebaseApp.initializeApp(options);
//            Log.d(TAG, "Initilize FCM");
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.d(TAG, "Not FCM");
//        }


        FloatingActionButton setting = findViewById(R.id.floating_action_button);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSetting = new Intent(LandingPage.this, AddPost.class);
                startActivity(goToSetting);
            }
        });
        String topic = "weather";

        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(task -> {
                    String msg = "Subs";
                    if (!task.isSuccessful()) {
                        msg = "Subs failed";
                    }
                    Log.d(TAG, msg);
                    Toast.makeText(LandingPage.this, msg, Toast.LENGTH_SHORT).show();
                });



        try {
            JSONObject main = new JSONObject();
            main.put("to", "/topics/" + topic);
            JSONObject scnd = new JSONObject();
            scnd.put("title", "Just Title");
            scnd.put("body", "BBBBBB");
            main.put("notification", scnd);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, main, response -> {

            }, error -> {

            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAACRlVeiw:APA91bGbzNnr14bbIgqj0H1BuidSv-3vg9P_gD6A2qvpeTWEeVIoNbtYCb_tBVnSAE5DquKawef5ff9bPeGRE5j1w5X-_QkqvNss6byesuBw2YZV1WVDm9rt-jG26myZxXZqe0NkAtri                                                                                                                                            ");
                    return header;
                }
            };

            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        Amplify.DataStore.query(Post.class,
                result -> {
                    posts.clear();
                    while (result.hasNext()) {
                        Post post = result.next();
                        posts.add(post);
                    }
                },
                error -> Log.e("Amplify", "There was an error in getting data from data store")
        );
        swipeRefreshLayout = findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        topAppBar = findViewById(R.id.topAppBar);

        PostAdapter adapter = new PostAdapter(posts);
        RecyclerView recyclerView = findViewById(R.id.landView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        topAppBar.setOnMenuItemClickListener(menuItem -> {
                    if (menuItem.getItemId() == R.id.profileIcon) {
                        startActivity(new Intent(this, Profile.class));
                        return true;
                    }
                    return false;
                }

        );


    }

    @Override
    public void onRefresh() {
        Amplify.DataStore.query(Post.class,
                result -> {
                    posts.clear();
                    while (result.hasNext()) {
                        Post post = result.next();
                        posts.add(post);
                    }
                    swipeRefreshLayout.setRefreshing(false);
                },
                error -> {
                    Toast.makeText(this, "Cannot refresh data", Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
        );
    }
}