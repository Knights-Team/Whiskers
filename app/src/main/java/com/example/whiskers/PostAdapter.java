package com.example.whiskers;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Post;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    URL url2;
    List<Post> posts = new ArrayList<>();
Context context;
    public PostAdapter(List<Post> posts,Context context) {
        this.posts = posts;
        this.context=context;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.post = posts.get(position);

        ImageView image = holder.itemView.findViewById(R.id.fragImage);
        TextView title = holder.itemView.findViewById(R.id.fragTitle);
        TextView location = holder.itemView.findViewById(R.id.fragLocation);
        Button goToDetails =holder.itemView.findViewById(R.id.detailsBtn);
        String fileName =holder.post.getImage();



        Amplify.Storage.getUrl(
                fileName,
                result -> {
                    Log.i("MyAmplifyApp", "Successfully generated: " + result.getUrl());
                    url2= result.getUrl();

                },
                error -> Log.e("MyAmplifyApp", "URL generation failure", error)
        );
        Amplify.Storage.downloadFile(
                fileName,
                new File(this.context.getFilesDir() +"/"+ fileName),
                result -> {
                    Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getPath());
                    String fileType = null;

                    try {
                        fileType = Files.probeContentType(result.getFile().toPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (fileType.split("/")[0].equals("image")){
                        image.setImageBitmap(BitmapFactory.decodeFile(result.getFile().getPath()));
                    }
//                    else {
//                        String linkedText = String.format("<a href=\"%s\">download File</a> ", uri);
//                        TextView test = findViewById(R.id.imgSrc);
//                        test.setText(Html.fromHtml(linkedText, HtmlCompat.FROM_HTML_MODE_LEGACY));
//                        test.setMovementMethod(LinkMovementMethod.getInstance());
//                    }
                },
                error -> Log.e("MyAmplifyApp",  "Download Failure ",error)
        );
        title.setText(holder.post.getTitle());
        location.setText(holder.post.getLocation());

        goToDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToDetailsPage=new Intent(view.getContext(),DetailsPage.class);
                goToDetailsPage.putExtra("image",holder.post.getImage());
                goToDetailsPage.putExtra("title",holder.post.getTitle());
                goToDetailsPage.putExtra("location",holder.post.getLocation());
                goToDetailsPage.putExtra("description",holder.post.getDescription());
                goToDetailsPage.putExtra("id",holder.post.getId());

                view.getContext().startActivity(goToDetailsPage);

            }
        });

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


}
