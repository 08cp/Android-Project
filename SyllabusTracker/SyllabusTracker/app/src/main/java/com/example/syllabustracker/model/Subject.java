package com.example.syllabustracker.model;

import org.json.JSONArray;
import org.json.JSONObject;

public class Subject {
    private String name;
    private int progress;
    private int subject_id;
    private int sem;
    private JSONArray units;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public JSONArray getUnits() {
        return units;
    }

    public void setUnits(JSONArray units) {
        this.units = units;
    }

    public int getSem() {
        return sem;
    }

    public void setSem(int sem) {
        this.sem = sem;
    }

    public Subject(String name, int progress, int subject_id, JSONArray units, int sem) {
        this.name = name;
        this.progress = progress;
        this.subject_id = subject_id;
        this.units = units;
        this.sem = sem;
    }
}

