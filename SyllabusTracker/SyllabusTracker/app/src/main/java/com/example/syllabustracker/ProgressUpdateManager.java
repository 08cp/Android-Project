package com.example.syllabustracker;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class ProgressUpdateManager {
    private static final String TAG = ProgressUpdateManager.class.getSimpleName();
    private static final String UPDATE_PROGRESS_URL = "http://your-api-url/update_progress";

    private Context context;
    private RequestQueue requestQueue;

    public ProgressUpdateManager(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void updateProgress(int studentId, int topicId, int progress) {
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("student_id", studentId);
            requestData.put("topic_id", topicId);
            requestData.put("progress", progress);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, UPDATE_PROGRESS_URL, requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Progress updated successfully");
                        // Handle response if needed
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error updating progress: " + error.getMessage());
                        // Handle error
                    }
                });

        requestQueue.add(request);
    }
}
