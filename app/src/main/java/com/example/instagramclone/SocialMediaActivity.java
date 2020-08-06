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

import com.google.android.material.tabs.TabLayout;
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
            ParseUser.getCurrentUser().logOut();
            finish();
            Intent i= new Intent(SocialMediaActivity.this,SignUp.class);
            startActivity(i);
        }
        if(item.getItemId()==R.id.delete){
            try{
                ParseUser.getCurrentUser().delete();
                ParseUser.getCurrentUser().logOut();
                finish();
                Toast.makeText(this,"Account successfully deleted",Toast.LENGTH_SHORT).show();
                Intent i= new Intent(SocialMediaActivity.this,SignUp.class);
                startActivity(i);
            }
            catch (Exception e){
                e.printStackTrace();
            }
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