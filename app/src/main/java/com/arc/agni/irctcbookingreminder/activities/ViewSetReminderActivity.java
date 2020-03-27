package com.arc.agni.irctcbookingreminder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.arc.agni.irctcbookingreminder.R;
import com.arc.agni.irctcbookingreminder.service.NotificationMusicService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;

import static com.arc.agni.irctcbookingreminder.constants.Constants.BOOKING_TIME;
import static com.arc.agni.irctcbookingreminder.constants.Constants.EVENT_TITILE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.INTENT_EXTRA_NOTIFICATION_CATEGORY;
import static com.arc.agni.irctcbookingreminder.constants.Constants.NOTIF_TYPE_ACTUAL;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_DATE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TIME;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.SCOPE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.SCOPE_NO_TOAST;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TIME_LEFT;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TITLE_VIEW_SET_REMINDER;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TRAVEL_DATE;

public class ViewSetReminderActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_set_reminder);
        setTitle(TITLE_VIEW_SET_REMINDER);

        // Stop the alarm sound if the notification is actual notification
        int notificationCategory = getIntent().getIntExtra(INTENT_EXTRA_NOTIFICATION_CATEGORY, 0);
        if (NOTIF_TYPE_ACTUAL == notificationCategory) {
            stopService(new Intent(this, NotificationMusicService.class));
        }

        // Do not show the Toast if SCOPE is set to NO_TOAST
        if (!(null != getIntent().getStringExtra(SCOPE) && SCOPE_NO_TOAST.equalsIgnoreCase(getIntent().getStringExtra(SCOPE)))) {
            Toast.makeText(this, "Reminder is set successfully", Toast.LENGTH_SHORT).show();
        }

        // Days left calculation
        Calendar reminderDateAndTime = Calendar.getInstance();
        reminderDateAndTime.setTimeInMillis(getIntent().getLongExtra(TIME_LEFT, 0));
        String timeLeft;
        long daysLeft = TimeUnit.MILLISECONDS.toDays(reminderDateAndTime.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
        long hoursleft = TimeUnit.MILLISECONDS.toHours(reminderDateAndTime.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
        long minutesLeft = TimeUnit.MILLISECONDS.toMinutes(reminderDateAndTime.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
        if (daysLeft == 0) {
            if (hoursleft == 0) {
                timeLeft = "(Tip : " + minutesLeft + " minutes to go for booking)";
            } else {
                timeLeft = "(Tip : " + hoursleft + " hour(s) to go for booking)";
            }
        } else {
            timeLeft = "(Tip : " + daysLeft + " day(s) to go for booking)";
        }
        timeLeft = timeLeft.contains("-") ? "(Booking has already started)" : timeLeft;

        ((TextView) findViewById(R.id.vsr_event_title)).setText(getIntent().getStringExtra(EVENT_TITILE));
        ((TextView) findViewById(R.id.vsr_reminder_type)).setText(getIntent().getStringExtra(REMINDER_TYPE));
        ((TextView) findViewById(R.id.vsr_travel_date)).setText(getIntent().getStringExtra(TRAVEL_DATE));
        ((TextView) findViewById(R.id.vsr_reminder_date)).setText(getIntent().getStringExtra(REMINDER_DATE));
        ((TextView) findViewById(R.id.vsr_reminder_time)).setText(getIntent().getStringExtra(REMINDER_TIME));
        ((TextView) findViewById(R.id.vsr_days_left)).setText(timeLeft);
        ((TextView) findViewById(R.id.vsr_booking_time)).setText(getIntent().getStringExtra(BOOKING_TIME));

        // Request for ad
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void gotoHome(View view) {
        Intent intent = new Intent(ViewSetReminderActivity.this, HomeScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void gotoViewReminders(View view) {
        Intent intent = new Intent(ViewSetReminderActivity.this, ViewRemindersActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
