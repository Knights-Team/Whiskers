package com.example.whiskers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.amplifyframework.datastore.generated.model.Post;

import java.util.ArrayList;
import java.util.List;

public class LandingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        List<Post> posts = new ArrayList<>();
        posts.add(Post.builder().location("aaaa").build());
        posts.add(Post.builder().location("wwww").build());
        posts.add(Post.builder().location("wwww").build());
        posts.add(Post.builder().location("wwww").build());
        posts.add(Post.builder().location("wwww").build());
        posts.add(Post.builder().location("wwww").build());
        posts.add(Post.builder().location("wwww").build());

        PostAdapter adapter = new PostAdapter(posts);
        RecyclerView recyclerView = findViewById(R.id.landView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}