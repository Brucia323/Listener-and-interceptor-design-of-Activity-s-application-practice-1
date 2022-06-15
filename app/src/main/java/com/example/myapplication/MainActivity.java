//MainActivity.java
package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button startButton, stopButton;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = findViewById(R.id.start);
        stopButton = findViewById(R.id.stop);
        startButton.setOnClickListener(v -> {
            intent = new Intent(MainActivity.this, MyService.class);
            MainActivity.this.startService(intent);
        });
        stopButton.setOnClickListener(v -> MainActivity.this.stopService(intent));
    }
}