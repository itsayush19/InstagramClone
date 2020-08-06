package com.example.instagramclone;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class UserTab extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;


    public UserTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_user_tab, container, false);
        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading User List");
        progressDialog.show();
        listView=view.findViewById(R.id.userList);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        arrayList=new ArrayList();
        arrayAdapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,arrayList);
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


        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i=new Intent(getContext(),UserPosts.class);
        i.putExtra("username",arrayList.get(position));
        startActivity(i);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
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
        return false;
    }
}