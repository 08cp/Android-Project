package com.example.syllabustracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class signupActivity extends AppCompatActivity {
    EditText editTextName,editTextEmail,editTextPassword;
    Button buttonSignup;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignup = findViewById(R.id.buttonSignup);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false); // Prevent dismissing by tapping outside of the dialog



        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isValidName(String.valueOf(editTextName.getText()))) {
                    editTextName.setError("Please enter a valid name");
                    return;
                }

                if (!isValidEmail(String.valueOf(editTextEmail.getText()))) {
                    editTextEmail.setError("Please enter a valid email address");
                    return;
                }

                if (!isValidPassword(String.valueOf(editTextPassword.getText()))) {
                    editTextPassword.setError("Password must be at least 6 characters long.");
                    return;
                }

                progressDialog.show();
                JSONObject postData = new JSONObject();
                try {
                    postData.put("name", editTextName.getText());
                    postData.put("password", editTextPassword.getText());
                    postData.put("email", editTextEmail.getText());
                    // Add other required parameters if any
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String url = "https://projectstore.vip/demo/syllabustracker/api/register.php";

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Handle the response
                                Log.d("Response", response.toString());
                                try {
                                    if(response.getString("status").equals("success")){
                                        Toast.makeText(signupActivity.this, "Registered Successfully PLease Login", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(signupActivity.this, loginActivity.class));
                                        finish();
                                        progressDialog.hide();
                                    }else{
                                        Toast.makeText(signupActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                        progressDialog.hide();
                                    }
                                } catch (JSONException e) {
                                    progressDialog.hide();
                                    throw new RuntimeException(e);
                                }
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
                RequestQueue requestQueue = Volley.newRequestQueue(signupActivity.this);
                requestQueue.add(jsonObjectRequest);
            }
        });
    }

    private boolean isValidName(String name) {
        return !name.isEmpty();
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        // Password must be at least 8 characters long and contain at least one letter and one number
        return password.length() == 6;
    }

}