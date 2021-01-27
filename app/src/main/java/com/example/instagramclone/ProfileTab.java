package com.example.instagramclone;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class ProfileTab extends Fragment {
    private EditText edtProfileName,edtBio,edtProf,edtHobbeis,edtFavSports;
    private Button btnUpdateInfo;
    private FirebaseAuth mAuth;
    private String BIO,prof,hob,fav,profilename;
    public ProfileTab() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile_tab, container, false);
        edtProfileName=view.findViewById(R.id.edtProfileName);
        edtBio=view.findViewById(R.id.edtBio);
        edtProf=view.findViewById(R.id.edtProf);
        edtHobbeis=view.findViewById(R.id.edtHobbies);
        edtFavSports=view.findViewById(R.id.edtFavSports);
        btnUpdateInfo=view.findViewById(R.id.btnUpdateInfo);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        /*
        if(parseUser.get("profilename")!=null){
            edtProfileName.setText(parseUser.get("profilename").toString());
        }
        else{
            edtProfileName.setText("");
        }
        if(parseUser.get("bio")!=null){
            edtBio.setText(parseUser.get("bio").toString());
        }
        else{
            edtBio.setText("");
        }
        if(parseUser.get("profession")!=null){
            edtProf.setText(parseUser.get("profession").toString());
        }
        else{
            edtProf.setText("");
        }
        if(parseUser.get("hobbies")!=null){
            edtHobbeis.setText(parseUser.get("hobbies").toString());
        }
        else{
            edtHobbeis.setText("");
        }
        if(parseUser.get("favouritesports")!=null){
            edtFavSports.setText(parseUser.get("favouritesports").toString());
        }
        else{
            edtFavSports.setText("");
        }

        String profilename=FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("profile_name").getKey();
        String BIO=FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("bio").getKey();
        String prof=FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("profession").getKey();
        String hob=FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("hobbies").getKey();
        String fav=FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("fav_sports").getKey();
        */
        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                BIO=(String) snapshot.child("bio").getValue();
                prof=(String) snapshot.child("profession").getValue();
                hob=(String) snapshot.child("hobbies").getValue();
                fav=(String) snapshot.child("fav_sports").getValue();
                profilename=(String) snapshot.child("profile_name").getValue();
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
        if(profilename!=null){
            edtProfileName.setText(profilename);
        }
        if(BIO!=null){
            edtBio.setText(BIO);
        }
        if(prof!=null){
            edtProf.setText(prof);
        }
        if(hob!=null){
            edtHobbeis.setText(hob);
        }
        if(fav!=null){
            edtFavSports.setText(fav);
        }
        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                parseUser.put("profilename",edtProfileName.getText().toString());
                parseUser.put("bio",edtBio.getText().toString());
                parseUser.put("profession",edtProf.getText().toString());
                parseUser.put("hobbies",edtHobbeis.getText().toString());
                parseUser.put("favouritesports",edtFavSports.getText().toString());
                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Toast.makeText(getContext(),"User Info Updated",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getContext(),"User Info Not Updated",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                */
                FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("profile_name").setValue(edtProfileName.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("bio").setValue(edtBio.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("profession").setValue(edtProf.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("hobbies").setValue(edtHobbeis.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("fav_sports").setValue(edtFavSports.getText().toString());
            }
        });

        return view;
    }
}