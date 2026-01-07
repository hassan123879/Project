package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    EditText email, password;
    FirebaseAuth auth;

    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }

    public void signup(View view) {
        auth.createUserWithEmailAndPassword(
                email.getText().toString(),
                password.getText().toString()
        ).addOnSuccessListener(authResult -> {
            Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e ->
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
