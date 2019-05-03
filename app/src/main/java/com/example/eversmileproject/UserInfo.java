package com.example.eversmileproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfo extends AppCompatActivity {
    private EditText FullName;
    private EditText Email;
    private EditText Age;
    private EditText Address;
    private EditText Phone;
    private Button Submit;
    private Button homebtn;
    private Button recordBtn;
    private ProgressBar progressBar;
    FirebaseFirestore db;
    private static final String NAME_KEY = "Name";
    private static final String EMAIL_KEY = "Email";
    private static final String PHONE_KEY = "Phone";
    private static final String AGE_KEY = "Age";
    private static final String ADDRESS_KEY = "Address";
    private static final String ID_KEY = "User ID";
    CircleImageView profileView;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid(); // unique reference for user

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        FullName = findViewById(R.id.FullName);
        Email = findViewById(R.id.UserEmail);
        Age = findViewById(R.id.UserAge);
        Address = findViewById(R.id.UserAddress);
        Phone = findViewById(R.id.UserNumber);
        Submit = findViewById(R.id.SubmitButton);
        homebtn = findViewById(R.id.homebtn);
        progressBar = findViewById(R.id.progressBar1);
        recordBtn = findViewById(R.id.recordBtn);
        db = FirebaseFirestore.getInstance();

        profileView = findViewById(R.id.profile_image);

        File imgFile = new File(Environment.getExternalStorageDirectory() + "/profile.jpg");

        // if/else statement to avoid null pointer exception
        if (imgFile.exists()) {
            Glide.with(UserInfo.this)
                    .load(imgFile)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.avatar_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .centerCrop()
                            .skipMemoryCache(true))
                    .into(profileView);
        }
        else {
            Glide.with(UserInfo.this)
                    .load(imgFile)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.avatar_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .centerCrop()
                            .skipMemoryCache(true))
                    .into(profileView);
        }

        FirebaseFirestore firebase = FirebaseFirestore.getInstance();
        // grab the collection of doctor offices using firebase
        firebase.collection("UserInfo").addSnapshotListener(new EventListener<QuerySnapshot>() {
            String test;
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot doc:queryDocumentSnapshots) { // scroll of user looking for matching ID
                        test = doc.getString("User ID");
                        if (test.equals(currentUser)) {

                            FullName.setText(doc.getString("Name"));
                            Age.setText(doc.getString("Age"));
                            Email.setText(doc.getString("Email"));
                            Address.setText(doc.getString("Address"));
                            Phone.setText(doc.getString("Phone"));
                            break;

                        }
                }
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = FullName.getText().toString();
                String age = Age.getText().toString();
                String email = Email.getText().toString();
                String address = Address.getText().toString();
                String phone = Phone.getText().toString();

                if (TextUtils.isEmpty(fullname)) {
                    Toast.makeText(getApplicationContext(), "Please enter your Full Name ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(age)) {
                    Toast.makeText(getApplicationContext(), "Please enter your Age ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter your Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(getApplicationContext(), "Please enter your address ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getApplicationContext(), "Please enter your Phone Number ", Toast.LENGTH_SHORT).show();
                    return;
                }
                addNewContact(fullname, email, age, phone, address, currentUser);
            }
        });

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfo.this, MainActivity.class));
            }
        });

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfo.this, UserRecords.class));
            }
        });

        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }



    private void selectImage() {

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfo.this);
        builder.setTitle("Select profile Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");

                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void openDialog(String dialog) {
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(dialog);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
                if (requestCode == SELECT_FILE)
                    onSelectFromGalleryResult(data);

                else if (requestCode == REQUEST_CAMERA)
                    onCaptureImageResult(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();

        Cursor cursor = getContentResolver().query(selectedImageUri, null, null, null, null);

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String selectedImagePath = cursor.getString(column_index);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Bitmap thumbnail;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;

        thumbnail = BitmapFactory.decodeFile(selectedImagePath, options);
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory() + "/profile.jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        profileView.setImageBitmap(thumbnail);
        // if/else statement to avoid null pointer exception
        if (destination.exists()) {
            Glide.with(UserInfo.this)
                    .load(thumbnail)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.avatar_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .centerCrop()
                            .skipMemoryCache(true))
                    .into(profileView);
        }
        else {
            Glide.with(UserInfo.this)
                    .load(thumbnail)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.avatar_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .centerCrop()
                            .skipMemoryCache(true))
                    .into(profileView);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory() + "/profile.jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        profileView.setImageBitmap(thumbnail);
        // if/else statement to avoid null pointer exception
        if (destination.exists()) {
            Glide.with(UserInfo.this)
                    .load(thumbnail)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.avatar_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .centerCrop()
                            .skipMemoryCache(true))
                    .into(profileView);
        }
        else {
            Glide.with(UserInfo.this)
                    .load(thumbnail)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.avatar_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .centerCrop()
                            .skipMemoryCache(true))
                    .into(profileView);
        }
    }

    private void addNewContact(String name, String email, String age, String phone, String Address, String userId) {
        Map<String, Object> newContact = new HashMap<>();
        newContact.put(ID_KEY, userId);
        newContact.put(NAME_KEY, name);
        newContact.put(EMAIL_KEY, email);
        newContact.put(PHONE_KEY, phone);
        newContact.put(AGE_KEY, age);
        newContact.put(ADDRESS_KEY, Address);

        progressBar.setVisibility(View.VISIBLE);
        db.collection("UserInfo").document(userId).set(newContact)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(UserInfo.this, "User Profile Updated",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserInfo.this, "ERROR" + e.toString(),
                                Toast.LENGTH_SHORT).show();
                        Log.d("TAG", e.toString());
                    }
                });
    }
}