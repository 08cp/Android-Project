package com.example.syllabustracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class splashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Retrieving login details
        String student_id = sharedPreferences.getString("student_id", "");
        String name = sharedPreferences.getString("name", "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // on below line we are
                // creating a new intent
                if (!student_id.isEmpty() && !name.isEmpty()) {
                    startActivity(new Intent(splashActivity.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(splashActivity.this, loginActivity.class));
                    finish();
                }
            }
        }, 2000);

    }
}