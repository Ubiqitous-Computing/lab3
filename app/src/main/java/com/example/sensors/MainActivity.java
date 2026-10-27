package com.example.sensors;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import android.Manifest;
import android.os.Build;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button startButton;
    Button stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Asks user for permission if it already hasn't been granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        100
                );
            }
        }

        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);

        //Listener for StopButton
        stopButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MotionService.class);
            stopService(intent);
        });

        // Listener for StartButton
        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MotionService.class);
            startService(intent);
        });
    }
}
