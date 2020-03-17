package com.arc.agni.irctcbookingreminder.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.arc.agni.irctcbookingreminder.activities.ViewSetReminderActivity;

import java.util.Calendar;

import static com.arc.agni.irctcbookingreminder.constants.Constants.DAYS;
import static com.arc.agni.irctcbookingreminder.constants.Constants.EVENT_TITILE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.MONTHS;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_DATE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TIME;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE_TATKAL;
import static com.arc.agni.irctcbookingreminder.constants.Constants.SCOPE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.SCOPE_NO_TOAST;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TRAVEL_DATE;

public class CommonUtil {

    //static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public static Intent createIntentPostReminderCreation(Context context, String reminderTitle, String reminderType, int travelDay, int travelMonth, int travelYear, Calendar reminderDateAndTime) {
        Intent intent = new Intent(context, ViewSetReminderActivity.class);
        intent.putExtra(EVENT_TITILE, reminderTitle);
        intent.putExtra(REMINDER_TYPE, reminderType);
        intent.putExtra(TRAVEL_DATE, formatDateToFullText(travelDay, travelMonth, travelYear));
        intent.putExtra(REMINDER_DATE, formatCalendarDateToFullText(reminderDateAndTime));
        String reminderTime = REMINDER_TYPE_TATKAL.equalsIgnoreCase(reminderType) ? "7.30 a.m. & 10.30 a.m." : ((reminderDateAndTime.get(Calendar.HOUR_OF_DAY) - 1) + ".30 a.m.");
        intent.putExtra(REMINDER_TIME, reminderTime);
        return intent;
    }

    public static Intent createIntentPostReminderCreation(Context context, String reminderTitle, String reminderType, Calendar travelDateAndTime, Calendar reminderDateAndTime) {
        Intent intent = new Intent(context, ViewSetReminderActivity.class);
        intent.putExtra(EVENT_TITILE, reminderTitle);
        intent.putExtra(REMINDER_TYPE, reminderType);
        intent.putExtra(TRAVEL_DATE, formatCalendarDateToFullText(travelDateAndTime));
        intent.putExtra(REMINDER_DATE, formatCalendarDateToFullText(reminderDateAndTime));
        String reminderTime = REMINDER_TYPE_TATKAL.equalsIgnoreCase(reminderType) ? "7.30 a.m. & 10.30 a.m." : ((reminderDateAndTime.get(Calendar.HOUR_OF_DAY) - 1) + ".30 a.m.");
        intent.putExtra(REMINDER_TIME, reminderTime);
        return intent;
    }

    public static PendingIntent createPendingIntentForNotification(Context context, String reminderTitle, String reminderType, int travelDay, int travelMonth, int travelYear, Calendar reminderDateAndTime, long eventID) {
        Intent notificationActivityIntent = new Intent(context, ViewSetReminderActivity.class);
        notificationActivityIntent.putExtra(EVENT_TITILE, reminderTitle);
        notificationActivityIntent.putExtra(REMINDER_TYPE, reminderType);
        notificationActivityIntent.putExtra(TRAVEL_DATE, formatDateToFullText(travelDay, travelMonth, travelYear));
        notificationActivityIntent.putExtra(REMINDER_DATE, formatCalendarDateToFullText(reminderDateAndTime));
        notificationActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationActivityIntent.putExtra(REMINDER_TIME, (reminderDateAndTime.get(Calendar.HOUR_OF_DAY) - 1) + ".30 a.m.");
        notificationActivityIntent.putExtra(SCOPE, SCOPE_NO_TOAST);
        PendingIntent viewReminderPendingIntent = PendingIntent.getActivity(context, (int) eventID, notificationActivityIntent, PendingIntent.FLAG_ONE_SHOT);
        return viewReminderPendingIntent;
    }

    public static String formatDateToFullText(int day, int month, int year) {
        Calendar date = Calendar.getInstance();
        date.set(year, month, day);
        return formatCalendarDateToFullText(date);

    }

    public static String formatCalendarDateToFullText(Calendar date) {
        return MONTHS[date.get(Calendar.MONTH)] + " " + date.get(Calendar.DAY_OF_MONTH) + ", " + date.get(Calendar.YEAR) + " (" + DAYS[date.get(Calendar.DAY_OF_WEEK) - 1] + ") ";

    }
}
