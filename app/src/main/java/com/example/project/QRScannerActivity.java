package com.example.project;

import android.os.Bundle;
import android.widget.Toast; // <-- Add this import

import androidx.camera.core.CameraSelector; // <-- Add this import
import androidx.camera.core.ImageAnalysis; // <-- Add this import
import androidx.camera.core.ImageProxy; // <-- Add this import
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat; // <-- Add this import

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner; // <-- Add this import
import com.google.mlkit.vision.barcode.BarcodeScanning; // <-- Add this import
import com.google.mlkit.vision.barcode.common.Barcode; // <-- Add this import
import com.google.mlkit.vision.common.InputImage; // <-- Add this import

import java.util.concurrent.ExecutionException; // <-- Add this import

public class QRScannerActivity extends AppCompatActivity {

    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This will now find your new layout file
        setContentView(R.layout.activity_qr);

        previewView = findViewById(R.id.previewView);
        startCamera();
    }

    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Set up the barcode scanner
                BarcodeScanner scanner = BarcodeScanning.getClient();

                // Set up the image analysis use case
                ImageAnalysis analysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                analysis.setAnalyzer(ContextCompat.getMainExecutor(this), imageProxy -> {
                    @androidx.camera.core.ExperimentalGetImage
                    android.media.Image mediaImage = imageProxy.getImage();
                    if (mediaImage != null) {
                        InputImage image = InputImage.fromMediaImage(
                                mediaImage,
                                imageProxy.getImageInfo().getRotationDegrees()
                        );

                        scanner.process(image)
                                .addOnSuccessListener(barcodes -> {
                                    for (Barcode barcode : barcodes) {
                                        String rawValue = barcode.getRawValue();
                                        // Display the scanned value in a Toast
                                        Toast.makeText(QRScannerActivity.this,
                                                rawValue,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    // Handle any errors here
                                })
                                .addOnCompleteListener(task -> {
                                    imageProxy.close();
                                });
                    }
                });

                // Unbind any existing use cases before rebinding
                cameraProvider.unbindAll();

                // Bind the camera to the lifecycle
                cameraProvider.bindToLifecycle(this,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        analysis,
                        new androidx.camera.core.Preview.Builder().build());

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }
}
