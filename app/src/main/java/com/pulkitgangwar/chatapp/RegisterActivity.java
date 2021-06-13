package com.pulkitgangwar.chatapp;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    EditText emailInput,nameInput,passwordInput,confirmPasswordInput;
    Button registerButton;
    String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    CircleImageView profileImage;
    TextView loginLink;
    Uri imageUri;
    Uri downloadedImageUri;


//    CONSTANTS
    final int REQUEST_CODE_PICK_IMAGE = 100;

//    FIREBASE
    FirebaseDatabase database;
    FirebaseStorage storage;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailInput = findViewById(R.id.register__email);
        nameInput = findViewById(R.id.register__name);
        passwordInput = findViewById(R.id.register__password);
        confirmPasswordInput = findViewById(R.id.register__confirmpassword);
        registerButton = findViewById(R.id.register__submit);
        profileImage = findViewById(R.id.register__profile);
        loginLink = findViewById(R.id.register__link);
        Pattern emailPattern = Pattern.compile(emailRegex);


//        firebase instances
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_URL);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString();
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                String confirmPassword = confirmPasswordInput.getText().toString();


                if(email.isEmpty() || password.isEmpty() || name.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this,"Please Enter valid data",Toast.LENGTH_SHORT).show();
                    nameInput.setError("empty");
                    emailInput.setError("empty");
                    passwordInput.setError("empty");
                    confirmPasswordInput.setError("empty");
                } else if(!emailPattern.matcher(email).matches()) {
                    Toast.makeText(RegisterActivity.this,"Invalid Email",Toast.LENGTH_SHORT).show();
                    emailInput.setError("Invalid Email");
                } else if(!password.equals(confirmPassword)) {
                    Log.d("oncreate",password);
                    Log.d("oncreate",confirmPassword);
                    passwordInput.setError("password do not match");
                    confirmPasswordInput.setError("password do not match");
                    Toast.makeText(RegisterActivity.this,"Password does not match",Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    passwordInput.setError("password should be 6 or more than 6 characters");
                    Toast.makeText(RegisterActivity.this,"Password should be 6 or more than 6 characters",Toast.LENGTH_SHORT).show();
                } else {
                        createUser(email,password,name);
                }
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),REQUEST_CODE_PICK_IMAGE);

            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }

     void createUser(String email,String password,String name) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    StorageReference storageReference = storage.getReference().child("userProfile").child(auth.getUid());
//                     store user profile image to firebase storage
                    if(imageUri != null) {
                        storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful()) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            downloadedImageUri = uri;
                                            storeUserInfo(email,name,downloadedImageUri.toString());
                                        }
                                    });
                                }
                            }
                        });
                    } else {
                        String defaultUrl = "https://firebasestorage.googleapis.com/v0/b/chatapp-478d8.appspot.com/o/user_profile.png?alt=media&token=5fdcee9a-4f70-4840-ae00-3e69621fff82";
                        storeUserInfo(email,name,defaultUrl);
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Registration Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void storeUserInfo(String email,String name,String originalImageUri) {
        DatabaseReference reference = database.getReference().child("users").child(auth.getUid());
        User user = new User(name,email,originalImageUri,auth.getUid());

        reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                } else {
                    Toast.makeText(RegisterActivity.this,"Something went wrong while saving user",Toast.LENGTH_LONG).show();
                }
            }
        });




    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_PICK_IMAGE) {
            if(data != null) {
                imageUri = data.getData();
                profileImage.setImageURI(imageUri);
            }
        }
    }
}