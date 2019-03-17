package com.example.eversmileproject;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.net.Uri;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;


public class view_pics extends AppCompatActivity{

    private Button mainMenuBtn;
    private Button cameraBtn;
    private Button storeBtn;
    private ImageView faceView;
    private ImageView leftView;
    private ImageView rightView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pics);
        mainMenuBtn = (Button) findViewById(R.id.btn_mainMenu);
        cameraBtn = (Button) findViewById(R.id.btn_camera);
        storeBtn = (Button) findViewById(R.id.btn_store);
        faceView = (ImageView) findViewById(R.id.imageFaceView);
        leftView = (ImageView) findViewById(R.id.imageLeftView);
        rightView = (ImageView) findViewById(R.id.imageRightView);
        faceView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/face.jpg"));
        leftView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/left.jpg"));
        rightView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/right.jpg"));

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images");
        final StorageReference faceRef = imagesRef.child("face.jpg");
        final StorageReference leftRef = imagesRef.child("left.jpg");
        final StorageReference rightRef = imagesRef.child("right.jpg");
        final Uri faceFile = Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/face.jpg"));
        final Uri leftFile = Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/face.jpg"));
        final Uri rightFile = Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/face.jpg"));

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
        storeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceRef.putFile(faceFile);
                leftRef.putFile(leftFile);
                rightRef.putFile(rightFile);
            }
        });
    }

}
