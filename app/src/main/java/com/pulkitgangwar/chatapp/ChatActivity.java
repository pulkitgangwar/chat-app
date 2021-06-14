package com.pulkitgangwar.chatapp;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    TextView nameView;
    CircleImageView userProfileImageView;
    String name,userProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // android resource instance
        nameView = findViewById(R.id.chat__name);
        userProfileImageView = findViewById(R.id.chat__userprofileimage);

        //intents from MainActivity
        name = getIntent().getStringExtra("name");
        userProfileImage = getIntent().getStringExtra("userProfileImage");


        // setters
        nameView.setText(name);
        Picasso.get().load(userProfileImage).into(userProfileImageView);

    }
}