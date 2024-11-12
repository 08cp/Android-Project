package com.example.syllabustracker;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.syllabustracker.adapter.SubjectAdapter;
import com.example.syllabustracker.model.Subject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class showSubjectsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SubjectAdapter subjectAdapter;
    private ArrayList<Subject> subjects;
    private ProgressDialog progressDialog;
    String url = "https://projectstore.vip/demo/syllabustracker/api/get_syllabustracker.php";
    RequestQueue requestQueue;
    String student_id;
    TextView heading;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_subjects);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        heading = findViewById(R.id.heading);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        subjects = new ArrayList<>();
        subjectAdapter = new SubjectAdapter(this, subjects);
        recyclerView.setAdapter(subjectAdapter);


        requestQueue = Volley.newRequestQueue(showSubjectsActivity.this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false); // Prevent dismissing by tapping outside of the dialog

        // Initialize SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Retrieving login details
        student_id = sharedPreferences.getString("student_id", "");
        Log.d("kalki", "student_id: "+student_id);




    }

    private void parseJsonAndPopulateSubjects(String jsonData,int sem) {
//        String jsonData = "{ \"subjects\": [{ \"name\": \"Operating System\", \"progress\": 50 }, { \"name\": \"Advance Java\", \"progress\": 30 }, { \"name\": \"Python\", \"progress\": 70 }] }";

        try {
            JSONObject root = new JSONObject(jsonData);
            JSONArray subjectsArray = root.getJSONArray("subjects");
            for (int i = 0; i < subjectsArray.length(); i++) {
                JSONObject subjectObject = subjectsArray.getJSONObject(i);
                String name = subjectObject.getString("name");
                int progress = subjectObject.getInt("progress");
                int subject_id = subjectObject.getInt("subject_id");
                JSONArray units = subjectObject.getJSONArray("units");
                Subject subject = new Subject(name, progress,subject_id, units,sem);
                subjects.add(subject);
            }
            subjectAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressDialog.show();
        String sem = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sem = extras.getString("sem");
            if(sem.equals("5")){
                heading.setText("5th Semester Subjects");
            }else{
                heading.setText("6th Semester Subjects");
            }
        }else{
            startActivity(new Intent(showSubjectsActivity.this,MainActivity.class));
            finish();
        }

        JSONObject postData = new JSONObject();
        try {
            postData.put("student_id", student_id);
            postData.put("sem", sem);
            // Add other required parameters if any
        } catch (JSONException e) {
            e.printStackTrace();
        }
        subjects.clear();
        String finalSem = sem;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("kalki", "onResponse: "+response);
                        // Handle the response
                        parseJsonAndPopulateSubjects(response.toString(), Integer.parseInt(finalSem));
                        progressDialog.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.e("Error", "Error occurred", error);
                        progressDialog.hide();
                    }
                });

// Add the request to the RequestQueue

        requestQueue.add(jsonObjectRequest);
    }
}