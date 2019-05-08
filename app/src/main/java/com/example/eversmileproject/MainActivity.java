package com.example.eversmileproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    CircleImageView profileView;

    protected void onStart() {
        super.onStart();
        // start authentication at startup
        mAuth.addAuthStateListener(mAuthListner);
        // main banner
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF4788F6));
        Spannable text = new SpannableString(getTitle());
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        setTitle(text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.signout);
        Button ShareBtn = findViewById(R.id.ShareBtn);
        Button seeBtn = findViewById(R.id.SeeBtn);
        Button findBtn = findViewById(R.id.FindBtn);
        Button appointment = findViewById(R.id.appointment);

        profileView = findViewById(R.id.profile_image);

        // File for the profile picture named profile.jpg on user's phone
        File imgFile = new File(Environment.getExternalStorageDirectory() + "/profile.jpg");

        // if/else statement to avoid null pointer exception
        // Glide library is used to display the profile picture
        if (imgFile.exists()) {
            Glide.with(MainActivity.this)
                    .load(imgFile)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.avatar_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .centerCrop()
                            .skipMemoryCache(true))
                    .into(profileView);
        }
        else {
            Glide.with(MainActivity.this)
                    .load(imgFile)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.avatar_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .centerCrop()
                            .skipMemoryCache(true))
                    .into(profileView);
                    openDialog("Remember to click the circle icon to select profile photo");
        }

        // create instance of Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        // check that user has credentials
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                String getEmail = "Welcome to SmileKnect! Please Log in.";
                // if not current user, redirect to sign in
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, signin.class));
                }
                // else, get user email
                else {
                    getEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                }
                // display email as user name
                setTitle(getEmail);
                Spannable text = new SpannableString(getTitle());
                text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                setTitle(text);

            }
        };

        // button to sign out of app
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });

        // button to move to see your smile
        seeBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, see_eversmile.class));
            }
        }));
        // button to move to social media sharing
        ShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, share_eversmile.class));
            }
        });
        // button to move to find smile map
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Maps.class));
            }
        });
        // button to go to appointment system
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Appointments.class));
            }
        });
        // click avatar to move to user info
        profileView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserInfo.class));
            }

        });
    }

    @Override
    public void onRestart() {
        super.onRestart();
        profileView = findViewById(R.id.profile_image);

        // File for the profile picture named profile.jpg on user's phone
        File imgFile = new File(Environment.getExternalStorageDirectory() + "/profile.jpg");

        // if/else statement to avoid null pointer exception
        // Glide library is used to display the profile picture
        if (imgFile.exists()) {
            Glide.with(MainActivity.this)
                    .load(imgFile)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.avatar_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .centerCrop()
                            .skipMemoryCache(true))
                    .into(profileView);
        }
        else {
            Glide.with(MainActivity.this)
                    .load(imgFile)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.avatar_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .centerCrop()
                            .skipMemoryCache(true))
                    .into(profileView);
        }
    }

    //Popup Window, displays the String passsed in as dialog
    public void openDialog(String dialog){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(dialog);

        // set ok button to close box
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        // show the actual dialog box
        alertDialog.show();
    }
}
