package com.arc.agni.irctcbookingreminder.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;

import com.arc.agni.irctcbookingreminder.R;
import com.arc.agni.irctcbookingreminder.service.NotificationMusicService;

import java.util.Calendar;

import androidx.core.app.NotificationCompat;

import static com.arc.agni.irctcbookingreminder.constants.Constants.ALERT_TYPE_ALARM;
import static com.arc.agni.irctcbookingreminder.constants.Constants.CHANNEL_DESCRIPTION;
import static com.arc.agni.irctcbookingreminder.constants.Constants.CHANNEL_ID;
import static com.arc.agni.irctcbookingreminder.constants.Constants.CHANNEL_NAME;
import static com.arc.agni.irctcbookingreminder.constants.Constants.EVENT_ID_ADDUP;
import static com.arc.agni.irctcbookingreminder.constants.Constants.INTENT_EXTRA_ALERT_TYPE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.INTENT_EXTRA_BOOKING_TIME;
import static com.arc.agni.irctcbookingreminder.constants.Constants.INTENT_EXTRA_NOTIFICATION;
import static com.arc.agni.irctcbookingreminder.constants.Constants.INTENT_EXTRA_NOTIFICATION_CATEGORY;
import static com.arc.agni.irctcbookingreminder.constants.Constants.INTENT_EXTRA_NOTIFICATION_ID;
import static com.arc.agni.irctcbookingreminder.constants.Constants.INTENT_EXTRA_NOTIFICATION_TITLE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.INTENT_EXTRA_TIME_LEFT;
import static com.arc.agni.irctcbookingreminder.constants.Constants.INTENT_EXTRA_TRAVEL_DATE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.MONTHS;
import static com.arc.agni.irctcbookingreminder.constants.Constants.NOTIFICATION_TEXT_ACTUAL;
import static com.arc.agni.irctcbookingreminder.constants.Constants.NOTIFICATION_TEXT_PRE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.NOTIF_TYPE_ACTUAL;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE_120_DAY;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE_CUSTOM;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE_TATKAL;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE_TATKAL_AC;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE_TATKAL_NON_AC;
import static com.arc.agni.irctcbookingreminder.constants.Constants._12_PM;
import static com.arc.agni.irctcbookingreminder.constants.Constants._30_MINUTES;
import static com.arc.agni.irctcbookingreminder.constants.Constants._5_PM;
import static com.arc.agni.irctcbookingreminder.constants.Constants._6_PM;

