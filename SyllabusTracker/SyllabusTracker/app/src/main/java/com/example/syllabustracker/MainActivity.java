package com.example.syllabustracker;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.example.syllabustracker.adapter.TopicAdapter;
import com.example.syllabustracker.model.Topic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView btnlogout;
    CardView showsubjectbtn,viewgoalbox;
    TextView nameuser,emailuser,overalltext,btnrestgoal;
    private ProgressDialog progressDialog;
    ProgressBar progressBarOverallProgress;
    RequestQueue requestQueue;
    String student_id;
    String url = "https://projectstore.vip/demo/syllabustracker/api/total_progress.php";
    String url2 = "https://projectstore.vip/demo/syllabustracker/api/setgoal.php";
    String url3 = "https://projectstore.vip/demo/syllabustracker/api/gettimer.php";
    ScrollView setgoalbox;
    TextView viewgoaldaysavaialble,viewgoaldaysremainingtopics,viewgoaldaystopicsperday;

    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private Button btnGetDates;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false); // Prevent dismissing by tapping outside of the dialog

        startDatePicker = findViewById(R.id.startDatePicker);
        btnrestgoal = findViewById(R.id.btnrestgoal);
        endDatePicker = findViewById(R.id.endDatePicker);
        btnGetDates = findViewById(R.id.btnGetDates);
        setgoalbox = findViewById(R.id.setgoalbox);
        viewgoalbox = findViewById(R.id.viewgoalbox);
        viewgoaldaystopicsperday = findViewById(R.id.viewgoaldaystopicsperday);
        viewgoaldaysremainingtopics = findViewById(R.id.viewgoaldaysremainingtopics);
        viewgoaldaysavaialble = findViewById(R.id.viewgoaldaysavaialble);

        overalltext = findViewById(R.id.overalltext);
        progressBarOverallProgress = findViewById(R.id.progressBarOverallProgress);
        showsubjectbtn = findViewById(R.id.showsubjectbtn);
        showsubjectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, semesterActivity.class));
            }
        });
        btnlogout = findViewById(R.id.btnlogout);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("student_id", null);
                editor.putString("email", null);
                editor.putString("name",null);
                editor.apply();
                startActivity(new Intent(MainActivity.this, loginActivity.class));
                finish();
            }
        });

        emailuser = findViewById(R.id.emailuser);
        nameuser = findViewById(R.id.nameuser);
        String name = sharedPreferences.getString("name", "");
        String email = sharedPreferences.getString("email", "");
        student_id = sharedPreferences.getString("student_id", "");
        emailuser.setText(email);
        nameuser.setText(name);

        getGoal();

        btnGetDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected start date
                int startDay = startDatePicker.getDayOfMonth();
                int startMonth = startDatePicker.getMonth()+1;
                int startYear = startDatePicker.getYear();

                // Get the selected end date
                int endDay = endDatePicker.getDayOfMonth();
                int endMonth = endDatePicker.getMonth()+1;
                int endYear = endDatePicker.getYear();

                String startdate = startYear+"/"+startMonth+"/"+startDay;
                String enddate = endYear+"/"+endMonth+"/"+endDay;

                setGoal(startdate,enddate);
            }
        });

        btnrestgoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewgoalbox.setVisibility(View.GONE);
                setgoalbox.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getprogress();
        getGoal();
    }

    void getprogress(){
        JSONObject postData = new JSONObject();
        try {
            postData.put("student_id", student_id);
            // Add other required parameters if any
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("kalki", "onResponse: "+response);
                        // Handle the response
                        try {
                            progressBarOverallProgress.setProgress(response.getInt("progress"));
                            overalltext.setText(response.getInt("progress")+"%");
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

    void getGoal(){
        JSONObject postData = new JSONObject();
        try {
            postData.put("student_id", student_id);
            // Add other required parameters if any
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url3, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("kalki", "onResponse: "+response);
                        // Handle the response
                        try {
                           if(response.getBoolean("isSet")){
                               viewgoaldaysavaialble.setText(response.getString("total_days_available"));
                               viewgoaldaysremainingtopics.setText(response.getString("remaining_topics"));
                               viewgoaldaystopicsperday.setText(response.getString("topics_per_day"));
                               viewgoalbox.setVisibility(View.VISIBLE);
                               setgoalbox.setVisibility(View.GONE);
                           }else{
                               viewgoalbox.setVisibility(View.GONE);
                               setgoalbox.setVisibility(View.VISIBLE);
                           }
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

    void setGoal(String start,String end){
        JSONObject postData = new JSONObject();
        try {
            postData.put("student_id", student_id);
            postData.put("start", start);
            postData.put("end", end);
            // Add other required parameters if any
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url2, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("kalki", "onResponse: "+response);
                        // Handle the response
                        try {
                           if(response.has("message")){
                               Toast.makeText(MainActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                               getGoal();
                           }else{
                               Toast.makeText(MainActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                               getGoal();
                           }
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
