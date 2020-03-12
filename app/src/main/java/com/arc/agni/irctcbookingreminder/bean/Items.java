package com.arc.agni.irctcbookingreminder.bean;

public class Items {

    private String summary;
    private Start start;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Start getStart() {
        return start;
    }

    public void setStart(Start start) {
        this.start = start;
    }

    @Override
    public String toString() {
        return summary;
    }
}
