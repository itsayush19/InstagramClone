package com.example.instagramclone;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.UUID;


public class SharePictureTab extends Fragment implements View.OnClickListener{

   private ImageView shareImage;
   private EditText caption;
   private Button btnShareImage;
   private FirebaseAuth mAuth;
   private Bitmap receivedImageBitmap;
   private String imagIdentifier;
   private String imageLink;

    public SharePictureTab() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_share_picture_tab, container, false);
        shareImage=view.findViewById(R.id.shareImage);
        caption=view.findViewById(R.id.caption);
        btnShareImage=view.findViewById(R.id.btnShare);
        mAuth=FirebaseAuth.getInstance();
        shareImage.setOnClickListener(SharePictureTab.this);
        btnShareImage.setOnClickListener(SharePictureTab.this);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.shareImage:
                if (android.os.Build.VERSION.SDK_INT >= 23 &&
                        ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]
                                    {Manifest.permission.READ_EXTERNAL_STORAGE},
                            1000);

                } else {

                    getChosenImage();

                }


                break;
            case R.id.btnShare:
                if (receivedImageBitmap != null) {

                    if (caption.getText().toString().equals("")) {
                        Toast.makeText(getContext(),"caption required",Toast.LENGTH_SHORT).show();


                    } else {

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        receivedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        imagIdentifier= UUID.randomUUID()+".png";
                        UploadTask uploadTask= FirebaseStorage.getInstance().getReference().child("my_images").child(imagIdentifier).putBytes(bytes);
                        final ProgressDialog dialog = new ProgressDialog(getContext());
                        dialog.setMessage("Loading...");
                        dialog.show();

                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(),"failed",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if(task.isSuccessful()){
                                            imageLink=task.getResult().toString();
                                        }

                                    }
                                });
                                HashMap<String,String> dataMap=new HashMap<>();
                                dataMap.put("imageLink", imageLink);
                                dataMap.put("caption",caption.getText().toString());
                                FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("posts").push().setValue(dataMap);
                                Toast.makeText(getContext(),"DONE!!",Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.dismiss();


                        /*
                        ParseFile parseFile = new ParseFile("img.png", bytes);
                        ParseObject parseObject = new ParseObject("Photo");
                        parseObject.put("picture", parseFile);
                        parseObject.put("image_des", caption.getText().toString());
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                        final ProgressDialog dialog = new ProgressDialog(getContext());
                        dialog.setMessage("Loading...");
                        dialog.show();
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e==null){
                                    Toast.makeText(getContext(),"DONE!!",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getContext(),"UNKNOWN ERROR",Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        });
                        */

                    }

                } else {

                    Toast.makeText(getContext(),"Error: You must select an image.",Toast.LENGTH_SHORT).show();

                }

                break;
        }
    }

    private void getChosenImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {

            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {

                getChosenImage();

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000) {

            if (resultCode == Activity.RESULT_OK) {

                //Do something with your captured image.
                /*try {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    receivedImageBitmap = BitmapFactory.decodeFile(picturePath);

                    shareImage.setImageBitmap(receivedImageBitmap);

                } catch (Exception e) {

                    e.printStackTrace();
                }*/
                Uri chosenImage=data.getData();
                try{
                    receivedImageBitmap=MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(),chosenImage);
                    shareImage.setImageBitmap(receivedImageBitmap);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

}