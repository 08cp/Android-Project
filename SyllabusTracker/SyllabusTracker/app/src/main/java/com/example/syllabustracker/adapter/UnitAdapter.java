package com.example.syllabustracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.syllabustracker.MainActivity;
import com.example.syllabustracker.R;
import com.example.syllabustracker.mcqtestActivity;
import com.example.syllabustracker.model.Unit;
import com.example.syllabustracker.showTopicActivity;
import com.example.syllabustracker.showUnitsActivity;

import java.util.List;

public class UnitAdapter extends RecyclerView.Adapter<UnitAdapter.UnitViewHolder> {

    private Context context;
    private List<Unit> units;

    public UnitAdapter(Context context, List<Unit> units) {
        this.context = context;
        this.units = units;
    }

    @NonNull
    @Override
    public UnitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_unit, parent, false);
        return new UnitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnitViewHolder holder, int position) {
        Unit unit = units.get(position);
        holder.unitNameTextView.setText(unit.getName());
        holder.progressBar.setProgress(unit.getProgress());
        holder.progresstext.setText(unit.getProgress()+"%");

        if(unit.getProgress() == 100){
            holder.btntest.setVisibility(View.VISIBLE);
        }
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, showTopicActivity.class);
                intent.putExtra("unitName",unit.getName());
                intent.putExtra("topics","{ \"topics\":"+ unit.getTopics().toString()+"}");
                context.startActivity(intent);
            }
        });
        holder.btntest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, mcqtestActivity.class);
                intent.putExtra("unit_id",unit.getUnit_id());
                intent.putExtra("subject_id",unit.getSubject_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return units.size();
    }

    static class UnitViewHolder extends RecyclerView.ViewHolder {
        TextView unitNameTextView,progresstext;
        ProgressBar progressBar;
        Button btntest;

        public UnitViewHolder(@NonNull View itemView) {
            super(itemView);
            unitNameTextView = itemView.findViewById(R.id.unitNameTextView);
            progressBar = itemView.findViewById(R.id.progressBar);
            btntest = itemView.findViewById(R.id.btntest);
            progresstext = itemView.findViewById(R.id.progresstext);
        }
    }
}
