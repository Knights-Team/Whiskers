package com.example.whiskers;

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

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Post;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class LandingPage extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    MaterialToolbar topAppBar;
    List<Post> posts = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        FloatingActionButton setting = findViewById(R.id.floating_action_button);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSetting = new Intent(LandingPage.this, AddPost.class);
                startActivity(goToSetting);
            }
        });


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

        PostAdapter adapter = new PostAdapter(posts,this);
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
                    Toast.makeText(this,"Cannot refresh data", Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
        );
    }
}