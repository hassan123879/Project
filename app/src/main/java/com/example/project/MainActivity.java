package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button; // Good practice to import the specific class

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    // Declare the variable as a Button for type safety
    Button btnChatbot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        // Initialize the button from your layout
        btnChatbot = findViewById(R.id.btnChatbot);

        // Set the click listener here, inside onCreate
        btnChatbot.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ChatbotActivity.class));
        });
    }

    public void scanQR(View view) {
        startActivity(new Intent(MainActivity.this, QRScannerActivity.class));
    }

    // REMOVED the problematic code from here

    public void logout(View view) {
        auth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}
