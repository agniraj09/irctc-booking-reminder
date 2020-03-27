package com.arc.agni.irctcbookingreminder.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.arc.agni.irctcbookingreminder.service.NotificationMusicService;

/**
 * This receiver is used to stop the alarm sound
 */
public class ActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.stopService(new Intent(context, NotificationMusicService.class));
    }
}
