package com.example.whiskers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.amplifyframework.datastore.generated.model.Post;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class LandingPage extends AppCompatActivity {
    MaterialToolbar topAppBar;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        List<Post> posts = new ArrayList<>();
        posts.add(Post.builder().location("amman").title("sick cat").description("cat need help").build());
        posts.add(Post.builder().location("wwww").build());
        posts.add(Post.builder().location("wwww").build());
        posts.add(Post.builder().location("wwww").build());
        posts.add(Post.builder().location("wwww").build());
        posts.add(Post.builder().location("wwww").build());
        posts.add(Post.builder().location("wwww").build());
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
}