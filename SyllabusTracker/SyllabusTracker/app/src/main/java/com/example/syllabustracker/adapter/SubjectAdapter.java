package com.example.syllabustracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.syllabustracker.R;
import com.example.syllabustracker.model.Subject;
import com.example.syllabustracker.showUnitsActivity;

import java.io.Serializable;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private Context context;
    private List<Subject> subjects;

    public SubjectAdapter(Context context, List<Subject> subjects) {
        this.context = context;
        this.subjects = subjects;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject subject = subjects.get(position);
        holder.subjectNameTextView.setText(subject.getName());
        holder.progressBar.setProgress(subject.getProgress());
        holder.progresstext.setText(subject.getProgress()+"%");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, showUnitsActivity.class);
                intent.putExtra("subjectName",subject.getName());
                intent.putExtra("units","{ \"units\":"+ subject.getUnits().toString()+"}");
                intent.putExtra("subjectid", subject.getSubject_id());
                intent.putExtra("sem", subject.getSem());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView subjectNameTextView,progresstext;
        ProgressBar progressBar;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectNameTextView = itemView.findViewById(R.id.subjectNameTextView);
            progressBar = itemView.findViewById(R.id.progressBar);
            progresstext = itemView.findViewById(R.id.progresstext);
        }
    }
}
