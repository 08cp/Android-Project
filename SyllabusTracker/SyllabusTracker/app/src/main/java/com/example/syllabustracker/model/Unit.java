package com.example.syllabustracker.model;

import org.json.JSONArray;

public class Unit {
    private String name;
    private int progress;
    private int unit_id;
    private int subject_id;
    private JSONArray topics;

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

    public int getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(int unit_id) {
        this.unit_id = unit_id;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public JSONArray getTopics() {
        return topics;
    }

    public void setTopics(JSONArray topics) {
        this.topics = topics;
    }

    public Unit(String name, int progress, int unit_id, int subject_id, JSONArray topics) {
        this.name = name;
        this.progress = progress;
        this.unit_id = unit_id;
        this.subject_id = subject_id;
        this.topics = topics;
    }
}
