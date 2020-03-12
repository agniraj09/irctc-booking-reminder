package com.arc.agni.irctcbookingreminder.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.arc.agni.irctcbookingreminder.R;
import com.arc.agni.irctcbookingreminder.utils.DialogUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static com.arc.agni.irctcbookingreminder.constants.Constants.ADMOB_APP_ID;
import static com.arc.agni.irctcbookingreminder.constants.Constants.CALENDAR_PERMISSION_WARNING;
import static com.arc.agni.irctcbookingreminder.constants.Constants.CONNECTION_KEY;
import static com.arc.agni.irctcbookingreminder.constants.Constants.CONNECTION_TIMEOUT;
import static com.arc.agni.irctcbookingreminder.constants.Constants.CONNECTION_VALUE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.CONTENT_LENGTH;
import static com.arc.agni.irctcbookingreminder.constants.Constants.EXIT_WARNING;
import static com.arc.agni.irctcbookingreminder.constants.Constants.HOLIDAY_LIST_TITLE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.IND_120_DAY_REMINDER;
import static com.arc.agni.irctcbookingreminder.constants.Constants.IND_CUSTOM_REMINDER;
import static com.arc.agni.irctcbookingreminder.constants.Constants.IND_TATKAL_REMINDER;
import static com.arc.agni.irctcbookingreminder.constants.Constants.IND_VIEW_REMINDERS;
import static com.arc.agni.irctcbookingreminder.constants.Constants.INTERNET_PERMISSION_WARNING;
import static com.arc.agni.irctcbookingreminder.constants.Constants.NEED_INTERNET_CONNECTION;
import static com.arc.agni.irctcbookingreminder.constants.Constants.NETWORK_TEST_URL;
import static com.arc.agni.irctcbookingreminder.constants.Constants.PERMISSIONS_REQUEST_INTERNET;
import static com.arc.agni.irctcbookingreminder.constants.Constants.PERMISSIONS_REQUEST_NETWORK;
import static com.arc.agni.irctcbookingreminder.constants.Constants.PERMISSIONS_REQUEST_READ_CALENDAR;
import static com.arc.agni.irctcbookingreminder.constants.Constants.PERMISSIONS_REQUEST_WRITE_CALENDAR;
import static com.arc.agni.irctcbookingreminder.constants.Constants.SOMETHING_WENT_WRONG;
import static com.arc.agni.irctcbookingreminder.constants.Constants.STATUS_CODE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TEST_DEVICE_ID;
import static com.arc.agni.irctcbookingreminder.constants.Constants.USER_AGENT_KEY;
import static com.arc.agni.irctcbookingreminder.constants.Constants.USER_AGENT_VALUE;

public class HomeScreenActivity extends AppCompatActivity {

    DialogUtil dialogUtil = new DialogUtil();
    static boolean isInternetAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        /*// Create Notification Channel. One time activity per application launch.
        createNotificationChannel();*/

        TextView holidayList = findViewById(R.id.holiday_list);
        holidayList.setText(HOLIDAY_LIST_TITLE);

        // Initialize MobileAds & Request for ads
        MobileAds.initialize(this, ADMOB_APP_ID);
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(TEST_DEVICE_ID).build();
        mAdView.loadAd(adRequest);
    }

    /**
     * Start "Holiday List" Activity
     */
    public void goToHolidayListPage(View view) throws ExecutionException, InterruptedException {
        if (isInternetAvailable()) {
            Intent intent = new Intent(HomeScreenActivity.this, HolidayListActivity.class);
            startActivity(intent);
        }
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
        return (requestCalendarReadPermission() && requestCalendarWritePermission());
    }

    public boolean isInternetAvailable() throws ExecutionException, InterruptedException {
        if (requestInternetPermission() && requestNetworkPermission()) {
            if (hasInternetAccess()) {
                return true;
            } else {
                Toast.makeText(this, NEED_INTERNET_CONNECTION, Toast.LENGTH_SHORT).show();
            }
        }

        return false;
    }

    /**
     * This method checks if INTERNET permission is given by user.If not, it will raise the request
     */
    public boolean requestInternetPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    PERMISSIONS_REQUEST_INTERNET);
        }
        return false;
    }

    /**
     * This method checks if NETWORK_ACCESS permission is given by user.If not, it will raise the request
     */
    public boolean requestNetworkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                    PERMISSIONS_REQUEST_NETWORK);
        }
        return false;
    }

    /**
     * This method checks if network is available to establish internet
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    /**
     * This method checks if internet connection is established
     */
    public boolean hasInternetAccess() throws ExecutionException, InterruptedException {
        if (isNetworkAvailable()) {
            new MyTask().execute().get();
            return isInternetAvailable;
        }
        return false;
    }

    /**
     * This method checks if CALENDAR_READ permission is given by user.If not, it will raise the request
     */
    public boolean requestCalendarReadPermission() {
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
    public boolean requestCalendarWritePermission() {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_NETWORK:
            case PERMISSIONS_REQUEST_INTERNET:
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, INTERNET_PERMISSION_WARNING, Toast.LENGTH_SHORT).show();
                }
                break;

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

    /**
     * Async Class To Perform Network Operations
     */
    private class MyTask extends AsyncTask<Void, Void, Void> {
        String result;

        @Override
        protected Void doInBackground(Void... urls) {
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL(NETWORK_TEST_URL)
                                .openConnection());
                urlc.setRequestProperty(USER_AGENT_KEY, USER_AGENT_VALUE);
                urlc.setRequestProperty(CONNECTION_KEY, CONNECTION_VALUE);
                urlc.setConnectTimeout(CONNECTION_TIMEOUT);
                urlc.connect();

                isInternetAvailable = (urlc.getResponseCode() == STATUS_CODE &&
                        urlc.getContentLength() == CONTENT_LENGTH);
            } catch (IOException e) {
                Log.e("", SOMETHING_WENT_WRONG, e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}