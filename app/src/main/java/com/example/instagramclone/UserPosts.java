package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import com.squareup.picasso.Picasso;



public class UserPosts extends AppCompatActivity {
    private Intent i;
    private LinearLayout linearLayout;
    private FirebaseAuth mAuth;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_posts);
        linearLayout=findViewById(R.id.linearLayout);
        mAuth=FirebaseAuth.getInstance();
        i=getIntent();
        final String recivedUsername=i.getStringExtra("username");
        setTitle(recivedUsername+"'s Posts");
        /*
        ParseQuery<ParseObject> parseQuery= ParseQuery.getQuery("Photo");
        parseQuery.whereEqualTo("username",recivedUsername);
        parseQuery.orderByDescending("createdAt");
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size()>0&&e==null){
                    for(ParseObject post:objects){
                        final TextView caption=new TextView(getApplicationContext());
                        caption.setText(post.get("image_des")+"");
                        ParseFile postPicture= (ParseFile) post.get("picture");
                        postPicture.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(data!=null&&e==null) {

                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    ImageView postImageView = new ImageView(getApplicationContext());
                                    LinearLayout.LayoutParams postImage_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    postImage_params.setMargins(5, 5, 5, 5);
                                    postImageView.setLayoutParams(postImage_params);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setImageBitmap(bitmap);
                                    LinearLayout.LayoutParams caption_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    caption_params.setMargins(5, 5, 5, 5);
                                    caption.setLayoutParams(caption_params);
                                    caption.setGravity(View.TEXT_ALIGNMENT_CENTER);
                                    caption.setBackgroundColor(Color.WHITE);
                                    caption.setTextColor(Color.rgb(214, 124, 155));
                                    caption.setTextSize(30f);
                                    linearLayout.addView(postImageView);
                                    linearLayout.addView(caption);

                                }
                            }
                        });
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),recivedUsername+" does not have any post",Toast.LENGTH_SHORT).show();
                    finish();
                }
                progressDialog.dismiss();
            }
        });
        */
        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String imageLink=(String) snapshot.child("imageLink").getValue();
                String des=(String) snapshot.child("caption").getValue();
                ImageView postImageView = new ImageView(getApplicationContext());
                LinearLayout.LayoutParams postImage_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                postImage_params.setMargins(5, 5, 5, 5);
                postImageView.setLayoutParams(postImage_params);
                postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Picasso.get().load(imageLink).into(postImageView);
                LinearLayout.LayoutParams caption_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                caption_params.setMargins(5, 5, 5, 5);
                TextView caption=new TextView(getApplicationContext());
                caption.setText(des);
                caption.setLayoutParams(caption_params);
                caption.setGravity(View.TEXT_ALIGNMENT_CENTER);
                caption.setBackgroundColor(Color.WHITE);
                caption.setTextColor(Color.rgb(214, 124, 155));
                caption.setTextSize(30f);
                linearLayout.addView(postImageView);
                linearLayout.addView(caption);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}