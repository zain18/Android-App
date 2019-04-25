package com.example.eversmileproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;


public class view_pics extends AppCompatActivity{

    private Button mainMenuBtn;
    private Button cameraBtn;
    private Button storeBtn;
    private Button noteBtn;
    private Button findBtn;
    private ImageView faceView;
    private ImageView leftView;
    private ImageView rightView;

    //Popup Window
    public void openDialog(String dialog){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(dialog);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize layout elements
        setContentView(R.layout.activity_view_pics);
        openDialog("Don't forget to upload your photos!");
        mainMenuBtn = (Button) findViewById(R.id.btn_mainMenu);
        cameraBtn = (Button) findViewById(R.id.btn_camera);
        storeBtn = (Button) findViewById(R.id.btn_store);
        noteBtn = (Button) findViewById(R.id.btn_note);
        findBtn = (Button) findViewById(R.id.btn_find);
        faceView = (ImageView) findViewById(R.id.imageFaceView);
        leftView = (ImageView) findViewById(R.id.imageLeftView);
        rightView = (ImageView) findViewById(R.id.imageRightView);
        faceView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/face.jpg"));
        leftView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/left.jpg"));
        rightView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/right.jpg"));

        //Create Firebase storage references
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid(); // unique reference for user
        String userName = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        StorageReference userRef = storageRef.child(currentUser);
        StorageReference imagesUserRef = userRef.child("images");
        final StorageReference faceRef = imagesUserRef.child(userName + "face.jpg");
        final StorageReference leftRef = imagesUserRef.child(userName + "left.jpg");
        final StorageReference rightRef = imagesUserRef.child(userName +"right.jpg");
        final Uri faceFile = Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/" + userName +"face.jpg"));
        final Uri leftFile = Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/" + userName + "face.jpg"));
        final Uri rightFile = Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/" + userName + "face.jpg"));



        // Buttons to switch activities
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
        noteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view_pics.this, edit_text.class));
            }
        });

        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view_pics.this, Maps.class));
            }
        });
        // Button to upload to cloud
        storeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceRef.putFile(faceFile);
                leftRef.putFile(leftFile);
                rightRef.putFile(rightFile);
                openDialog("Pictures uploaded!");

            }
        });
    }

}
