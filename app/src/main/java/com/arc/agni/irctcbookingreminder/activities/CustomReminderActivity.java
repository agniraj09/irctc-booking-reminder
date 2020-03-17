package com.arc.agni.irctcbookingreminder.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.arc.agni.irctcbookingreminder.R;
import com.arc.agni.irctcbookingreminder.notification.ReminderBroadcast;
import com.arc.agni.irctcbookingreminder.utils.CalendarUtil;
import com.arc.agni.irctcbookingreminder.utils.CommonUtil;
import com.arc.agni.irctcbookingreminder.utils.DialogUtil;
import com.arc.agni.irctcbookingreminder.utils.ValidationUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static com.arc.agni.irctcbookingreminder.constants.Constants.CUSTOM_BOOKING_REMINDER_HOUR;
import static com.arc.agni.irctcbookingreminder.constants.Constants.CUSTOM_BOOKING_REMINDER_MINUTE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.IND_CUSTOM_REMINDER;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE_120_DAY;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE_CUSTOM;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE_TATKAL_NON_AC;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TITLE_AND_DATE_WARNING;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TITLE_CUSTOM_REMINDER;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TRAVEL_DATE_WARNING;
import static com.arc.agni.irctcbookingreminder.constants.Constants._120_DAY_BOOKING_REMINDER_HOUR;
import static com.arc.agni.irctcbookingreminder.constants.Constants._1_DAY;

public class CustomReminderActivity extends AppCompatActivity {

    static int inputDay, inputMonth, inputYear;
    static int travelDay, travelMonth, travelYear;
    int dateX, monthX, yearX;
    EditText travelDate, reminderDate;

    static Calendar selectedTravelDate = Calendar.getInstance();
    public static boolean isTravelDateSelected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_reminder);
        setTitle(TITLE_CUSTOM_REMINDER);

        // 'travelDate' & 'reminderDate' TextView is accessed at class level.
        travelDate = findViewById(R.id.cr_traveldate);
        reminderDate = findViewById(R.id.cr_reminderdate);

        // Request for ad
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void showDatePickerDialogForReminderDate(View view) {
        if (isTravelDateSelected) {
            selectedTravelDate.set(travelYear, travelMonth, travelDay, 0, 0);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view1, datePickerYear, datePickerMonth, datePickerDay) -> {
                inputYear = datePickerYear;
                inputMonth = datePickerMonth;
                inputDay = datePickerDay;
                String reminderDateText = inputDay + "/" + (inputMonth + 1) + "/" + inputYear;
                reminderDate.setText(reminderDateText);
            }, yearX, monthX, dateX);

            Calendar userShowDateStart = Calendar.getInstance();
            userShowDateStart.add(Calendar.DAY_OF_YEAR, _1_DAY);

            datePickerDialog.getDatePicker().setMinDate(userShowDateStart.getTimeInMillis() - 1000);
            datePickerDialog.getDatePicker().setMaxDate(selectedTravelDate.getTimeInMillis() - 1000);
            datePickerDialog.setTitle("");
            datePickerDialog.show();
        } else {
            Toast.makeText(this, TRAVEL_DATE_WARNING, Toast.LENGTH_SHORT).show();
        }
    }

    public void showDatePickerDialogForTravelDate(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view1, datePickerYear, datePickerMonth, datePickerDay) -> {
            travelYear = datePickerYear;
            travelMonth = datePickerMonth;
            travelDay = datePickerDay;
            String travelDateText = travelDay + "/" + (travelMonth + 1) + "/" + travelYear;
            travelDate.setText(travelDateText);
            isTravelDateSelected = true;
        }, yearX, monthX, dateX);

        Calendar userShowDateStart = Calendar.getInstance();
        userShowDateStart.add(Calendar.DAY_OF_YEAR, _1_DAY);
        datePickerDialog.getDatePicker().setMinDate(userShowDateStart.getTimeInMillis() - 1000);
        datePickerDialog.setTitle("");
        datePickerDialog.show();
    }

    /**
     * This method is used to create CUSTOM REMINDER
     */
    public void createEvent(View view) {
        // Check if CALENDAR_WRITE PERMISSION is provided
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Retrieve title
        String reminderTitle = ((EditText) findViewById(R.id.cr_event_title_input)).getText().toString();

        // Create Custom reminder
        if (ValidationUtil.titleAndDateValidation(reminderTitle, inputDay)) {

            // Build reminderDateAndTime
            Calendar reminderDateAndTime = Calendar.getInstance();
            reminderDateAndTime.set(inputYear, inputMonth, inputDay, CUSTOM_BOOKING_REMINDER_HOUR, CUSTOM_BOOKING_REMINDER_MINUTE);
            // Build travelDateAndTime(exDateAndTime)
            Calendar travelDateAndTime = Calendar.getInstance();
            travelDateAndTime.set(travelYear, travelMonth, travelDay, CUSTOM_BOOKING_REMINDER_HOUR, CUSTOM_BOOKING_REMINDER_MINUTE);

            // Create reminder
            long eventId = CalendarUtil.createReminder(reminderTitle, reminderDateAndTime, travelDateAndTime, REMINDER_TYPE_CUSTOM, this);

            // Schedule notification
            CommonUtil.buildAndScheduleNotification(REMINDER_TYPE_CUSTOM, reminderTitle, travelDay, travelMonth, travelYear, CUSTOM_BOOKING_REMINDER_HOUR, this, reminderDateAndTime, eventId);

            // Show Success Pop-up
            Intent intent = CommonUtil.createIntentPostReminderCreation(this, reminderTitle, REMINDER_TYPE_CUSTOM, travelDay, travelMonth, travelYear, reminderDateAndTime);
            startActivity(intent);
        } else {
            Toast.makeText(this, TITLE_AND_DATE_WARNING, Toast.LENGTH_SHORT).show();
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
