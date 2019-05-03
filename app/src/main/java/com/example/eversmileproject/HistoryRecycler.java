package com.example.eversmileproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HistoryRecycler extends AppCompatActivity {
    RecyclerView recyclerView; // list view for all the items
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firebase_recycler);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid(); // reference for specific user
        DatabaseReference historydb = databaseReference.child(currentUser).child("history"); // reference for users history box

        // listener to update recycler list
        historydb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String fileName = dataSnapshot.getKey(); // capture name and download urls
                String url = dataSnapshot.getValue(String.class);

                ((FBItemAdapter)recyclerView.getAdapter()).update(fileName,url); // use adapter to update list
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView = findViewById(R.id.firebaseRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(HistoryRecycler.this)); // set up layout
        // call the firebase adapter constructor
        FBItemAdapter myAdapter = new FBItemAdapter(recyclerView, HistoryRecycler.this,new ArrayList<String>(), new ArrayList<String>());
        recyclerView.setAdapter(myAdapter); // set FBItemAdapter as the adapter for recycler view
    }
}
