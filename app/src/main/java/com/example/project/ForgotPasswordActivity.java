package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText email;

    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_forgot);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
    }

    public void reset(View view) {
        auth.sendPasswordResetEmail(email.getText().toString())
                .addOnSuccessListener(unused ->
                        Toast.makeText(this, "Reset email sent", Toast.LENGTH_SHORT).show());
    }
}
