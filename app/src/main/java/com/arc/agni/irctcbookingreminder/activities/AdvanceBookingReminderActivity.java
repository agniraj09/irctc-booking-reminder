package com.arc.agni.irctcbookingreminder.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.arc.agni.irctcbookingreminder.R;

import static com.arc.agni.irctcbookingreminder.constants.Constants.*;

import com.arc.agni.irctcbookingreminder.utils.CalendarUtil;
import com.arc.agni.irctcbookingreminder.utils.DialogUtil;
import com.arc.agni.irctcbookingreminder.utils.ValidationUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class AdvanceBookingReminderActivity extends AppCompatActivity {

    static int inputDay, inputMonth, inputYear;
    EditText travelDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_booking);
        setTitle(TITLE_120_DAY_REMINDER);

        // 'travelDate' TextView is accessed at class level.
        travelDate = findViewById(R.id.ab_traveldate);

        // Extra info will be passed only from BOOKING_DAY_CALCULATOR Activity with preset TRAVEL_DATE. No Extra info will be passed from HOME Activity
        inputDay = getIntent().getIntExtra(LABEL_INPUT_DAY, 0);
        inputMonth = getIntent().getIntExtra(LABEL_INPUT_MONTH, 0);
        inputYear = getIntent().getIntExtra(LABEL_INPUT_YEAR, 0);
        if (inputDay != 0) {
            String travelDateText = inputDay + "/" + (inputMonth + 1) + "/" + inputYear;
            travelDate.setText(travelDateText);
        }

        // Request for ad
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    /**
     * This method is used to display the Date Picker. The minimum start date is TODAY + 121 DAYS.
     */
    public void showDatePickerDialogOnButtonClick(View view) {
        int day = 0, month = 0, year = 0;
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view1, datePickerYear, datePickerMonth, datePickerDay) -> {
            inputYear = datePickerYear;
            inputMonth = datePickerMonth;
            inputDay = datePickerDay;
            String travelDateText = inputDay + "/" + (inputMonth + 1) + "/" + inputYear;
            travelDate.setText(travelDateText);
        }, year, month, day);

        Calendar userShowDateStart = Calendar.getInstance();
        userShowDateStart.add(Calendar.DAY_OF_YEAR, _121_DAYS);

        datePickerDialog.getDatePicker().setMinDate(userShowDateStart.getTimeInMillis() - 1000);
        datePickerDialog.setTitle("");
        datePickerDialog.show();
    }

    /**
     * This method is used to create 120_DAY_REMINDER
     */
    public void createEvent(View view) {
        // Check if CALENDAR_WRITE PERMISSION is provided
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Retrieve title
        String reminderTitle = ((EditText) findViewById(R.id.ab_event_title_input)).getText().toString();

        // Create 120 Day reminder
        if (ValidationUtil.titleAndDateValidation(reminderTitle, inputDay)) {
            // Build reminderDateAndTime
            Calendar reminderDateAndTime = Calendar.getInstance();
            reminderDateAndTime.set(inputYear, inputMonth, inputDay, _120_DAY_BOOKING_REMINDER_HOUR, _120_DAY_BOOKING_REMINDER_MINUTE);
            reminderDateAndTime.add(Calendar.DAY_OF_YEAR, MINUS_120_DAYS);

            // Create reminder
            CalendarUtil.createReminder(reminderTitle, reminderDateAndTime, Calendar.getInstance(), REMINDER_TYPE_120_DAY, this);
            DialogUtil.showDialogPostEventCreation(AdvanceBookingReminderActivity.this, IND_120_DAY_REMINDER);
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
