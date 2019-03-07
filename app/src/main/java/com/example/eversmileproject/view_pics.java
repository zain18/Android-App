package com.example.eversmileproject;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class view_pics extends AppCompatActivity{

    private Button mainMenuBtn;
    private Button cameraBtn;
    private ImageView faceView;
    private ImageView leftView;
    private ImageView rightView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pics);
        mainMenuBtn = (Button) findViewById(R.id.btn_mainMenu);
        cameraBtn = (Button) findViewById(R.id.btn_camera);
        faceView = (ImageView) findViewById(R.id.imageFaceView);
        leftView = (ImageView) findViewById(R.id.imageLeftView);
        rightView = (ImageView) findViewById(R.id.imageRightView);
        faceView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/face.jpg"));
        leftView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/left.jpg"));
        rightView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/right.jpg"));

        mainMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view_pics.this, MainActivity.class));
            }
        });
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view_pics.this, see_eversmile.class));
            }
        });
    }
}
