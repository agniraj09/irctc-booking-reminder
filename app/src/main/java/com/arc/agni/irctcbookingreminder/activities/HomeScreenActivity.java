package com.arc.agni.irctcbookingreminder.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.arc.agni.irctcbookingreminder.R;
import com.arc.agni.irctcbookingreminder.utils.DialogUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static com.arc.agni.irctcbookingreminder.constants.Constants.*;

public class HomeScreenActivity extends AppCompatActivity {

    private AdView mAdView;
    private static final int MY_PERMISSIONS_REQUEST_READ_CALENDAR = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 2;
    DialogUtil dialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        dialogUtil = new DialogUtil(this);

        MobileAds.initialize(this, ADMOB_APP_ID);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("0EC56B91253E874AAF286CEDC3945F6A").build();
        mAdView.loadAd(adRequest);

        // Create Notification Channel
        createNotificationChannel();
    }

    public void goToBookingCalculatorPage(View view) {
        if (arePermissionsGranted()) {
            Intent intent = new Intent(HomeScreenActivity.this, BookingDayCalculatorActivity.class);
            startActivity(intent);
        }
    }

    public void goToAdvanceBookingReminderPage(View view) {
        if (arePermissionsGranted()) {
            Intent intent = new Intent(HomeScreenActivity.this, AdvanceBookingReminderActivity.class);
            startActivity(intent);
        }
    }

    public void goToTatkalReminderPage(View view) {
        if (arePermissionsGranted()) {
            Intent intent = new Intent(HomeScreenActivity.this, TatkalReminderActivity.class);
            startActivity(intent);
        }
    }

    public void goToCustomBookingPage(View view) {
        if (arePermissionsGranted()) {
            Intent intent = new Intent(HomeScreenActivity.this, CustomReminderActivity.class);
            startActivity(intent);
        }
    }

    public void goToViewRemindersPage(View view) {
        if (arePermissionsGranted()) {
            Intent intent = new Intent(HomeScreenActivity.this, ViewRemindersActivity.class);
            startActivity(intent);
        }
    }

    public void showAdvanceBookingReminderInfo(View view) {
        dialogUtil.showDescriptionDialog(this, IND_120_DAY_REMINDER);
    }

    public void showTatkalReminderInfo(View view) {
        dialogUtil.showDescriptionDialog(this, IND_TATKAL_REMINDER);
    }

    public void showCustomReminderInfo(View view) {
        dialogUtil.showDescriptionDialog(this, IND_CUSTOM_REMINDER);
    }

    public void showViewRemindersInfo(View view) {
        dialogUtil.showDescriptionDialog(this, IND_VIEW_REMINDERS);
    }

    public boolean arePermissionsGranted() {
        return (requestReadPermission() && requestWritePermission());
    }

    public boolean requestReadPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALENDAR},
                    MY_PERMISSIONS_REQUEST_READ_CALENDAR);
        }
        return false;
    }

    public boolean requestWritePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_CALENDAR},
                    MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CALENDAR: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                } else {
                    Toast.makeText(this, CALENDAR_PERMISSION_WARNING, Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_WRITE_CALENDAR: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                } else {
                    Toast.makeText(this, CALENDAR_PERMISSION_WARNING, Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void createNotificationChannel() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = null;
            channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESCRIPTION);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage(EXIT_WARNING)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        HomeScreenActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}
