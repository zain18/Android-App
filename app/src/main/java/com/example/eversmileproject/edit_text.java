package com.example.eversmileproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class edit_text extends AppCompatActivity {
    static SimpleDateFormat s = new SimpleDateFormat("MMddyyyyhhmmss"); // date format
    static String format = s.format(new Date());
    String userName = FirebaseAuth.getInstance().getCurrentUser().getEmail(); // retrieve user email
    private static final String FILE_NAME = format+".txt"; // create file name using current date
    String path = Environment.getExternalStorageDirectory() + "/" + userName + FILE_NAME; // note file path
    File tempFile = new File(path);
    private Button saveBtn;
    private Button backBtn;

    EditText mEditText; // text window

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);

        mEditText = findViewById(R.id.edit_text);
        saveBtn = (Button) findViewById(R.id.button_save);
        backBtn = (Button) findViewById(R.id.button_back);

        //Create Firebase storage references
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid(); // unique reference for user
        StorageReference userRef = storageRef.child(currentUser);
        StorageReference noteUserRef = userRef.child("notes");
        final StorageReference noteRef = noteUserRef.child(userName + FILE_NAME);
        final Uri noteFile = Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/" + userName + FILE_NAME)); // create uri for note file

        // This button takes the text in the box and saves it to cloud
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString(); // capture text in textbox
                FileOutputStream fos = null;

                try {
                    fos = new FileOutputStream(tempFile); // create file output stream
                    fos.write(text.getBytes()); // write to stream
                    mEditText.getText().clear();
                    Toast.makeText(edit_text.this, "Note saved to cloud", // message user that file is being saves
                            Toast.LENGTH_LONG).show();

                    noteRef.putFile(noteFile); // write to cloud
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        // button returns user to viewing gallery
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(edit_text.this, view_pics.class));
            }
        });
    }
}
