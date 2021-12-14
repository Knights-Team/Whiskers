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

        // The topic name can be optionally prefixed with "/topics/".


// See documentation on defining a message payload.

//        Message message = Message.builder()
//                .putData("score", "850")
//                .setTopic(topic)
//                .build();
        Bundle bundle = new Bundle();
        bundle.putString("Score", "850");
        bundle.putString("topic", topic);
        RemoteMessage remoteMessage = new RemoteMessage(bundle);
        JSONObject main = new JSONObject();
        try {
            main.put("to", "/topics/" + topic);
            JSONObject scnd = new JSONObject();
            scnd.put("title", "Just Title");
            scnd.put("body", "BBBBBB");
            main.put("notification", scnd);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, main, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
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

// Send a message to the devices subscribed to the provided topic.
//        String response = null;
//        try {
//            response = FirebaseMessaging.getInstance().send(message);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//// Response is a message ID string.
//        Log.d(TAG, "Successfully sent message: " + response);


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