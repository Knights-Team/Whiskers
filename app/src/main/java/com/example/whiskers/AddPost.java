package com.example.whiskers;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class AddPost extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private static final int REQUEST_CODE_LOCATION_PERMISSION=1;
    String fileName;
    Uri dataUri;
    private static final int CODE_REQUEST =55 ;
    private static final String TAG = "upload";
    private String fileType;
    private File uploadFile;
    private String location;
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

        Button locationButton = findViewById(R.id.getLocation);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(
                            AddPost.this,
                            new String[]{ Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION
                    );
                }else{
                    locationButton.setText(AddPost.this.location);
                }
            }


        });
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
                        .location(location != null ? location: "No Location")
                        .description(getinputDec)
//                       .image(fileName)
                        .userId(id)
                        .build();
                saveToAPI(post);
                finish();
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




        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some rare situations this can be null.
                    try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses = null;
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        this.location = addresses.get(0).getAddressLine(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });
    }










    public void saveToAPI(Post post){
        Amplify.DataStore.save(
                post,
                success -> Log.i("Amplify", "Saved item: " + success.item().getTitle()),
                error -> Log.e("Amplify", "Could not save item to DataStore", error)
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