package com.example.eversmileproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class share_eversmile extends AppCompatActivity {

    private Button faceBtn;
    private Button twitBtn;
    private Button instaBtn;
    private Button snapBtn;
    private Button mainBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_eversmile);
        faceBtn = (Button) findViewById(R.id.shareFace);
        twitBtn =(Button) findViewById(R.id.shareTwit);
        instaBtn =(Button) findViewById(R.id.shareInsta);
        snapBtn =(Button) findViewById(R.id.shareSnap);
        mainBtn = (Button) findViewById(R.id.shareMain);

        faceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(share_eversmile.this, FacebookLogin.class));
            }
        });


        twitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(share_eversmile.this, ShareToTwitter.class));
            }
        });
        instaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(share_eversmile.this, ShareToInstagram.class));
            }
        });
        snapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(share_eversmile.this, ShareToSnapchat.class));
            }
        });
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(share_eversmile.this, MainActivity.class));
            }
        });


    }
}
