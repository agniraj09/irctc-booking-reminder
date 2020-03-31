package com.arc.agni.irctcbookingreminder.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.arc.agni.irctcbookingreminder.R;
import com.arc.agni.irctcbookingreminder.service.NotificationMusicService;
import com.ebanx.swipebtn.SwipeButton;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;

import static com.arc.agni.irctcbookingreminder.constants.Constants.INTENT_EXTRA_BOOKING_TIME;
import static com.arc.agni.irctcbookingreminder.constants.Constants.INTENT_EXTRA_NOTIFICATION_TITLE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.INTENT_EXTRA_TIME_LEFT;
import static com.arc.agni.irctcbookingreminder.constants.Constants.INTENT_EXTRA_TRAVEL_DATE;

public class AlarmScreenActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_screen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            this.setTurnScreenOn(true);
            this.setShowWhenLocked(true);
        } else {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        // Days left calculation
        Calendar reminderDateAndTime = Calendar.getInstance();
        reminderDateAndTime.setTimeInMillis(getIntent().getLongExtra(INTENT_EXTRA_TIME_LEFT, 0));
        String timeLeft;
        long daysLeft = TimeUnit.MILLISECONDS.toDays(reminderDateAndTime.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
        long hoursleft = TimeUnit.MILLISECONDS.toHours(reminderDateAndTime.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
        long minutesLeft = TimeUnit.MILLISECONDS.toMinutes(reminderDateAndTime.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
        if (daysLeft == 0) {
            if (hoursleft == 0) {
                timeLeft = "(" + minutesLeft + " minutes to go for booking)";
            } else {
                timeLeft = "(" + hoursleft + " hour(s) to go for booking)";
            }
        } else {
            timeLeft = "(" + daysLeft + " day(s) to go for booking)";
        }
        timeLeft = timeLeft.contains("-") ? "(Booking has already started)" : timeLeft;

        ((TextView) findViewById(R.id.as_event_title)).setText(getIntent().getStringExtra(INTENT_EXTRA_NOTIFICATION_TITLE));
        ((TextView) findViewById(R.id.as_travel_date)).setText(getIntent().getStringExtra(INTENT_EXTRA_TRAVEL_DATE));
        ((TextView) findViewById(R.id.as_days_left)).setText(timeLeft);
        ((TextView) findViewById(R.id.as_booking_time)).setText(getIntent().getStringExtra(INTENT_EXTRA_BOOKING_TIME));

        SwipeButton enableButton = findViewById(R.id.stop_alarm);
        enableButton.setOnStateChangeListener(active -> stopAlarm(null));
    }

    public void stopAlarm(View view) {
        stopService(new Intent(this, NotificationMusicService.class));
        finish();
    }

    /**
     * This method is triggered when back button is pressed
     */
    @Override
    public void onBackPressed() {
        stopAlarm(null);
    }

}
