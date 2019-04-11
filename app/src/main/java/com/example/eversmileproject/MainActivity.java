package com.example.eversmileproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.graphics.drawable.ColorDrawable;

import com.facebook.login.widget.LoginButton;
import com.facebook.share.Share;
import com.google.firebase.auth.FirebaseAuth;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.CallbackManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;

    public TextView userName;
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFD81B60));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TextView userName = (TextView) findViewById(R.id.userName);
        Button button = (Button) findViewById(R.id.signout);
        Button ShareBtn =(Button) findViewById(R.id.ShareBtn);
        Button seeBtn =(Button) findViewById(R.id.SeeBtn);
        Button findBtn = (Button) findViewById(R.id.FindBtn);

        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                String getEmail = "";
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, signin.class));
                } else {
                    getEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                }
                setTitle("Logged in as: " + getEmail);
            }
        };

        //userName.setText("Logged in as: " + getEmail);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });


        seeBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, see_eversmile.class));
            }
        }));
        ShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, share_eversmile.class));
            }
        });
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Maps.class));
            }
        });


    }


}
