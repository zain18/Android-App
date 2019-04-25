package com.example.eversmileproject;

import android.Manifest;
import android.app.ProgressDialog;
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

        dlHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserRecords.this, HistoryRecycler.class));
            }
        });

        dlXray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserRecords.this, XrayRecycler.class));
            }
        });

        dlReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserRecords.this, ReportRecycler.class));
            }
        });



        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserRecords.this, MainActivity.class));
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            selectFile();
        }
        else
            Toast.makeText(UserRecords.this,"Please grant permission", Toast.LENGTH_SHORT).show();
    }

    private void selectFile(){
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 86);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==86 && resultCode==RESULT_OK && data != null){
            fileUri = data.getData();
            uploadName = data.getData().getLastPathSegment();
        }
        else{
            Toast.makeText(UserRecords.this,"Please select file",Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadHistory(Uri fileUri){
        final String fileName = uploadName + ".pdf";
        final String fileName1 = uploadName;
        final Uri myuri = fileUri;
        StorageReference storageReference=storage.getReference();
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference userRef = storageReference.child(currentUser);
        final StorageReference historyUserRef = userRef.child("history").child(fileName);

        historyUserRef.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                historyUserRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        url =task.getResult().toString();
                        DatabaseReference reference = database.getReference();
                        DatabaseReference historydb = reference.child(currentUser).child("history");

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
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserRecords.this,"Upload failed",Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UserRecords.this,"Upload In Progress",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void uploadXray(Uri xrayUri){
        final String fileName = uploadName + ".pdf";
        final String fileName1 = uploadName;
        StorageReference storageReference=storage.getReference();
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference userRef = storageReference.child(currentUser);
        final StorageReference xrayUserRef = userRef.child("xray").child(fileName);

        xrayUserRef.putFile(xrayUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                xrayUserRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        url =task.getResult().toString();
                        DatabaseReference reference = database.getReference();
                        DatabaseReference xraydb = reference.child(currentUser).child("xray");

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
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserRecords.this,"Upload failed",Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UserRecords.this,"Upload In Progress",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
