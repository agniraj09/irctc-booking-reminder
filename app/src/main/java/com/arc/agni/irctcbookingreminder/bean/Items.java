package com.arc.agni.irctcbookingreminder.bean;

public class Items {

    private String monthLabel;
    private String day;
    private String summary;
    private Start start;

    public String getMonthLabel() {
        return monthLabel;
    }

    public void setMonthLabel(String monthLabel) {
        this.monthLabel = monthLabel;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

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
