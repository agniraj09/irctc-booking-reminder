package com.arc.agni.irctcbookingreminder.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.arc.agni.irctcbookingreminder.R;
import com.arc.agni.irctcbookingreminder.utils.DialogUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class HomeScreenActivity extends AppCompatActivity {

    public static String ADMOB_APP_ID = "ca-app-pub-4587610802196055~4797049191";
    private AdView mAdView;
    private static final int MY_PERMISSIONS_REQUEST_READ_CALENDAR = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 2;
    DialogUtil dialogUtil ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        dialogUtil = new DialogUtil(this);

        MobileAds.initialize(this, ADMOB_APP_ID);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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
        dialogUtil.showDescriptionDialog(this, 1);
    }

    public void showTatkalReminderInfo(View view) {
        dialogUtil.showDescriptionDialog(this, 2);
    }

    public void showCustomReminderInfo(View view) {
        dialogUtil.showDescriptionDialog(this, 3);
    }

    public void showViewRemindersInfo(View view) {
        dialogUtil.showDescriptionDialog(this, 4);
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
                    Toast.makeText(this, "Calendar Permission needed to proceed further", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_WRITE_CALENDAR: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                } else {
                    Toast.makeText(this, "Calendar Permission needed to proceed further", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        HomeScreenActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}
