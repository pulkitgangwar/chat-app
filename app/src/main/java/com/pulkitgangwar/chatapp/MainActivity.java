package com.pulkitgangwar.chatapp;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    RecyclerView dashboardRecyclerView;
    ArrayList<User> users;
    UserAdapter userAdapter;
    Button dashboardLogOut;

    // firebase variables
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseStorage storage;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // firebase instance
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_URL);

        // android resource instance
        user = auth.getCurrentUser();
        dashboardRecyclerView = findViewById(R.id.dasboard__recyclerview);
        dashboardLogOut = findViewById(R.id.dashboard__logout);




        if(user == null){
            startActivity(new Intent(MainActivity.this,RegisterActivity.class));
        } else {
            getUsersData();

            dashboardLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auth.signOut();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }
            });
        }
    }

    public void getUsersData() {
        DatabaseReference databaseReference = database.getReference().child("users");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot firebaseUsers: dataSnapshot.getChildren()) {
                    User user = firebaseUsers.getValue(User.class);
                    users = new ArrayList<>();
                    userAdapter = new UserAdapter(users);
                    users.add(user);
                    dashboardRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    dashboardRecyclerView.setAdapter(userAdapter);
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}