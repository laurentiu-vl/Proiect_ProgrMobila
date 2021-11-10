package com.example.frankthefrog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Button playButton, optionsButton, leaderboards;
    private final int REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);

        // Init buttons
        playButton = findViewById(R.id.playButton);
        optionsButton = findViewById(R.id.optionsButton);
        leaderboards = findViewById(R.id.leaderboardsButton);

        // Listeners
        playButton.setOnClickListener(view -> {

        });

        optionsButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Options.class);
            startActivityForResult(intent, REQUEST_CODE);
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE) {
            assert data != null;
            boolean hasMusic = data.getBooleanExtra("sound", false);
            boolean hasSound = data.getBooleanExtra("music", false);
            Log.d("Main", "hasMusic: " + hasMusic);
            Log.d("Main", "hasSound: " + hasSound);
            Toast.makeText(MainActivity.this, "Back to the main activity", Toast.LENGTH_LONG).show();
        }
    }
}