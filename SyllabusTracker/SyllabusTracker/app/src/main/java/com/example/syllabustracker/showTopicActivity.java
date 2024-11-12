package com.example.syllabustracker;

import android.annotation.SuppressLint;
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

import com.example.syllabustracker.adapter.TopicAdapter;
import com.example.syllabustracker.model.Topic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class showTopicActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TopicAdapter topicAdapter;
    private List<Topic> topics;
    TextView unitnameview;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_topic);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        topics = new ArrayList<>();
        topicAdapter = new TopicAdapter(this, topics);
        recyclerView.setAdapter(topicAdapter);

        unitnameview= findViewById(R.id.unitnameview);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String topics = extras.getString("topics");
            Log.d("kalki", "onCreate: "+topics);
            String name = extras.getString("unitName");
            unitnameview.setText(name+" All Topics");
            parseJsonAndPopulateTopics(topics);
        }
        // Parse JSON data and populate topics list

    }

    private void parseJsonAndPopulateTopics(String jsonData) {
//        String jsonData = "{ \"topics\": [{ \"name\": \"Equations\", \"completed\": true }, { \"name\": \"Inequalities\", \"completed\": false }] }";

        try {
            JSONObject root = new JSONObject(jsonData);

            JSONArray topicsArray = root.getJSONArray("topics");
            for (int k = 0; k < topicsArray.length(); k++) {
                JSONObject topicObject = topicsArray.getJSONObject(k);
                String topicName = topicObject.getString("name");
                String notes_link = topicObject.getString("notes_link");
                String note = topicObject.getString("note");
                int topic_id = topicObject.getInt("topic_id");
                boolean completed = topicObject.getBoolean("completed");
                Topic topic = new Topic(topicName, topic_id,completed,notes_link,note);
                topics.add(topic);

            }
            topicAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}