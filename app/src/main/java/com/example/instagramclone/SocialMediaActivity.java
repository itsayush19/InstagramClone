package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import java.util.UUID;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

public class SocialMediaActivity extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;
    private FirebaseAuth mAuth;
    private String imagIdentifier;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);
        setTitle("YOUR FEED");
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager=findViewById(R.id.viewpager);
        tabAdapter=new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);
        tabLayout=findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager,false);
        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.postImageItem){
            if(Build.VERSION.SDK_INT>=23&& ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String [] {Manifest.permission.READ_EXTERNAL_STORAGE},3000);
            }
            else{
                captureImage();
            }
        }
        if(item.getItemId()==R.id.logOut){
            mAuth.signOut();
            finish();
            Intent i= new Intent(SocialMediaActivity.this,SignUp.class);
            startActivity(i);
        }
        if(item.getItemId()==R.id.delete){
            Toast.makeText(this,"will do it later",Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void captureImage() {
        Intent i= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,4000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==4000&&resultCode==RESULT_OK&&data!=null){
            try{
                /*
                Uri capturedImage= data.getData();
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),capturedImage);
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                byte[] bytes=byteArrayOutputStream.toByteArray();
                ParseFile parseFile=new ParseFile("img.png",bytes);
                ParseObject parseObject=new ParseObject("Photo");
                parseObject.put("picture",parseFile);
                parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                final ProgressDialog progressDialog=new ProgressDialog(getApplicationContext());
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Toast.makeText(SocialMediaActivity.this,"Picture Uploaded",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(SocialMediaActivity.this,"Picture Not Uploaded",Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });*/
                Uri capturedImage= data.getData();
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),capturedImage);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] dat = baos.toByteArray();
                imagIdentifier= UUID.randomUUID()+".png";
                UploadTask uploadTask= FirebaseStorage.getInstance().getReference().child("my_images").child(imagIdentifier).putBytes(dat);
                final ProgressDialog progressDialog=new ProgressDialog(getApplicationContext());
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SocialMediaActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String imageLink= FirebaseStorage.getInstance().getReference().child("my_images").child(imagIdentifier).getDownloadUrl().toString();
                        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("posts").push().setValue(imageLink);
                        Toast.makeText(SocialMediaActivity.this,"Uploaded",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });


            }
            catch(Exception e ){
                e.printStackTrace();
            }
        }
        else{
            captureImage();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
/*
  private void selectImage(){
        if(Build.VERSION.SDK_INT<23){
            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,1000);
        }
        else if (Build.VERSION.SDK_INT>=23){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1000);
            }
            else{
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1000);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            selectImage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000&&resultCode==RESULT_OK&&data!=null){
            Uri chosenImage=data.getData();
            try{
                bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),chosenImage);
                postImage.setImageBitmap(bitmap);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void uploadImage(){
        // Get the data from an ImageView as bytes
        if(bitmap!=null){
            postImage.setDrawingCacheEnabled(true);
            postImage.buildDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();
            imagIdentifier= UUID.randomUUID()+".png";
            UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("my_images").child(imagIdentifier).putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(SocialMediaActivity.this,exception.toString(),Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    Toast.makeText(SocialMediaActivity.this,"Uploaded",Toast.LENGTH_LONG).show();

                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                imageLink=task.getResult().toString();
                            }
                        }
                    });
                }
            });

        }
    }
 */