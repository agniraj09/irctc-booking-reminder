package com.arc.agni.irctcbookingreminder.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.arc.agni.irctcbookingreminder.activities.ViewSetReminderActivity;
import com.arc.agni.irctcbookingreminder.notification.ReminderBroadcast;

import java.util.Calendar;

import static com.arc.agni.irctcbookingreminder.constants.Constants.BOOKING_TIME;
import static com.arc.agni.irctcbookingreminder.constants.Constants.DAYS;
import static com.arc.agni.irctcbookingreminder.constants.Constants.EVENT_ID_ADDUP;
import static com.arc.agni.irctcbookingreminder.constants.Constants.EVENT_TITILE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.INTENT_EXTRA_NOTIFICATION_CATEGORY;
import static com.arc.agni.irctcbookingreminder.constants.Constants.MINUS_1_DAY;
import static com.arc.agni.irctcbookingreminder.constants.Constants.MONTHS;
import static com.arc.agni.irctcbookingreminder.constants.Constants.NOTIF_TYPE_ACTUAL;
import static com.arc.agni.irctcbookingreminder.constants.Constants.NOTIF_TYPE_DUMMY;
import static com.arc.agni.irctcbookingreminder.constants.Constants.NOTIF_TYPE_PRE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_DATE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TIME;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE_TATKAL;
import static com.arc.agni.irctcbookingreminder.constants.Constants.SCOPE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.SCOPE_NO_TOAST;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TATKAL_BOOKING_AC_REMINDER_HOUR;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TATKAL_BOOKING_NON_AC_REMINDER_HOUR;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TIME_LEFT;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TRAVEL_DATE;
import static com.arc.agni.irctcbookingreminder.constants.Constants._6_PM;

public class CommonUtil {

    public static void buildAndScheduleNotification(String reminderType, String reminderTitle, int travelDay, int travelMonth, int travelYear, int bookingHour, Context context, Calendar reminderDateAndTime, long eventId) {
        Calendar localReminderDateAndTime = Calendar.getInstance();
        localReminderDateAndTime.setTimeInMillis(reminderDateAndTime.getTimeInMillis());
        String notificationText = ReminderBroadcast.buildNotificationContent(reminderType, reminderTitle, travelDay, travelMonth, travelYear, bookingHour);
        PendingIntent notificationActivityIntent = CommonUtil.createPendingIntentForNotification(context, reminderTitle, reminderType, travelDay, travelMonth, travelYear, localReminderDateAndTime, eventId, NOTIF_TYPE_ACTUAL);
        ReminderBroadcast.scheduleNotification(reminderTitle, notificationText, localReminderDateAndTime, context, eventId, notificationActivityIntent, NOTIF_TYPE_ACTUAL);

        // Set one additional reminder notification if the reminder date is long
        Calendar thresholdTime = Calendar.getInstance();
        thresholdTime.setTimeInMillis(localReminderDateAndTime.getTimeInMillis());
        thresholdTime.add(Calendar.DAY_OF_YEAR, MINUS_1_DAY);
        thresholdTime.set(Calendar.HOUR_OF_DAY, _6_PM);
        if (thresholdTime.getTime().after(Calendar.getInstance().getTime())) {
            eventId = eventId + EVENT_ID_ADDUP;
            notificationText = ReminderBroadcast.buildNotificationContentForPreviousDay(reminderType, reminderTitle, travelDay, travelMonth, travelYear, bookingHour);
            notificationActivityIntent = CommonUtil.createPendingIntentForNotification(context, reminderTitle, reminderType, travelDay, travelMonth, travelYear, localReminderDateAndTime, eventId, NOTIF_TYPE_PRE);
            localReminderDateAndTime.add(Calendar.DAY_OF_YEAR, MINUS_1_DAY);
            ReminderBroadcast.scheduleNotification(reminderTitle, notificationText, localReminderDateAndTime, context, eventId, notificationActivityIntent, NOTIF_TYPE_PRE);
        }

    }

    public static Intent createIntentPostReminderCreation(Context context, String reminderTitle, String reminderType, int travelDay, int travelMonth, int travelYear, Calendar reminderDateAndTime) {
        Intent intent = createIntent(context, reminderTitle, reminderType, reminderDateAndTime, NOTIF_TYPE_DUMMY);
        intent.putExtra(TRAVEL_DATE, formatDateToFullText(travelDay, travelMonth, travelYear));
        return intent;
    }

    public static Intent createIntentPostReminderCreation(Context context, String reminderTitle, String reminderType, Calendar travelDateAndTime, Calendar reminderDateAndTime) {
        Intent intent = createIntent(context, reminderTitle, reminderType, reminderDateAndTime, NOTIF_TYPE_DUMMY);
        intent.putExtra(TRAVEL_DATE, formatCalendarDateToFullText(travelDateAndTime));
        return intent;
    }

    private static PendingIntent createPendingIntentForNotification(Context context, String reminderTitle, String reminderType, int travelDay, int travelMonth, int travelYear, Calendar reminderDateAndTime, long eventID, int notificationType) {
        Intent notificationActivityIntent = createIntent(context, reminderTitle, reminderType, reminderDateAndTime, notificationType);
        notificationActivityIntent.putExtra(TRAVEL_DATE, formatDateToFullText(travelDay, travelMonth, travelYear));
        notificationActivityIntent.putExtra(SCOPE, SCOPE_NO_TOAST);
        notificationActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        return PendingIntent.getActivity(context, (int) eventID, notificationActivityIntent, PendingIntent.FLAG_ONE_SHOT);
    }

    private static Intent createIntent(Context context, String reminderTitle, String reminderType, Calendar reminderDateAndTime, int notificationType) {
        Intent intent = new Intent(context, ViewSetReminderActivity.class);
        intent.putExtra(EVENT_TITILE, reminderTitle);
        intent.putExtra(REMINDER_TYPE, reminderType);
        intent.putExtra(REMINDER_DATE, formatCalendarDateToFullText(reminderDateAndTime));

        // If reminder type is TATKAL, show both AC & NON_AC details or else show the actual details
        String reminderTime;
        String bookingTime;
        if (REMINDER_TYPE_TATKAL.equalsIgnoreCase(reminderType)) {
            reminderTime = "AC - " + (TATKAL_BOOKING_AC_REMINDER_HOUR - 1) + ".30 a.m.\nNon AC - " + (TATKAL_BOOKING_NON_AC_REMINDER_HOUR - 1) + ".30 a.m.";
            bookingTime = "AC - " + TATKAL_BOOKING_AC_REMINDER_HOUR + " a.m.\nNon AC - " + TATKAL_BOOKING_NON_AC_REMINDER_HOUR + " a.m";

        } else {
            reminderTime = (reminderDateAndTime.get(Calendar.HOUR_OF_DAY) - 1) + ".30 a.m.";
            bookingTime = reminderDateAndTime.get(Calendar.HOUR_OF_DAY) + " a.m";
        }
        intent.putExtra(TIME_LEFT, reminderDateAndTime.getTimeInMillis());
        intent.putExtra(BOOKING_TIME, bookingTime);
        intent.putExtra(REMINDER_TIME, reminderTime);
        intent.putExtra(INTENT_EXTRA_NOTIFICATION_CATEGORY, notificationType);
        return intent;
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
