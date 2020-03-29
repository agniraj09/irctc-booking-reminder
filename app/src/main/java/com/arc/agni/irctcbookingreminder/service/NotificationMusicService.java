package com.arc.agni.irctcbookingreminder.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import com.arc.agni.irctcbookingreminder.activities.AlarmScreenActivity;

import java.io.IOException;

import static com.arc.agni.irctcbookingreminder.constants.Constants.INTENT_EXTRA_BOOKING_TIME;
import static com.arc.agni.irctcbookingreminder.constants.Constants.INTENT_EXTRA_NOTIFICATION_TITLE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.INTENT_EXTRA_TIME_LEFT;
import static com.arc.agni.irctcbookingreminder.constants.Constants.INTENT_EXTRA_TRAVEL_DATE;

public class NotificationMusicService extends Service {

    MediaPlayer player = new MediaPlayer();

    public NotificationMusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Get the URI of the ALARM tone. If the tone is not set by user, get RINGTONE tone.
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (null == uri) {
            uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        }

        // 'setAudioAttributes' method is supported only =above LOLLIPOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                player.setAudioAttributes(new AudioAttributes.Builder()
                        .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                        .setLegacyStreamType(AudioManager.STREAM_ALARM)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build());
                player.setDataSource(this, uri);
                player.setLooping(true);
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            player = MediaPlayer.create(this, uri);
            player.setAudioStreamType(AudioManager.STREAM_ALARM);
            player.setLooping(true);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();

        Intent alarmScreenIntent = new Intent(this, AlarmScreenActivity.class);
        alarmScreenIntent.putExtra(INTENT_EXTRA_NOTIFICATION_TITLE, intent.getStringExtra(INTENT_EXTRA_NOTIFICATION_TITLE));
        alarmScreenIntent.putExtra(INTENT_EXTRA_TRAVEL_DATE, intent.getStringExtra(INTENT_EXTRA_TRAVEL_DATE));
        alarmScreenIntent.putExtra(INTENT_EXTRA_TIME_LEFT, intent.getLongExtra(INTENT_EXTRA_TIME_LEFT, 0));
        alarmScreenIntent.putExtra(INTENT_EXTRA_BOOKING_TIME, intent.getStringExtra(INTENT_EXTRA_BOOKING_TIME));
        alarmScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(alarmScreenIntent);

        return START_NOT_STICKY; // Service will be killed if app is killed
    }

    @Override
    public void onDestroy() {
        player.stop();
        player.release();
    }
}
