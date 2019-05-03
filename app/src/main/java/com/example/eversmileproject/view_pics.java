package com.example.eversmileproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private Button deleteBtn;
    private ImageView faceView;
    private ImageView leftView;
    private ImageView rightView;
    private String uploadMessage;
    private boolean faceExists;
    private boolean rightExists;
    private boolean leftExists;

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

    //Popup Window to remind users to upload
    protected void openDialog(String dialog){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(dialog);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // check that user uploaded files exist on firebase, nested listeners to select and display message
    protected void checkUpload(){
        // check to see if each of the files exist
        faceRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                faceExists = true;
                // check to see if left file exists
                leftRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        leftExists = true;
                        // check to see if right file exists
                        rightRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                rightExists = true; // in case all three of true, no message
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                rightExists = false;
                                selectMessage();
                                openDialog(uploadMessage);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        leftExists = false;
                        rightRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                rightExists = true;
                                selectMessage();
                                openDialog(uploadMessage);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                rightExists = false;
                                selectMessage();
                                openDialog(uploadMessage);
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                faceExists = false;
                leftRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        leftExists = true;
                        rightRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                rightExists = true;
                                selectMessage();
                                openDialog(uploadMessage);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                rightExists = false;
                                selectMessage();
                                openDialog(uploadMessage);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        leftExists = false;
                        rightRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                rightExists = true;
                                selectMessage();
                                openDialog(uploadMessage);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                rightExists = false;
                                selectMessage();
                                openDialog(uploadMessage);
                            }
                        });
                    }
                });
            }
        });
    }

    // this method is just an if/else tree to select correct message
    protected void selectMessage(){
        if (!faceExists && !leftExists && !rightExists)
            uploadMessage = "Please upload your photos";
        else if (!faceExists&& !leftExists)
            uploadMessage = "Please upload your face and left photos";
        else if (!faceExists && !rightExists)
            uploadMessage = "Please upload your face and right photos";
        else if (!faceExists)
            uploadMessage = "Please upload your face photo";
        else if (!rightExists && !leftExists)
            uploadMessage = "Please upload your left and right photos";
        else if (!leftExists)
            uploadMessage = "Please upload your left photo";
        else if (!rightExists)
            uploadMessage = "Please upload your right photo";
        else
            uploadMessage = ""; // all photos present
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize layout elements
        setContentView(R.layout.activity_view_pics);
        // prompt user to upload appropriate photos
        mainMenuBtn = findViewById(R.id.btn_mainMenu);
        cameraBtn = findViewById(R.id.btn_camera);
        storeBtn = findViewById(R.id.btn_store);
        noteBtn = findViewById(R.id.btn_note);
        findBtn = findViewById(R.id.btn_find);
        faceView = findViewById(R.id.imageFaceView);
        leftView = findViewById(R.id.imageLeftView);
        rightView = findViewById(R.id.imageRightView);
        deleteBtn = findViewById(R.id.btn_delete);
        faceView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/" + userName+ "face.jpg"));
        leftView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/" + userName + "left.jpg"));
        rightView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/" + userName + "right.jpg"));

        final File facepic = new File(Environment.getExternalStorageDirectory()+"/" + userName +"face.jpg");
        final File leftpic = new File(Environment.getExternalStorageDirectory()+"/" + userName +"left.jpg");
        final File rightpic = new File(Environment.getExternalStorageDirectory()+"/" + userName +"right.jpg");
        final Uri faceFile = Uri.fromFile(facepic);
        final Uri leftFile = Uri.fromFile(leftpic);
        final Uri rightFile = Uri.fromFile(rightpic);

        checkUpload(); // check what photos the user has uploaded

        // Buttons to switch activities
        mainMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view_pics.this, MainActivity.class));
            }
        });
        // return to camera
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view_pics.this, see_eversmile.class));
            }
        });
        // button to allow user to write a note
        noteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view_pics.this, edit_text.class));
            }
        });
        // after pictures, allow user to move to find office map
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

        // allow user to delete all photos from phone and firebase
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete photos from external storage
                facepic.delete();
                leftpic.delete();
                rightpic.delete();
                // delete photos from firebase
                faceRef.delete();
                leftRef.delete();
                rightRef.delete();
                // update image views
                faceView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/" + userName+ "face.jpg"));
                leftView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/" + userName + "left.jpg"));
                rightView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/" + userName + "right.jpg"));
                // pop up message
                openDialog("Photos deleted");
            }
        });
    }
}
