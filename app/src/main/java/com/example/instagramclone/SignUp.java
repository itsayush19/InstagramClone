package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.parse.ParseInstallation;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private EditText edtEmail;
    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnSignUp,btnLogIn;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        edtEmail=findViewById(R.id.email);
        edtUsername=findViewById(R.id.usernameup);
        edtPassword=findViewById(R.id.passwordup);
        btnSignUp=findViewById(R.id.signupbtn);
        btnLogIn=findViewById(R.id.loginbtn);
        mAuth=FirebaseAuth.getInstance();
        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER&&event.getAction()==KeyEvent.ACTION_DOWN){
                    onClick(btnSignUp);
                }
                return false;
            }
        });
        btnSignUp.setOnClickListener(this);
        btnLogIn.setOnClickListener(this);
        if(mAuth.getCurrentUser()!=null){
            transitionToSocialMedia();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.signupbtn:
                if(edtEmail.getText().toString().equals("")||edtUsername.getText().toString().equals("")||edtPassword.getText().toString().equals("")){
                    Toast.makeText(SignUp.this,"Email, Username, Password are required",Toast.LENGTH_SHORT).show();
                }
                else{
                    /*
                    final ParseUser appUser=new ParseUser();
                    appUser.setEmail(edtEmail.getText().toString());
                    appUser.setUsername(edtUsername.getText().toString());
                    appUser.setPassword(edtPassword.getText().toString());
                    final ProgressDialog pd=new ProgressDialog(this);
                    pd.setMessage("Signing Up"+appUser.getUsername());
                    pd.show();
                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null){
                                Toast.makeText(SignUp.this,appUser.getUsername()+" is signed up",Toast.LENGTH_SHORT).show();
                                transitionToSocialMedia();
                            }
                            else{
                                Toast.makeText(SignUp.this,appUser.getUsername()+" is not signed up",Toast.LENGTH_SHORT).show();
                            }
                            pd.dismiss();
                        }
                    });
                    */
                    final ProgressDialog pd=new ProgressDialog(this);
                    pd.setMessage("Signing Up"+edtUsername);
                    pd.show();
                    mAuth.createUserWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUp.this,edtUsername+" is signed up",Toast.LENGTH_SHORT).show();
                                FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid()).child("username").setValue(edtUsername.getText().toString());
                                transitionToSocialMedia();
                            }
                            else{
                                Toast.makeText(SignUp.this,edtUsername+" is not signed up",Toast.LENGTH_SHORT).show();
                            }
                            pd.dismiss();
                        }
                    });
                }
                break;
            case R.id.loginbtn:
                Intent i= new Intent(SignUp.this,LoginActivity.class);
                startActivity(i);
                break;
        }
    }
    public void tapped(View view){
        try{
                InputMethodManager i= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                i.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void transitionToSocialMedia(){
        Intent i=new Intent(this,SocialMediaActivity.class);
        startActivity(i);
        finish();
    }
}