package com.example.eversmileproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

// this activity allows user to upload and download records
public class UserRecords extends AppCompatActivity {
    private Button ulHistory;
    private Button ulXray;
    private Button dlHistory;
    private Button dlXray;
    private Button dlReport;
    private Button homeBtn;
    private Button selectBtn;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    Uri fileUri;
    String uploadName = "test";
    Intent intent;
    String url;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_records);
        ulHistory = findViewById(R.id.uploadHistory);
        ulXray = findViewById(R.id.uploadXray);
        dlHistory = findViewById(R.id.downloadHistory);
        dlXray = findViewById(R.id.downloadXray);
        dlReport = findViewById(R.id.downloadReport);
        homeBtn = findViewById(R.id.homebtn);
        selectBtn = findViewById(R.id.selectBtn);

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        // button to allow user to select file to upload
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UserRecords.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    selectFile();
                }
                else
                    ActivityCompat.requestPermissions(UserRecords.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
            }
        });
        // upload selected file to history folder
        ulHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fileUri!=null){
                    uploadHistory(fileUri);
                }
                else{
                    Toast.makeText(UserRecords.this,"File Selection error", Toast.LENGTH_SHORT).show();
                }

            }
        });
        // upload selected file to xray folder
        ulXray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileUri!=null){
                    uploadXray(fileUri);
                }
                else{
                    Toast.makeText(UserRecords.this,"File Selection error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // open history recycler view
        dlHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserRecords.this, HistoryRecycler.class));
            }
        });
        // open xray recycler view
        dlXray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserRecords.this, XrayRecycler.class));
            }
        });
        // open doctor report recycler view
        dlReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserRecords.this, ReportRecycler.class));
            }
        });


        // button to return to main menu
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserRecords.this, MainActivity.class));
            }
        });
    }

    @Override // get permission for selecting files
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            selectFile();
        }
        else
            Toast.makeText(UserRecords.this,"Please grant permission", Toast.LENGTH_SHORT).show();
    }

    // allow user to select file for upload
    private void selectFile(){
        Intent intent = new Intent(); // create intent
        intent.setType("*/*"); // allow user to select any type of file
        intent.setAction(Intent.ACTION_GET_CONTENT); // intent which retrieves files
        // start intent, 86 is arbitrary request code
        startActivityForResult(intent, 86);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // check for arbitrary request code from retrieve intent
        if (requestCode==86 && resultCode==RESULT_OK && data != null){
            fileUri = data.getData(); // capture uri
            uploadName = data.getData().getLastPathSegment(); // capture file name
        }
        else{
            Toast.makeText(UserRecords.this,"Please select file",Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadHistory(Uri fileUri){
        // upload name, add .pdf so user can download from base
        final String fileName = uploadName + ".pdf";
        final String fileName1 = uploadName; // file name for firebase database
        StorageReference storageReference=storage.getReference();
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference userRef = storageReference.child(currentUser);
        // generate reference for users history folder
        final StorageReference historyUserRef = userRef.child("history").child(fileName);

        historyUserRef.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) { // add file to firebase cloud
                historyUserRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) { // wait for on complete
                        url =task.getResult().toString(); // retrieve download url
                        // create firebase database reference
                        DatabaseReference reference = database.getReference();
                        DatabaseReference historydb = reference.child(currentUser).child("history");
                        // once file is upload to cloud, store the download url in firebase database
                        historydb.child(fileName1).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(UserRecords.this,"File Uploaded",Toast.LENGTH_SHORT).show();
                                }
                                else
                                    Toast.makeText(UserRecords.this,"Upload failed",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) { // failure message
                Toast.makeText(UserRecords.this,"Upload failed",Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) { // message that file is uploading
                Toast.makeText(UserRecords.this,"Upload In Progress",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadXray(Uri xrayUri){
        // upload name, add .pdf so user can download from base
        final String fileName = uploadName + ".pdf";
        final String fileName1 = uploadName; // file name for firebase database
        StorageReference storageReference=storage.getReference();
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference userRef = storageReference.child(currentUser);
        // generate reference for users history folder
        final StorageReference xrayUserRef = userRef.child("xray").child(fileName);

        xrayUserRef.putFile(xrayUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) { // add file to firebase cloud
                xrayUserRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) { // wait for on complete
                        url =task.getResult().toString(); // retrieve download url
                        // create firebase database reference
                        DatabaseReference reference = database.getReference();
                        DatabaseReference xraydb = reference.child(currentUser).child("xray");
                        // once file is upload to cloud, store the download url in firebase database
                        xraydb.child(fileName1).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(UserRecords.this,"File Uploaded",Toast.LENGTH_SHORT).show();
                                }
                                else
                                    Toast.makeText(UserRecords.this,"Upload failed",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) { // failure message
                Toast.makeText(UserRecords.this,"Upload failed",Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) { // message that file is uploading
                Toast.makeText(UserRecords.this,"Upload In Progress",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
