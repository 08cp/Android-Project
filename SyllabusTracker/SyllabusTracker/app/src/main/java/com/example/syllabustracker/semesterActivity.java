package com.example.syllabustracker;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class semesterActivity extends AppCompatActivity {
    CardView showsubjectbtnacadmiccal,showsubjectbtn5th,showsubjectbtn6th;
    String pdfFileName = "https://projectstore.vip/demo/syllabustracker/api/Academic_calendar.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_semester);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        showsubjectbtnacadmiccal = findViewById(R.id.showsubjectbtnacadmiccal);
        showsubjectbtn6th = findViewById(R.id.showsubjectbtn6th);
        showsubjectbtn5th = findViewById(R.id.showsubjectbtn5th);
        showsubjectbtnacadmiccal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPdfFromUrl(pdfFileName);
            }
        });

        showsubjectbtn5th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(semesterActivity.this, showSubjectsActivity.class);
                i.putExtra("sem","5");
                startActivity(i);
            }
        });
        showsubjectbtn6th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(semesterActivity.this, showSubjectsActivity.class);
                i.putExtra("sem","6");
                startActivity(i);
            }
        });
    }

    private void openPdfFromUrl(String pdfUrl) {
        Uri uri = Uri.parse(pdfUrl);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "No application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }
}