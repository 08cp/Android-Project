package com.example.syllabustracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class mcqtestActivity extends AppCompatActivity implements View.OnClickListener{

    TextView totalQuestionsTextView;
    TextView questionTextView;
    Button ansA, ansB, ansC, ansD;
    Button submitBtn;


    private String[] question;
    private String[][] choices;
    private String[] correctAnswers;

    int score=0;
    public  int totalQuestion = 0;
//    int totalQuestion = question.length;
    int currentQuestionIndex = 0;
    String selectedAnswer = "";
    int subject_id,unit_id;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mcqtest);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize UI elements
        totalQuestionsTextView = findViewById(R.id.total_question);
        questionTextView = findViewById(R.id.question);
        ansA = findViewById(R.id.ans_A);
        ansB = findViewById(R.id.ans_B);
        ansC = findViewById(R.id.ans_C);
        ansD = findViewById(R.id.ans_D);
        submitBtn = findViewById(R.id.submit_btn);

        ansA.setOnClickListener(this);
        ansB.setOnClickListener(this);
        ansC.setOnClickListener(this);
        ansD.setOnClickListener(this);
        submitBtn.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            subject_id = extras.getInt("subject_id");
            unit_id = extras.getInt("unit_id");
        }

        JSONObject params = new JSONObject();
        try {
            params.put("subject_id", subject_id);
            params.put("unit_id", unit_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://projectstore.vip/demo/syllabustracker/api/mcq.php";
        // Request a JSON response from the provided URL
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("kalki", "response: "+response);
                        try {
                            // Extract JSON array from the response
                            JSONArray jsonArray = response.getJSONArray("Data");
                            Log.d("kalki", "jsonArray: "+jsonArray);
                            totalQuestion = jsonArray.length();
                            totalQuestionsTextView.setText("Total questions : "+jsonArray.length());
                            // Initialize arrays based on the size of the JSON array
                            question = new String[jsonArray.length()];
                            choices = new String[jsonArray.length()][4];
                            correctAnswers = new String[jsonArray.length()];

                            // Loop through the JSON array to extract data
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                question[i] = jsonObject.getString("question");
                                choices[i][0] = jsonObject.getString("ans1");
                                choices[i][1] = jsonObject.getString("ans2");
                                choices[i][2] = jsonObject.getString("ans3");
                                choices[i][3] = jsonObject.getString("ans4");
                                correctAnswers[i] = jsonObject.getString("correct_ans");
                            }

                            // Now you can use your arrays with the dynamically assigned values
                            // For example, you can display them in a ListView or RecyclerView

                            loadNewQuestion();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("kalki", "Volley Error: " + error.getMessage());
            }
        });

        // Add the request to the RequestQueue
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onClick(View view) {

        ansA.setBackgroundColor(Color.WHITE);
        ansB.setBackgroundColor(Color.WHITE);
        ansC.setBackgroundColor(Color.WHITE);
        ansD.setBackgroundColor(Color.WHITE);

        Button clickedButton = (Button) view;
        if(clickedButton.getId()==R.id.submit_btn){
            if(selectedAnswer.equals(correctAnswers[currentQuestionIndex])){
                score++;
            }
            currentQuestionIndex++;
            loadNewQuestion();


        }else{
            //choices button clicked
            selectedAnswer  = clickedButton.getText().toString();
            clickedButton.setBackgroundColor(Color.MAGENTA);

        }
    }

    void loadNewQuestion(){

        if(currentQuestionIndex == totalQuestion ){
            finishQuiz();
            return;
        }

        questionTextView.setText(question[currentQuestionIndex]);
        ansA.setText(choices[currentQuestionIndex][0]);
        ansB.setText(choices[currentQuestionIndex][1]);
        ansC.setText(choices[currentQuestionIndex][2]);
        ansD.setText(choices[currentQuestionIndex][3]);

    }




    void finishQuiz(){
        String passStatus = "";
        if(score > totalQuestion*0.60){
            passStatus = "Passed";
        }else{
            passStatus = "Failed";
        }

        new AlertDialog.Builder(this)
                .setTitle(passStatus)
                .setMessage("Score is "+ score+" out of "+ totalQuestion)
                .setPositiveButton("Restart",(dialogInterface, i) -> restartQuiz() )
                .setCancelable(false)
                .show();


    }
    void restartQuiz(){
        score = 0;
        currentQuestionIndex =0;
        loadNewQuestion();
    }
}