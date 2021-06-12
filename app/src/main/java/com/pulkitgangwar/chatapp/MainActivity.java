package com.pulkitgangwar.chatapp;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseStorage storage;
    TextView dashboardTitle;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        user = auth.getCurrentUser();
        dashboardTitle = findViewById(R.id.dashboard__title);
        logoutButton = findViewById(R.id.dashboard__logout);

        if(user == null){
            startActivity(new Intent(MainActivity.this,RegisterActivity.class));
        } else {
            StorageReference storageReference = storage.getReference().child("userProfile").child(auth.getUid());
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.d("oncreate",String.valueOf(uri));

                }
            });
            dashboardTitle.setText(user.getEmail().toString());
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auth.signOut();
//                    change to loginActivity
                    startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                }
            });
        }
    }
}