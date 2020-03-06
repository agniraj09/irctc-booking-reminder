package com.arc.agni.irctcbookingreminder.utils;

import android.content.ContentValues;
import android.provider.CalendarContract;

import java.util.Calendar;
import java.util.TimeZone;

import static com.arc.agni.irctcbookingreminder.constants.Constants.CALENDAR_ACCOUNT_NAME;
import static com.arc.agni.irctcbookingreminder.constants.Constants.CALENDAR_COLOR_LOCAL;
import static com.arc.agni.irctcbookingreminder.constants.Constants.CALENDAR_NAME;
import static com.arc.agni.irctcbookingreminder.constants.Constants.OWNER_ACCOUNT_ID;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_DURATION;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE_CUSTOM;

public class CalendarUtil {



    public ContentValues setCalendarContentValues(){

        ContentValues values = new ContentValues();
        values.put(
                CalendarContract.Calendars.ACCOUNT_NAME,
                CALENDAR_ACCOUNT_NAME);
        values.put(
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.ACCOUNT_TYPE_LOCAL);
        values.put(
                CalendarContract.Calendars.NAME,
                CALENDAR_NAME);
        values.put(
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                CALENDAR_NAME);
        values.put(
                CalendarContract.Calendars.CALENDAR_COLOR,
                CALENDAR_COLOR_LOCAL);
        values.put(
                CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
                CalendarContract.Calendars.CAL_ACCESS_OWNER);
        values.put(
                CalendarContract.Calendars.OWNER_ACCOUNT,
                OWNER_ACCOUNT_ID);
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
        reminderDateTime.add(Calendar.MINUTE, REMINDER_DURATION);
        long endMillis = reminderDateTime.getTimeInMillis();
        long exMillis = exDate.getTimeInMillis();

        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, reminderType);
        values.put(CalendarContract.Events.CALENDAR_ID, calendarID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        if(reminderType.equalsIgnoreCase(REMINDER_TYPE_CUSTOM)) {
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
