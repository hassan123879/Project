package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }

    public void login(View view) {
        auth.signInWithEmailAndPassword(
                email.getText().toString(),
                password.getText().toString()
        ).addOnSuccessListener(authResult -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }).addOnFailureListener(e ->
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public void signup(View view) {
        startActivity(new Intent(this, SignupActivity.class));
    }

    public void forgot(View view) {
        startActivity(new Intent(this, ForgotPasswordActivity.class));
    }
}
