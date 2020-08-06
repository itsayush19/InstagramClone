package com.example.instagramclone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class ProfileTab extends Fragment {
    EditText edtProfileName,edtBio,edtProf,edtHobbeis,edtFavSports;
    Button btnUpdateInfo;

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

        final ParseUser parseUser=ParseUser.getCurrentUser();
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

        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        return view;
    }
}