package com.arc.agni.irctcbookingreminder.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.arc.agni.irctcbookingreminder.R;
import com.arc.agni.irctcbookingreminder.utils.CalendarUtil;
import com.arc.agni.irctcbookingreminder.utils.DialogUtil;
import com.arc.agni.irctcbookingreminder.utils.ValidationUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;

public class AdvanceBookingReminderActivity extends AppCompatActivity {

    CalendarUtil calendarUtil = new CalendarUtil();
    static String input_title;
    static int input_date, input_month, input_year;
    int dateX, monthX, yearX;
    EditText travel_date;
    EditText travel_title;
    private AdView mAdView;
    public  String reminderType = "120 Day Reminder";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_booking);
        setTitle("120 Day Reminder");

        travel_date = findViewById(R.id.ab_traveldate);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    public void createCalendar(Context context) {
        ContentValues values = calendarUtil.setCalendarContentValues();
        Uri.Builder builder =
                CalendarContract.Calendars.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarUtil.MY_ACCOUNT_NAME);
        builder.appendQueryParameter(
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.ACCOUNT_TYPE_LOCAL);
        builder.appendQueryParameter(
                CalendarContract.CALLER_IS_SYNCADAPTER,
                "true");
        Uri uri =
                context.getContentResolver().insert(builder.build(), values);
    }

    public void showDatePickerDialogOnButtonClick(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                input_year = year;
                input_month = month;
                input_date = day;
                String travelDate = input_date + "/" + (input_month + 1) + "/" + input_year;
                travel_date.setText(travelDate);
            }
        }, yearX, monthX, dateX);

        Calendar userShowDateStart = Calendar.getInstance();
        userShowDateStart.add(Calendar.DAY_OF_YEAR, 121);

        datePickerDialog.getDatePicker().setMinDate(userShowDateStart.getTimeInMillis() - 1000);
        datePickerDialog.setTitle("");
        datePickerDialog.show();
    }

    public long getCalendarId(Context context) {
        long noSuchCalendarIndicator = -1;
        String[] projection = new String[]{CalendarContract.Calendars._ID};
        String selection =
                CalendarContract.Calendars.ACCOUNT_NAME +
                        " = ? AND " +
                        CalendarContract.Calendars.ACCOUNT_TYPE +
                        " = ? ";

        String[] selArgs =
                new String[]{
                        CalendarUtil.MY_ACCOUNT_NAME,
                        CalendarContract.ACCOUNT_TYPE_LOCAL};
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return noSuchCalendarIndicator;
        }
        Cursor cursor =
                context.getContentResolver().
                        query(
                                CalendarContract.Calendars.CONTENT_URI,
                                projection,
                                selection,
                                selArgs,
                                null);
        if (cursor.moveToFirst()) {
            return cursor.getLong(0);
        }
        return -1;
    }

    public void createEvent(View view) {
        ValidationUtil validationUtil = new ValidationUtil();
        travel_title = findViewById(R.id.ab_event_title_input);
        input_title = travel_title.getText().toString();

        if (validationUtil.advanceBookingValidation(input_title, input_date)) {
            long calId = getCalendarId(this);
            if (calId == -1) {
                createCalendar(this);
                calId = getCalendarId(this);
            }

            Calendar reminderDateAndTime = Calendar.getInstance();
            reminderDateAndTime.set(input_year, input_month, input_date, 7, 0);
            reminderDateAndTime.add(Calendar.DAY_OF_YEAR, -120);

            Calendar dummy = Calendar.getInstance();
            ContentValues values = calendarUtil.setEventContentValues(calId, reminderDateAndTime, dummy, input_title, reminderType);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Uri uri = getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
            long eventID = Long.parseLong(uri.getLastPathSegment());
            values = calendarUtil.setReminderContentValues(eventID);
            getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, values);

            DialogUtil.showDialogPostEventCreation(AdvanceBookingReminderActivity.this, 1);
        } else {
            Toast.makeText(this, "Please Enter Valid Title and Date", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}