public class ReminderBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(INTENT_EXTRA_NOTIFICATION);
        int notificationID = intent.getIntExtra(INTENT_EXTRA_NOTIFICATION_ID, 0);
        int notificationCategory = intent.getIntExtra(INTENT_EXTRA_NOTIFICATION_CATEGORY, 0);
        String alertType = intent.getStringExtra(INTENT_EXTRA_ALERT_TYPE);

        // Start alarm music for on booking day(actual) notifications
        if (NOTIF_TYPE_ACTUAL == notificationCategory && ALERT_TYPE_ALARM.equalsIgnoreCase(alertType)) {
            Intent alarmScreenIntent = new Intent(context, NotificationMusicService.class);
            alarmScreenIntent.putExtra(INTENT_EXTRA_NOTIFICATION_TITLE, intent.getStringExtra(INTENT_EXTRA_NOTIFICATION_TITLE));
            alarmScreenIntent.putExtra(INTENT_EXTRA_TRAVEL_DATE, intent.getStringExtra(INTENT_EXTRA_TRAVEL_DATE));
            alarmScreenIntent.putExtra(INTENT_EXTRA_TIME_LEFT, intent.getLongExtra(INTENT_EXTRA_TIME_LEFT, 0));
            alarmScreenIntent.putExtra(INTENT_EXTRA_BOOKING_TIME, intent.getStringExtra(INTENT_EXTRA_BOOKING_TIME));
            context.startService(alarmScreenIntent);
        }

        // Fire notification
        notificationManager.notify(notificationID, notification);
    }

    private static void createChannel(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.setDescription(CHANNEL_DESCRIPTION);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    /**
     * This method will create a NOTIFICATION object with the provided NOTIFICATION_TEXT and other NOTIFICATION_CONFIGURATIONS.
     */
    private static Notification createNotification(String notificationTitle, String notificationText, Context context, PendingIntent viewReminderPendingIntent, int notificationType, long eventID) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setColor(context.getResources().getColor(R.color.colorAccent))
                .setContentTitle(notificationTitle)
                .setContentText(notificationType == NOTIF_TYPE_ACTUAL ? NOTIFICATION_TEXT_ACTUAL : NOTIFICATION_TEXT_PRE)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationText))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(viewReminderPendingIntent);
        return builder.build();
    }

    /**
     * This method will schedule a notification with the provided NOTIFICATION_TEXT at the specified REMINDER_DATE_TIME
     */
    public static void scheduleNotification(String notificationTitle, String reminderType, String notificationText, Calendar reminderDateAndTime, String travelDateAndTime, Context context, long eventID, PendingIntent viewReminderPendingIntent, int notificationType, String alertType) {
        // Create notification channel
        createChannel(context);

        // Create notification with passed text
        Notification notification = createNotification(notificationTitle, notificationText, context, viewReminderPendingIntent, notificationType, eventID);

        // Create Intent with extra to pass to BroadcastReceiver
        Intent notificationIntent = new Intent(context, ReminderBroadcast.class);
        notificationIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        notificationIntent.putExtra(INTENT_EXTRA_NOTIFICATION, notification);
        notificationIntent.putExtra(INTENT_EXTRA_NOTIFICATION_ID, (int) eventID);
        notificationIntent.putExtra(INTENT_EXTRA_NOTIFICATION_CATEGORY, notificationType);
        notificationIntent.putExtra(INTENT_EXTRA_ALERT_TYPE, alertType);
        if (ALERT_TYPE_ALARM.equalsIgnoreCase(alertType)) {
            notificationIntent.putExtra(INTENT_EXTRA_NOTIFICATION_TITLE, notificationTitle);
            notificationIntent.putExtra(INTENT_EXTRA_TRAVEL_DATE, travelDateAndTime);
            notificationIntent.putExtra(INTENT_EXTRA_TIME_LEFT, reminderDateAndTime.getTimeInMillis());
            notificationIntent.putExtra(INTENT_EXTRA_BOOKING_TIME, reminderDateAndTime.get(Calendar.HOUR_OF_DAY) + " a.m today");
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) eventID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Scheduling the notification
        Calendar localReminderDateAndTime = Calendar.getInstance();
        localReminderDateAndTime.setTimeInMillis(reminderDateAndTime.getTimeInMillis());
        long notificationTime = setNotificationTime(localReminderDateAndTime, reminderType, notificationType);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
        }
    }

    public static long setNotificationTime(Calendar reminderDateAndTime, final String reminderType, int notificationType) {

        if (NOTIF_TYPE_ACTUAL == notificationType) {
            reminderDateAndTime.set(Calendar.HOUR_OF_DAY, (reminderDateAndTime.get(Calendar.HOUR_OF_DAY) - 1));
            reminderDateAndTime.set(Calendar.MINUTE, _30_MINUTES);
        } else {
            switch (reminderType) {
                case REMINDER_TYPE_120_DAY: {
                    reminderDateAndTime.set(Calendar.HOUR_OF_DAY, _5_PM);
                    break;
                }
                case REMINDER_TYPE_TATKAL:
                case REMINDER_TYPE_TATKAL_AC:
                case REMINDER_TYPE_TATKAL_NON_AC: {
                    reminderDateAndTime.set(Calendar.HOUR_OF_DAY, _6_PM);
                    break;
                }
                case REMINDER_TYPE_CUSTOM: {
                    reminderDateAndTime.set(Calendar.HOUR_OF_DAY, _12_PM);
                    break;
                }
            }

        }
        return reminderDateAndTime.getTimeInMillis();
        //return Calendar.getInstance().getTimeInMillis() + 10000;
    }

    /**
     * This method will build the notification text with given data
     */
    public static String buildNotificationContent(String reminderType, String reminderTitle, int travelDay, int travelMonth, int travelYear, int bookingHour) {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder
                .append("You have set a reminder to book a train ticket for \"")
                .append(reminderTitle)
                .append("\". The travel date is ")
                .append(MONTHS[travelMonth]).append(" ").append(travelDay).append(", ").append(travelYear)
                .append(" (").append(travelDay).append("/").append(travelMonth + 1).append("/").append(travelYear).append(").");
        if (!REMINDER_TYPE_CUSTOM.equalsIgnoreCase(reminderType)) {
            contentBuilder.append(" Booking will open today at ").append(bookingHour).append(" a.m.");
        }
        contentBuilder.append("\nGood Luck!");

        return contentBuilder.toString();
    }

    /**
     * This method will build the notification text with given data
     */
    public static String buildNotificationContentForPreviousDay(String reminderType, String reminderTitle, int travelDay, int travelMonth, int travelYear, int bookingHour) {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder
                .append("You have set a reminder to book a train ticket for \"")
                .append(reminderTitle)
                .append("\". The travel date is ")
                .append(MONTHS[travelMonth]).append(" ").append(travelDay).append(", ").append(travelYear)
                .append(" (").append(travelDay).append("/").append(travelMonth + 1).append("/").append(travelYear).append(").");
        if (REMINDER_TYPE_CUSTOM.equalsIgnoreCase(reminderType)) {
            contentBuilder.append(" Tomorrow is your custom booking date");
        } else {
            contentBuilder.append(" Booking opens tomorrow at ").append(bookingHour).append(" a.m.");
        }
        contentBuilder.append("\nGood Luck!");

        return contentBuilder.toString();
    }

    /**
     * This method is used to delete the set notification if the corresponding event is deleted
     */
    public static void cancelNotification(int eventID, Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(context, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, eventID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

        // Delete the additional notification
        pendingIntent = PendingIntent.getBroadcast(context, (eventID + (int) EVENT_ID_ADDUP), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }
}
