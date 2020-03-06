package com.arc.agni.irctcbookingreminder.bean;

import android.view.ViewGroup;

import com.arc.agni.irctcbookingreminder.activities.ViewRemindersActivity;

import java.util.Comparator;

import static com.arc.agni.irctcbookingreminder.constants.Constants.SORT_BY_REMINDER_DATE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.SORT_BY_TRAVEL_DATE;

public class Event implements Comparator {

    private String eventID;
    private String eventTitle;
    private String eventType;
    private String reminderDate;
    private String travelDate;

    public Event() {
    }

    public Event(String eventID, String eventTitle, String eventType, String reminderDate, String travelDate) {
        this.eventID = eventID;
        this.eventTitle = eventTitle;
        this.eventType = eventType;
        this.reminderDate = reminderDate;
        this.travelDate = travelDate;
    }

    @Override
    public int compare(Object o1, Object o2) {
        Event event_one = (Event) o1;
        Event event_two = (Event) o2;
        if (ViewRemindersActivity.sortByParamIndicator == SORT_BY_REMINDER_DATE) {
            return (event_one.getReminderDate()).compareTo(event_two.getReminderDate());
        } else if (ViewRemindersActivity.sortByParamIndicator == SORT_BY_TRAVEL_DATE) {
            return (event_one.getTravelDate()).compareTo(event_two.getTravelDate());
        }
        return 1;
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
