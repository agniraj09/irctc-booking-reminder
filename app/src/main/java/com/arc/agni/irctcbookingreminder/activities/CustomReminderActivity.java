package com.arc.agni.irctcbookingreminder.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
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

public class CustomReminderActivity extends AppCompatActivity {

    CalendarUtil calendarUtil = new CalendarUtil();
    static String input_title;
    static int input_date, input_month, input_year;
    static int travel_date, travel_month, travel_year;
    int dateX, monthX, yearX;
    EditText reminder_date;
    EditText travel_date_show;
    EditText travel_title;
    private AdView mAdView;
    static Calendar selectedTravelDate = Calendar.getInstance();
    public static boolean isTravelDateSelected = false;
    public static String reminderType = "Custom Reminder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_reminder);
        setTitle("Custom Reminder");

        reminder_date = findViewById(R.id.cr_reminderdate);
        travel_date_show = findViewById(R.id.cr_traveldate);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void showDatePickerDialog(View view) {
        if (isTravelDateSelected) {
            selectedTravelDate.set(travel_year, travel_month, travel_date, 0, 0);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    input_year = year;
                    input_month = month;
                    input_date = day;
                    String reminderDate = input_date + "/" + (input_month + 1) + "/" + input_year;
                    reminder_date.setText(reminderDate);
                }
            }, yearX, monthX, dateX);

            Calendar userShowDateStart = Calendar.getInstance();
            userShowDateStart.add(Calendar.DAY_OF_YEAR, 1);

            datePickerDialog.getDatePicker().setMinDate(userShowDateStart.getTimeInMillis() - 1000);
            datePickerDialog.getDatePicker().setMaxDate(selectedTravelDate.getTimeInMillis() - 1000);
            datePickerDialog.setTitle("");
            datePickerDialog.show();
        } else {
            Toast.makeText(this, "Please Select Travel Date First", Toast.LENGTH_SHORT).show();
        }
    }

    public void showDatePickerDialogOnButtonClick(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                travel_year = year;
                travel_month = month;
                travel_date = day;
                String travelDate = travel_date + "/" + (travel_month + 1) + "/" + travel_year;
                travel_date_show.setText(travelDate);
                isTravelDateSelected = true;
            }
        }, yearX, monthX, dateX);

        Calendar userShowDateStart = Calendar.getInstance();
        userShowDateStart.add(Calendar.DAY_OF_YEAR, 1);
        datePickerDialog.getDatePicker().setMinDate(userShowDateStart.getTimeInMillis() - 1000);
        datePickerDialog.setTitle("");
        datePickerDialog.show();
    }


    public void createEvent(View view) {
        ValidationUtil validationUtil = new ValidationUtil();
        travel_title = findViewById(R.id.cr_event_title_input);
        input_title = travel_title.getText().toString();

        if (validationUtil.advanceBookingValidation(input_title, input_date)) {
            AdvanceBookingReminderActivity advanceBookingReminderActivity = new AdvanceBookingReminderActivity();
            long calId = advanceBookingReminderActivity.getCalendarId(this);
            if (calId == -1) {
                advanceBookingReminderActivity.createCalendar(this);
                calId = advanceBookingReminderActivity.getCalendarId(this);
            }

            Calendar reminderDateAndTime = Calendar.getInstance();
            reminderDateAndTime.set(input_year, input_month, input_date, 7, 30);

            Calendar travelDateAndTime = Calendar.getInstance();
            travelDateAndTime.set(travel_year, travel_month, travel_date, 7, 30);

            ContentValues values = calendarUtil.setEventContentValues(calId, reminderDateAndTime, travelDateAndTime, input_title, reminderType);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Uri uri = getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
            long eventID = Long.parseLong(uri.getLastPathSegment());
            values = calendarUtil.setReminderContentValues(eventID);
            getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, values);

            DialogUtil.showDialogPostEventCreation(CustomReminderActivity.this, 3);
        } else {
            Toast.makeText(this, "Please Enter Valid Title and Date", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
