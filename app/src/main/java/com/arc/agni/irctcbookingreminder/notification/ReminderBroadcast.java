package com.arc.agni.irctcbookingreminder.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.arc.agni.irctcbookingreminder.R;
import com.arc.agni.irctcbookingreminder.activities.TatkalReminderActivity;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.arc.agni.irctcbookingreminder.constants.Constants.CHANNEL_ID;
import static com.arc.agni.irctcbookingreminder.constants.Constants.NOTIFICATION_ID;

public class ReminderBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_information_outline_white_36dp)
                .setContentTitle("Booking Reminder")
                .setContentText("This is a reminder")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void createNotification(Context context, AlarmManager alarmManager, long notificationTime) {
        Intent intent = new Intent(context, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
    }
}
