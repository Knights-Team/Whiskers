package com.example.whiskers;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    List<Post> posts = new ArrayList<>();

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public Post post;
        public View itemView;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_card, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.post = posts.get(position);

       // TextView image = holder.itemView.findViewById(R.id.fragImage);
        TextView title = holder.itemView.findViewById(R.id.fragTitle);
        TextView location = holder.itemView.findViewById(R.id.fragLocation);
        Button goToDetails =holder.itemView.findViewById(R.id.detailsBtn);

       // image.setText(holder.post.getTitle());
        title.setText(holder.post.getTitle());
        location.setText(holder.post.getLocation());

        goToDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToDetailsPage=new Intent(view.getContext(),DetailsPage.class);
                //goToDetailsPage.putExtra("image",holder.post.getImage());
                goToDetailsPage.putExtra("title",holder.post.getTitle());
                goToDetailsPage.putExtra("location",holder.post.getLocation());
                goToDetailsPage.putExtra("description",holder.post.getDescription());

                view.getContext().startActivity(goToDetailsPage);

            }
        });

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


}
