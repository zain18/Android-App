package com.example.eversmileproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class UserInfo extends AppCompatActivity {
    private EditText FullName;
    private EditText Email;
    private EditText Age;
    private EditText Address;
    private EditText Message;
    private EditText Phone;
    private Button Submit;
    private Button homebtn;
    private ProgressBar progressBar;
    FirebaseFirestore db;
    private static final String NAME_KEY = "Name";
    private static final String EMAIL_KEY = "Email";
    private static final String PHONE_KEY = "Phone";
    private static final String AGE_KEY = "Age";
    private static final String ADDRESS_KEY = "Address";
    private static final String MESSAGE_KEY = "Message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        FullName = findViewById(R.id.FullName);
        Email = findViewById(R.id.UserEmail);
        Age = findViewById(R.id.UserAge);
        Address = findViewById(R.id.UserAddress);
        Phone = findViewById(R.id.UserNumber);
        Message = findViewById(R.id.UserMessage);
        Submit = findViewById(R.id.SubmitButton);
        homebtn = findViewById(R.id.homebtn);
        progressBar = findViewById(R.id.progressBar1);
        db = FirebaseFirestore.getInstance();

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fullname = FullName.getText().toString();
                String age = Age.getText().toString();
                String email = Email.getText().toString();
                String address = Address.getText().toString();
                String phone = Phone.getText().toString();
                String message = Message.getText().toString();


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
                addNewContact(fullname, email, age, phone, address, message);



            }


        });

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfo.this, MainActivity.class));
            }
        });



    }

    private void addNewContact(String name, String email, String age, String phone, String Address, String Message) {
        Map<String, Object> newContact = new HashMap<>();
        newContact.put(NAME_KEY, name);
        newContact.put(EMAIL_KEY, email);
        newContact.put(PHONE_KEY, phone);
        newContact.put(AGE_KEY, age);
        newContact.put(ADDRESS_KEY, Address);
        newContact.put(MESSAGE_KEY, Message);
        progressBar.setVisibility(View.VISIBLE);
        db.collection("UserInfo").document(name).set(newContact)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(UserInfo.this, "User Registered",
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