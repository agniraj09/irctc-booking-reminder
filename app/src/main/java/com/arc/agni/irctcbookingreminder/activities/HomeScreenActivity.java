package com.arc.agni.irctcbookingreminder.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.arc.agni.irctcbookingreminder.R;
import com.arc.agni.irctcbookingreminder.utils.CalendarUtil;
import com.arc.agni.irctcbookingreminder.utils.DialogUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static com.arc.agni.irctcbookingreminder.constants.Constants.*;

public class HomeScreenActivity extends AppCompatActivity {

    DialogUtil dialogUtil = new DialogUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        /*// Create Notification Channel. One time activity per application launch.
        createNotificationChannel();*/

        // Initialize MobileAds & Request for ads
        MobileAds.initialize(this, ADMOB_APP_ID);
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(TEST_DEVICE_ID).build();
        mAdView.loadAd(adRequest);
    }

    /**
     * Start "Booking Day Calculator" Activity
     */
    public void goToBookingCalculatorPage(View view) {
        if (arePermissionsGranted()) {
            Intent intent = new Intent(HomeScreenActivity.this, BookingDayCalculatorActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Start "120 Day Reminder" Activity
     */
    public void goToAdvanceBookingReminderPage(View view) {
        goToActivity(HomeScreenActivity.this, AdvanceBookingReminderActivity.class);
    }

    /**
     * Start "Tatkal Reminder" Activity
     */
    public void goToTatkalReminderPage(View view) {
        goToActivity(HomeScreenActivity.this, TatkalReminderActivity.class);
    }

    /**
     * Start "Custom Reminder" Activity
     */
    public void goToCustomBookingPage(View view) {
        goToActivity(HomeScreenActivity.this, CustomReminderActivity.class);
    }

    /**
     * Start "View Reminders" Activity
     */
    public void goToViewRemindersPage(View view) {
        goToActivity(HomeScreenActivity.this, ViewRemindersActivity.class);
    }

    /**
     * This method is used to start the specified activity from the given context
     */
    public <T> void goToActivity(Context packageContext, Class<T> className) {
        if (arePermissionsGranted()) {
            Intent intent = new Intent(packageContext, className);
            startActivity(intent);
        }
    }

    /**
     * Shows "120 Day Reminder" Info in pop-up
     */
    public void showAdvanceBookingReminderInfo(View view) {
        dialogUtil.showDescriptionDialog(this, IND_120_DAY_REMINDER);
    }

    /**
     * Shows "Tatkal Reminder" Info in pop-up
     */
    public void showTatkalReminderInfo(View view) {
        dialogUtil.showDescriptionDialog(this, IND_TATKAL_REMINDER);
    }

    /**
     * Shows "Custom Reminder" Info in pop-up
     */
    public void showCustomReminderInfo(View view) {
        dialogUtil.showDescriptionDialog(this, IND_CUSTOM_REMINDER);
    }

    /**
     * Shows "View Reminders" Info in pop-up
     */
    public void showViewRemindersInfo(View view) {
        dialogUtil.showDescriptionDialog(this, IND_VIEW_REMINDERS);
    }

    /**
     * This method checks if READ & WRITE permissions for CALENDAR are given by user
     */
    public boolean arePermissionsGranted() {
        return (requestReadPermission() && requestWritePermission());
    }

    /**
     * This method checks if CALENDAR_READ permission is given by user.If not, it will raise the request
     */
    public boolean requestReadPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALENDAR},
                    PERMISSIONS_REQUEST_READ_CALENDAR);
        }
        return false;
    }

    /**
     * This method checks if CALENDAR_WRITE permission is given by user.If not, it will raise the request
     */
    public boolean requestWritePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_CALENDAR},
                    PERMISSIONS_REQUEST_WRITE_CALENDAR);
        }
        return false;
    }

    /*    */

    /**
     * This method creates an exclusive notification channel to shoot notifications
     *//*
    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESCRIPTION);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CALENDAR:
            case PERMISSIONS_REQUEST_WRITE_CALENDAR: {
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, CALENDAR_PERMISSION_WARNING, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * This method is triggered when back button is pressed when in Home Page
     */
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage(EXIT_WARNING)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> HomeScreenActivity.super.onBackPressed()).create().show();
    }
}
