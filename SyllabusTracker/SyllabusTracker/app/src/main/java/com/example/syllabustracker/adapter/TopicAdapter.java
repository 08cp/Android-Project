package com.example.syllabustracker.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.syllabustracker.MainActivity;
import com.example.syllabustracker.R;
import com.example.syllabustracker.loginActivity;
import com.example.syllabustracker.model.Topic;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private Context context;
    private List<Topic> topics;
    private ProgressDialog progressDialog;
    RequestQueue requestQueue;
    String student_id,topicid;


    public TopicAdapter(Context context, List<Topic> topics) {
        this.context = context;
        this.topics = topics;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_topic, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Topic topic = topics.get(position);
        holder.topicNameTextView.setText(topic.getName());
        holder.topicCheckbox.setChecked(topic.isCovered());
        topicid = String.valueOf(topic.getTopicid());

        // Update checkbox state based on current data item's properties
        holder.topicCheckbox.setOnCheckedChangeListener(null); // Remove listener temporarily to prevent unwanted triggering

        SharedPreferences sharedPreferences = context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);

        // Retrieving login details
        student_id = sharedPreferences.getString("student_id", "");

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false); // Prevent dismissing by tapping outside of the dialog
        requestQueue = Volley.newRequestQueue(context);

        holder.topicCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateProgress(topic, isChecked, holder.topicCheckbox, position);
                progressDialog.dismiss();
            }
        });

        holder.topicnotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(topic.getNotes_link()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });

        holder.topicselfnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNoteDialog(context, String.valueOf(topic.getTopicid()),topic.getNote(),topic);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    static class TopicViewHolder extends RecyclerView.ViewHolder {
        TextView topicNameTextView;
        CheckBox topicCheckbox;
        Button topicnotes, topicselfnote;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            topicNameTextView = itemView.findViewById(R.id.topicNameTextView);
            topicCheckbox = itemView.findViewById(R.id.topicCheckbox);
            topicnotes = itemView.findViewById(R.id.topicnotes);
            topicselfnote = itemView.findViewById(R.id.topicselfnote);
        }
    }

    private void showAddNoteDialog(Context context, String topicidd,String getnote,Topic topic) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_self_note, null);

        Dialog dialog = new Dialog(context);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        EditText editTextNote = view.findViewById(R.id.editTextNote);
        Button buttonSaveNote = view.findViewById(R.id.buttonSaveNote);
        Button buttonEditNote = view.findViewById(R.id.buttonEditNote);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageView closedialog = view.findViewById(R.id.close_box);
        editTextNote.setText(getnote);

        buttonEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To set EditText as editable
                editTextNote.setEnabled(true); // Enable editing
                editTextNote.setFocusable(true); // Allow focus
                editTextNote.setFocusableInTouchMode(true); // Allow focus in touch mode
            }
        });

        buttonSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = editTextNote.getText().toString();
                progressDialog.show();
                JSONObject postData = new JSONObject();
                try {
                    postData.put("student_id", student_id);
                    postData.put("topic_id", topicidd);
                    postData.put("note", note);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("kalki", "PostData: " + postData.toString());
                String url = "https://projectstore.vip/demo/syllabustracker/api/selfnote.php";

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Handle the response
                                Log.d("kalki", response.toString());
                                try {
                                    if (response.has("message")) {
                                        editTextNote.setEnabled(false); // Disable editing
                                        editTextNote.setFocusable(false); // Remove focus
                                        editTextNote.setFocusableInTouchMode(false); // Remove focus from touch mode
                                        Toast.makeText(context, "Save Successfully.", Toast.LENGTH_SHORT).show();
                                        topic.setNote(note);
                                        progressDialog.hide();
                                    } else {
                                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
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
                                Log.e("kalki", "Error occurred", error);
                                progressDialog.hide();
                            }
                        });
                requestQueue.add(jsonObjectRequest);
            }
        });

        closedialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void updateProgress(Topic topic, boolean isChecked, CheckBox checkBox, int position) {
        progressDialog.show();
        JSONObject postData = new JSONObject();
        try {
            postData.put("student_id", student_id);
            postData.put("topic_id", topic.getTopicid());
            postData.put("progress", isChecked ? 1 : 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "https://projectstore.vip/demo/syllabustracker/api/progress.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("kalki", response.toString());
                        progressDialog.dismiss();
                        try {
                            if (response.has("message")) {
                                Toast.makeText(context, "Update Successfully.", Toast.LENGTH_SHORT).show();
                                topics.get(position).setCovered(isChecked); // Update the correct topic's covered state
                                checkBox.setChecked(isChecked); // Update the checkbox state directly
                            } else {
                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("kalki", "Error occurred", error);
                        progressDialog.dismiss();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
}
