package com.arc.agni.irctcbookingreminder.bean;

import com.arc.agni.irctcbookingreminder.activities.ViewRemindersActivity;

import java.util.Comparator;

import static com.arc.agni.irctcbookingreminder.constants.Constants.SORT_BY_REMINDER_DATE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.SORT_BY_TRAVEL_DATE;

public class Event {

    private String eventID;
    private String eventTitle;
    private String eventType;
    private String reminderDate;
    private String travelDate;

    public Event() {
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(String reminderDate) {
        this.reminderDate = reminderDate;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(String travelDate) {
        this.travelDate = travelDate;
    }
}
