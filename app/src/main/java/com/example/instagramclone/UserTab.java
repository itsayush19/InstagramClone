package com.example.instagramclone;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.List;


public class UserTab extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private ListView listView;
    private ArrayList<String> usernames,bio,hobbies,profession,fav;
    private ArrayAdapter adapter;
    private FirebaseAuth mAuth;


    public UserTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_user_tab, container, false);
        listView=view.findViewById(R.id.userList);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        mAuth=FirebaseAuth.getInstance();
        usernames=new ArrayList();
        bio=new ArrayList<>();
        profession=new ArrayList<>();
        hobbies=new ArrayList<>();
        fav=new ArrayList<>();
        adapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,usernames);
        listView.setAdapter(adapter);
        /*
        ParseQuery<ParseUser> parseQuery= ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null && objects.size()>0){
                    for(ParseUser user:objects){
                        arrayList.add(user.getUsername());
                    }
                    listView.setAdapter(arrayAdapter);
                    progressDialog.dismiss();
                }
            }
        });
        */
        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String username= (String) snapshot.child("username").getValue();
                String b=(String) snapshot.child("bio").getValue();
                String p=(String) snapshot.child("profession").getValue();
                String h=(String) snapshot.child("hobbies").getValue();
                String f=(String) snapshot.child("fav_sports").getValue();
                usernames.add(username);
                bio.add(b);
                profession.add(p);
                hobbies.add(h);
                fav.add(f);
                adapter.notifyDataSetChanged();
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

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i=new Intent(getContext(),UserPosts.class);
        i.putExtra("username",usernames.get(position));
        startActivity(i);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        /*
        ParseQuery<ParseUser> parseQuery=ParseUser.getQuery();
        parseQuery.whereEqualTo("username",arrayList.get(position));
        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if(object!=null&&e==null){
                    final AlertDialog.Builder db=new AlertDialog.Builder(getContext());
                    db.setTitle(object.getUsername()+"'s info");
                    db.setMessage(object.get("bio")+"\n"+object.get("profession")+"\n"+object.get("hobbies")+"\n"+object.get("favouritesports"));
                    db.setIcon(R.drawable.user);
                    db.show();
                }
            }
        });
        */
        AlertDialog.Builder db=new AlertDialog.Builder(getContext());
        db.setTitle(usernames.get(position)+"'s info");
        db.setMessage(bio.get(position)+"\n"+profession.get(position)+"\n"+hobbies.get(position)+"\n"+fav.get(position));
        db.setIcon(R.drawable.user);
        db.show();
        return false;
    }
}
/*
 FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            String username = (String) snapshot.child("username").getValue();
                            uids.add(snapshot.getKey());
                            usernames.add(username);
                            adapter.notifyDataSetChanged();
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
 */