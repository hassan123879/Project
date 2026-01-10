package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button; // Good practice to import the specific class

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.LoadAdError;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    // Declare the variable as a Button for type safety
    Button btnChatbot;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Moved to the top (best practice)
        setContentView(R.layout.activity_main);

        // 1. Initialize Mobile Ads SDK first
        MobileAds.initialize(this, initializationStatus -> {});

        // 2. Create the ad request ONCE
        AdRequest adRequest = new AdRequest.Builder().build();

        // 3. Load Interstitial Ad
        InterstitialAd.load(this,
                "ca-app-pub-3940256099942544/1033173712",
                adRequest, // Using the variable defined above
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        // Optional: Show it immediately once loaded if you wish
                        // mInterstitialAd.show(MainActivity.this);
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        mInterstitialAd = null;
                    }
                });

        // 4. Load Banner Ad (AdView)
        AdView adView = findViewById(R.id.adView);
        // Removed the redundant "AdRequest adRequest = ..." line from here
        adView.loadAd(adRequest);

        // 5. Initialize other components
        auth = FirebaseAuth.getInstance();
        btnChatbot = findViewById(R.id.btnChatbot);
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
