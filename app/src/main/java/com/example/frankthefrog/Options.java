package com.example.frankthefrog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.Objects;

public class Options extends AppCompatActivity {
    private CheckBox sound, music, statistics, skip;
    private Button back;
    private boolean hasMusic = false, hasSound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_options);

        // Init Checkboxes
        sound = findViewById(R.id.soundOption);
        music = findViewById(R.id.musicOption);
        statistics = findViewById(R.id.statisticsBarOption);
        skip = findViewById(R.id.disableSkipButtonOption);

        // Init Button
        back = findViewById(R.id.optionsBackButton);

        // Click Listeners
        sound.setOnClickListener(view -> {
            hasSound = sound.isChecked();
        });

        music.setOnClickListener(view -> {
            hasMusic = music.isChecked();
        });

        back.setOnClickListener(view -> {
            Intent intent = getIntent();
            intent.putExtra("sound", hasSound);
            intent.putExtra("music", hasMusic);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}