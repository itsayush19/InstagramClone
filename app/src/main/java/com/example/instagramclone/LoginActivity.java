package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtEmail,edtPword;
    private Button btnLogin,btnSignup;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtEmail=findViewById(R.id.loginemail);
        edtPword=findViewById(R.id.pwlogin);
        btnLogin=findViewById(R.id.lgbtn);
        btnSignup=findViewById(R.id.sulgn);
        mAuth=FirebaseAuth.getInstance();
        edtPword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER&&event.getAction()==KeyEvent.ACTION_DOWN){
                    onClick(btnLogin);
                }
                return false;
            }
        });

        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);

        if(mAuth.getCurrentUser()!=null){
            transitionToSocialMedia();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lgbtn:
                if(edtPword.getText().toString().equals("")||edtEmail.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this,"Email and Password are required",Toast.LENGTH_SHORT).show();
                }
                else
                {   /*
                    ParseUser.logInInBackground(edtEmail.getText().toString(), edtPword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user!=null&&e==null){
                            Toast.makeText(LoginActivity.this,user.getUsername()+" is logged in",Toast.LENGTH_SHORT).show();
                            transitionToSocialMedia();
                        }
                        else{
                            Toast.makeText(LoginActivity.this,user.getUsername()+" is not logged in",Toast.LENGTH_SHORT).show();
                        }
                      }
                    });
                    */

                    mAuth.signInWithEmailAndPassword(edtEmail.getText().toString(),edtEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,"logged in",Toast.LENGTH_SHORT).show();
                                transitionToSocialMedia();
                            }
                        }
                    });
                }
                break;
            case R.id.sulgn:
                Intent i= new Intent(LoginActivity.this,SignUp.class);
                startActivity(i);
                break;
        }
    }
    public void whenTapped(View view){
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