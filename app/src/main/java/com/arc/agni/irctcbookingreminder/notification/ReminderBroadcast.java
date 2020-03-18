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

import com.arc.agni.irctcbookingreminder.R;

import java.util.Calendar;

import androidx.core.app.NotificationCompat;

import static com.arc.agni.irctcbookingreminder.constants.Constants.CHANNEL_DESCRIPTION;
import static com.arc.agni.irctcbookingreminder.constants.Constants.CHANNEL_ID;
import static com.arc.agni.irctcbookingreminder.constants.Constants.CHANNEL_NAME;
import static com.arc.agni.irctcbookingreminder.constants.Constants.EVENT_ID_ADDUP;
import static com.arc.agni.irctcbookingreminder.constants.Constants.INTENT_EXTRA_NOTIFICATION;
import static com.arc.agni.irctcbookingreminder.constants.Constants.INTENT_EXTRA_NOTIFICATION_ID;
import static com.arc.agni.irctcbookingreminder.constants.Constants.MONTHS;
import static com.arc.agni.irctcbookingreminder.constants.Constants.NOTIFICATION_TEXT_ACTUAL;
import static com.arc.agni.irctcbookingreminder.constants.Constants.NOTIFICATION_TEXT_PRE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.NOTIF_TYPE_ACTUAL;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE_CUSTOM;

public class ReminderBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(INTENT_EXTRA_NOTIFICATION);
        int notificationID = intent.getIntExtra(INTENT_EXTRA_NOTIFICATION_ID, 0);
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
    private static Notification createNotification(String notificationTitle, String notificationText, Context context, PendingIntent viewReminderPendingIntent, int notificationType) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle(notificationTitle)
                .setContentText(notificationType == NOTIF_TYPE_ACTUAL ? NOTIFICATION_TEXT_ACTUAL : NOTIFICATION_TEXT_PRE)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationText))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(viewReminderPendingIntent)
                .addAction(R.drawable.ic_notification_icon, "View", viewReminderPendingIntent);
        return builder.build();
    }

    /**
     * This method will schedule a notification with the provided NOTIFICATION_TEXT at the specified REMINDER_DATE_TIME
     */
    public static void scheduleNotification(String notificationTitle, String notificationText, Calendar reminderDateAndTime, Context context, long eventID, PendingIntent viewReminderPendingIntent, int notificationType) {
        // Create notification channel
        createChannel(context);

        // Create notification with passed text
        Notification notification = createNotification(notificationTitle, notificationText, context, viewReminderPendingIntent, notificationType);

        // Create Intent with extra to pass to BroadcastReceiver
        Intent notificationIntent = new Intent(context, ReminderBroadcast.class);
        notificationIntent.putExtra(INTENT_EXTRA_NOTIFICATION, notification);
        notificationIntent.putExtra(INTENT_EXTRA_NOTIFICATION_ID, (int) eventID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) eventID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Calculate time difference in millis
        long notificationTime = reminderDateAndTime.getTimeInMillis() - (1000 * 60 * 30);
        //long notificationTime = Calendar.getInstance().getTimeInMillis() + 10000;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
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
