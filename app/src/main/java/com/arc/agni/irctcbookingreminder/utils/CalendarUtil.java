package com.arc.agni.irctcbookingreminder.utils;

import android.content.ContentValues;
import android.provider.CalendarContract;

import com.arc.agni.irctcbookingreminder.activities.CustomReminderActivity;

import java.util.Calendar;
import java.util.TimeZone;

public class CalendarUtil {

    public static final String MY_ACCOUNT_NAME = "IRCTC Booking Reminder";

    public ContentValues setCalendarContentValues(){

        ContentValues values = new ContentValues();
        values.put(
                CalendarContract.Calendars.ACCOUNT_NAME,
                MY_ACCOUNT_NAME);
        values.put(
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.ACCOUNT_TYPE_LOCAL);
        values.put(
                CalendarContract.Calendars.NAME,
                "IRCTC Reminder Calendar");
        values.put(
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                "IRCTC Reminder Calendar");
        values.put(
                CalendarContract.Calendars.CALENDAR_COLOR,
                0xffff0000);
        values.put(
                CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
                CalendarContract.Calendars.CAL_ACCESS_OWNER);
        values.put(
                CalendarContract.Calendars.OWNER_ACCOUNT,
                "abc@xyz.com");
        values.put(
                CalendarContract.Calendars.CALENDAR_TIME_ZONE,
                "");
        values.put(
                CalendarContract.Calendars.SYNC_EVENTS,
                1);
        return values;
    }

    public ContentValues setEventContentValues(long calendarID, Calendar reminderDateTime, Calendar exDate, String title, String reminderType){
        ContentValues values = new ContentValues();
        TimeZone timeZone = TimeZone.getDefault();
        long startMillis = reminderDateTime.getTimeInMillis();
        reminderDateTime.add(Calendar.MINUTE, 25);
        long endMillis = reminderDateTime.getTimeInMillis();
        long exMillis = exDate.getTimeInMillis();

        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, reminderType);
        values.put(CalendarContract.Events.CALENDAR_ID, calendarID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        if(reminderType.equalsIgnoreCase(CustomReminderActivity.reminderType)) {
            values.put(CalendarContract.Events.EXDATE, exMillis);
        }
        return values;
    }

    public ContentValues setReminderContentValues(long eventID){
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, eventID);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        values.put(CalendarContract.Reminders.MINUTES, 60);
        return values;
    }
}
