package com.pulkitgangwar.chatapp;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    TextView registerLink;
    EditText emailInput,passwordInput;
    Button submitButton;
    String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";


    // firebase
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // res instances
        registerLink = findViewById(R.id.login__link);
        emailInput = findViewById(R.id.login__email);
        passwordInput = findViewById(R.id.login__password);
        submitButton = findViewById(R.id.login__submit);
        Pattern emailPattern = Pattern.compile(emailRegex);


        // firebase instances
        auth = FirebaseAuth.getInstance();

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                if(email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    emailInput.setError("Fields cannot be empty");
                    passwordInput.setError("Fields cannot be empty");
                } else if(!emailPattern.matcher(email).matches()) {
                    Toast.makeText(LoginActivity.this,"Invalid Email",Toast.LENGTH_SHORT).show();
                    emailInput.setError("Invalid Email");
                } else {

                    loginUser(email,password);

                }

            }
        });
    }


    private void loginUser(String email,String password) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Login Credentials Incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}