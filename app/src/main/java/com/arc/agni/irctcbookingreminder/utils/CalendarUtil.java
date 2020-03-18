package com.arc.agni.irctcbookingreminder.utils;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

import androidx.core.app.ActivityCompat;

import static com.arc.agni.irctcbookingreminder.constants.Constants.CALENDAR_ACCOUNT_NAME;
import static com.arc.agni.irctcbookingreminder.constants.Constants.CALENDAR_COLOR_LOCAL;
import static com.arc.agni.irctcbookingreminder.constants.Constants.CALENDAR_NAME;
import static com.arc.agni.irctcbookingreminder.constants.Constants.OWNER_ACCOUNT_ID;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_DURATION;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE_120_DAY;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE_CUSTOM;

public class CalendarUtil {

    /**
     * This method is used to create a new calendar for our application. It's a one time event per lifetime of application
     */
    public static void createCalendar(Context context) {
        ContentValues values = setCalendarContentValues();
        Uri.Builder builder =
                CalendarContract.Calendars.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(
                CalendarContract.Calendars.ACCOUNT_NAME,
                CALENDAR_ACCOUNT_NAME);
        builder.appendQueryParameter(
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.ACCOUNT_TYPE_LOCAL);
        builder.appendQueryParameter(
                CalendarContract.CALLER_IS_SYNCADAPTER,
                "true");
        context.getContentResolver().insert(builder.build(), values);
    }

    /**
     * This method sets attributes for a new calendar that will be created(One time event) for our application.
     */
    private static ContentValues setCalendarContentValues() {

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

    /**
     * This method is used to retrieve the calendar ID that was created earlier.
     */
    public static long getCalendarId(Context context) {
        long noSuchCalendarIndicator = -1;
        String[] projection = new String[]{CalendarContract.Calendars._ID};
        String selection =
                CalendarContract.Calendars.ACCOUNT_NAME +
                        " = ? AND " +
                        CalendarContract.Calendars.ACCOUNT_TYPE +
                        " = ? ";

        String[] selArgs =
                new String[]{
                        CALENDAR_ACCOUNT_NAME,
                        CalendarContract.ACCOUNT_TYPE_LOCAL};
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return noSuchCalendarIndicator;
        }
        Cursor cursor =
                context.getContentResolver().
                        query(
                                CalendarContract.Calendars.CONTENT_URI,
                                projection,
                                selection,
                                selArgs,
                                null);
        if (Objects.requireNonNull(cursor).moveToFirst()) {
            return cursor.getLong(0);
        }
        return -1;
    }

    public static long createReminder(String reminderTitle, Calendar reminderDateAndTime, Calendar exDateAndTime, String reminderType, Context context) {

        ContextWrapper contextWrapper = new ContextWrapper(context);
        long calId = getCalendarId(context);
        if (calId == -1) {
            createCalendar(context);
            calId = getCalendarId(context);
        }

        ContentValues values = setEventContentValues(calId, reminderTitle, reminderDateAndTime, exDateAndTime, reminderType);

        Uri uri = contextWrapper.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
        long eventID = Long.parseLong(Objects.requireNonNull(Objects.requireNonNull(uri).getLastPathSegment()));
        values = setReminderContentValues(eventID);
        contextWrapper.getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, values);
        return eventID;
    }

    /**
     * This method sets attributes for a new event that will be created(whenever a user create a reminder) by users
     */
    public static ContentValues setEventContentValues(long calendarID, String title, Calendar reminderDateTime, Calendar exDate, String reminderType) {
        Calendar localReminderDateTime = Calendar.getInstance();
        localReminderDateTime.setTimeInMillis(reminderDateTime.getTimeInMillis());
        ContentValues values = new ContentValues();
        TimeZone timeZone = TimeZone.getDefault();
        long startMillis = localReminderDateTime.getTimeInMillis();
        localReminderDateTime.add(Calendar.MINUTE, REMINDER_DURATION);
        long endMillis = localReminderDateTime.getTimeInMillis();
        long exMillis = exDate.getTimeInMillis();

        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, reminderType);
        values.put(CalendarContract.Events.CALENDAR_ID, calendarID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        // In case of CUSTOM REMINDER, TRAVEL DATE  is stored in EXDATE. It will be retrieved later in ViewRemindersActivity
        if (reminderType.equalsIgnoreCase(REMINDER_TYPE_CUSTOM)) {
            values.put(CalendarContract.Events.EXDATE, exMillis);
        }
        return values;
    }

    public static ContentValues setReminderContentValues(long eventID) {
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, eventID);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        values.put(CalendarContract.Reminders.MINUTES, REMINDER_DURATION);
        return values;
    }

}
