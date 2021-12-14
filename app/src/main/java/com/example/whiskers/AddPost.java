package com.example.whiskers;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Post;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
@RequiresApi(api = Build.VERSION_CODES.Q)
public class AddPost extends AppCompatActivity {
    String fileName;
    Uri dataUri;
    private static final int CODE_REQUEST =55 ;
    private static final String TAG = "upload";
    private String fileType;
    private File uploadFile;
    EditText new_post_title;
    EditText new_post_desc;
    String imgSrc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        AuthUser authUser = Amplify.Auth.getCurrentUser();
        String id =authUser.getUserId();
        Button addPost=findViewById(R.id.post_btn);
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText new_post_title=findViewById(R.id.new_post_title);
                EditText new_post_desc=findViewById(R.id.new_post_desc);
                String getinputTitle = new_post_title.getText().toString();
                String getinputDec = new_post_desc.getText().toString();
//              uploadFile();
                Post post = Post.builder()
                .title(getinputTitle)
                        .location("amman")
                        .description(getinputDec)
//                       .image(fileName)
                        .userId(id)
                        .build();
                saveToAPI(post);
            }
        });
        Button pickFileButton = findViewById(R.id.upload);
        pickFileButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  pickFile();
                                                  Toast.makeText(AddPost.this, "pick", Toast.LENGTH_SHORT).show();

                                              }

                                          }

        );
    }










    public void saveToAPI(Post post){
        Amplify.API.mutate(ModelMutation.create(post),
                success -> Log.i("Tutorial", "Saved item: " + success.getData()),
                error -> Log.e("Tutorial", "Could not save item to API", error)
        );
    }
    @SuppressLint("IntentReset")
    private  void  pickFile(){
        @SuppressLint("IntentReset") Intent selecteFile = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selecteFile.setType(("*/*"));
        selecteFile=Intent.createChooser(selecteFile,"Select File");
        startActivityForResult(selecteFile,1234);

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requesstCode,int resultCode, @Nullable Intent data) {
        super.onActivityResult(requesstCode, resultCode, data);
        assert data != null;
        dataUri = data.getData();
        Uri uri = data.getData();

        String src = uri.getPath();
        fileType = getContentResolver().getType(uri);
        fileName = new SimpleDateFormat("yyMMddHHmmssZ").format(new Date())+"." + fileType.split("/")[1];
        File source = new File(src);
        String file = uri.getLastPathSegment();
        File destination = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/CustomFolder"+file);
        uploadFile = new File(getApplicationContext().getFilesDir(), "uploadFile");

        try {
            InputStream inputStream = getContentResolver().openInputStream(data.getData());
            FileUtils.copy(inputStream, new FileOutputStream(uploadFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    private void uploadFile(){
//        Amplify.Storage.uploadFile(
//                fileName,
//                uploadFile,
//                success -> {
//                    Log.i(TAG, "uploadFileToS3: succeeded " + success.getKey());
//                },
//                error -> {
//                    Log.e(TAG, "uploadFileToS3: failed " + error.toString());
//                }
//        );
//    }

}