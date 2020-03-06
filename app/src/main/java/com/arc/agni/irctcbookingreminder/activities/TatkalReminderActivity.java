package com.arc.agni.irctcbookingreminder.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.CalendarContract;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.arc.agni.irctcbookingreminder.R;
import com.arc.agni.irctcbookingreminder.notification.ReminderBroadcast;
import com.arc.agni.irctcbookingreminder.utils.CalendarUtil;
import com.arc.agni.irctcbookingreminder.utils.DialogUtil;
import com.arc.agni.irctcbookingreminder.utils.ValidationUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static com.arc.agni.irctcbookingreminder.constants.Constants.*;

public class TatkalReminderActivity extends AppCompatActivity {

    CalendarUtil calendarUtil = new CalendarUtil();
    static String input_title;
    EditText travel_title;
    static int input_date = 0, input_month = 0, input_year = 0;
    int dateX, monthX, yearX;
    EditText travel_date;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tatkal_reminder);
        setTitle(TITLE_TATKAL_REMINDER);

        travel_date = findViewById(R.id.tr_traveldate);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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
        userShowDateStart.add(Calendar.DAY_OF_YEAR, _2_DAYS);

        datePickerDialog.getDatePicker().setMinDate(userShowDateStart.getTimeInMillis() - 1000);
        datePickerDialog.setTitle("");
        datePickerDialog.show();
    }

    public void createEvent(View view) {
        ValidationUtil validationUtil = new ValidationUtil();
        travel_title = findViewById(R.id.tr_event_title_input);
        input_title = travel_title.getText().toString();

        CheckBox ac = findViewById(R.id.acCoach);
        CheckBox nonAc = findViewById(R.id.nonAcCoach);
        boolean checkedAtleastOne = ac.isChecked() | nonAc.isChecked();

        if (validationUtil.advanceBookingValidation(input_title, input_date)) {
            if (checkedAtleastOne) {
                AdvanceBookingReminderActivity advanceBookingReminderActivity = new AdvanceBookingReminderActivity();
                long calId = advanceBookingReminderActivity.getCalendarId(this);
                if (calId == -1) {
                    advanceBookingReminderActivity.createCalendar(this);
                    calId = advanceBookingReminderActivity.getCalendarId(this);
                }

                Calendar reminderDateAndTime = Calendar.getInstance();

                if (ac.isChecked()) {
                    reminderDateAndTime.set(input_year, input_month, input_date, TATKAL_BOOKING_AC_REMINDER_HOUR, TATKAL_BOOKING__AC_REMINDER_MINUTE);
                    reminderDateAndTime.add(Calendar.DAY_OF_YEAR, MINUS_1_DAY);
                    createReminder(calId, reminderDateAndTime);
                }

                if (nonAc.isChecked()) {
                    reminderDateAndTime.set(input_year, input_month, input_date, TATKAL_BOOKING_NON_AC_REMINDER_HOUR, TATKAL_BOOKING__NON_AC_REMINDER_MINUTE);
                    reminderDateAndTime.add(Calendar.DAY_OF_YEAR, MINUS_1_DAY);
                    createReminder(calId, reminderDateAndTime);
                }

                DialogUtil.showDialogPostEventCreation(TatkalReminderActivity.this, IND_TATKAL_REMINDER);

                long time = System.currentTimeMillis();
                long timePlusTen = 1000 * 5;
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                new ReminderBroadcast().createNotification(TatkalReminderActivity.this, alarmManager, time + timePlusTen);
            } else {
                Toast.makeText(this, COACH_PREFERENCE_WARNING, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, TITLE_AND_DATE_WARNING, Toast.LENGTH_SHORT).show();
        }
    }

    public void createReminder(long calId, Calendar reminderDateAndTime) {
        Calendar dummy = Calendar.getInstance();

        ContentValues values = calendarUtil.setEventContentValues(calId, reminderDateAndTime, dummy, input_title, REMINDER_TYPE_TATKAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Uri uri = getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
        long eventID = Long.parseLong(uri.getLastPathSegment());
        values = calendarUtil.setReminderContentValues(eventID);
        getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, values);
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
