package com.example.syllabustracker;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
import com.example.syllabustracker.adapter.UnitAdapter;
import com.example.syllabustracker.model.Subject;
import com.example.syllabustracker.model.Unit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class showUnitsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UnitAdapter unitAdapter;
    private ArrayList<Unit> unitsList;
    TextView subviewname;
    int subject_id,sem;
    String student_id;
    private ProgressDialog progressDialog;
    String url = "https://projectstore.vip/demo/syllabustracker/api/get_syllabustracker.php";
    RequestQueue requestQueue;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_units);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        unitsList = new ArrayList<>();
        unitAdapter = new UnitAdapter(this, unitsList);
        recyclerView.setAdapter(unitAdapter);


        requestQueue = Volley.newRequestQueue(showUnitsActivity.this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false); // Prevent dismissing by tapping outside of the dialog

        // Initialize SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Retrieving login details
        student_id = sharedPreferences.getString("student_id", "");

        subviewname = findViewById(R.id.subviewname);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String units = extras.getString("units");
            String name = extras.getString("subjectName");
            subject_id = extras.getInt("subjectid");
            sem = extras.getInt("sem");
            subviewname.setText(name+" All Units");
//            parseJsonAndPopulateUnits(units);
        }

    }

    private void parseJsonAndPopulateUnits(JSONArray unitsArray) {

        try {
//            JSONObject root = new JSONObject(jsonData);
//            JSONArray unitsArray = root.getJSONArray("units");
            for (int i = 0; i < unitsArray.length(); i++) {
                JSONObject unitObject = unitsArray.getJSONObject(i);
                String name = unitObject.getString("name");
                int progress = unitObject.getInt("progress");
                int unit_id = unitObject.getInt("unit_id");
                JSONArray topics = unitObject.getJSONArray("topics");
                Unit unit = new Unit(name, progress,unit_id,subject_id,topics);
                unitsList.add(unit);
            }
            unitAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // This method finds a subject by its ID in the JSON data
    private JSONObject findSubjectById(String jsonData, String subjectId) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray subjectsArray = jsonObject.getJSONArray("subjects");

            // Iterate through the subjects array to find the subject with the given ID
            for (int i = 0; i < subjectsArray.length(); i++) {
                JSONObject subject = subjectsArray.getJSONObject(i);
                String currentSubjectId = subject.getString("subject_id");

                // Check if the current subject's ID matches the target subject ID
                if (currentSubjectId.equals(subjectId)) {
                    return subject; // Found the subject, return it
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null; // Return null if subject not found
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressDialog.show();

        JSONObject postData = new JSONObject();
        try {
            postData.put("student_id", student_id);
            postData.put("sem", sem);
            // Add other required parameters if any
        } catch (JSONException e) {
            e.printStackTrace();
        }
        unitsList.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("kalki", "onResponse: "+response);
                        // Handle the response
                        JSONObject jsonObject = findSubjectById(response.toString(), String.valueOf(subject_id));
                        Log.d("kalki", "jsonObject: "+jsonObject);
                        try {
                            parseJsonAndPopulateUnits(jsonObject.getJSONArray("units"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
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