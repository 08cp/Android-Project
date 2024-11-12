package com.example.syllabustracker.model;

public class Topic {
    private String name;
    private String notes_link;
    private String note;
    private int topicid;
    private boolean covered;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTopicid() {
        return topicid;
    }

    public void setTopicid(int topicid) {
        this.topicid = topicid;
    }

    public boolean isCovered() {
        return covered;
    }

    public void setCovered(boolean covered) {
        this.covered = covered;
    }

    public String getNotes_link() {
        return notes_link;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Topic(String name, int topicid, boolean covered, String notes_link, String note) {
        this.name = name;
        this.topicid = topicid;
        this.covered = covered;
        this.notes_link = notes_link;
        this.note = note;
    }
}
