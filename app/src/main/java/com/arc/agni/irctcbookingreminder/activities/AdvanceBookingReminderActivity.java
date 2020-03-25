package com.arc.agni.irctcbookingreminder.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arc.agni.irctcbookingreminder.R;
import com.arc.agni.irctcbookingreminder.utils.CalendarUtil;
import com.arc.agni.irctcbookingreminder.utils.CommonUtil;
import com.arc.agni.irctcbookingreminder.utils.ValidationUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static com.arc.agni.irctcbookingreminder.constants.Constants.AT;
import static com.arc.agni.irctcbookingreminder.constants.Constants.BOOKING_OPENING_TIME;
import static com.arc.agni.irctcbookingreminder.constants.Constants.BOOKING_WILL_START;
import static com.arc.agni.irctcbookingreminder.constants.Constants.DAYS;
import static com.arc.agni.irctcbookingreminder.constants.Constants.IST;
import static com.arc.agni.irctcbookingreminder.constants.Constants.LABEL_INPUT_DAY;
import static com.arc.agni.irctcbookingreminder.constants.Constants.LABEL_INPUT_MONTH;
import static com.arc.agni.irctcbookingreminder.constants.Constants.LABEL_INPUT_YEAR;
import static com.arc.agni.irctcbookingreminder.constants.Constants.LABEL_TRAVEL_HINT;
import static com.arc.agni.irctcbookingreminder.constants.Constants.MINUS_120_DAYS;
import static com.arc.agni.irctcbookingreminder.constants.Constants.MONTHS;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE_120_DAY;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TITLE_120_DAY_REMINDER;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TITLE_AND_DATE_WARNING;
import static com.arc.agni.irctcbookingreminder.constants.Constants._120_DAY_BOOKING_REMINDER_HOUR;
import static com.arc.agni.irctcbookingreminder.constants.Constants._120_DAY_BOOKING_REMINDER_MINUTE;
import static com.arc.agni.irctcbookingreminder.constants.Constants._121_DAYS;

public class AdvanceBookingReminderActivity extends AppCompatActivity {

    static int inputDay, inputMonth, inputYear;
    String travelHint;
    EditText travelDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_booking);
        setTitle(TITLE_120_DAY_REMINDER);

        // 'travelDate' TextView is accessed at class level.
        travelDate = findViewById(R.id.ab_traveldate);

        // Extra info will be passed only from BOOKING_DAY_CALCULATOR & HOLIDAY_LIST Activity with preset TRAVEL_DATE. No Extra info will be passed from HOME Activity
        inputDay = getIntent().getIntExtra(LABEL_INPUT_DAY, 0);
        inputMonth = getIntent().getIntExtra(LABEL_INPUT_MONTH, 0);
        inputYear = getIntent().getIntExtra(LABEL_INPUT_YEAR, 0);
        travelHint = getIntent().getStringExtra(LABEL_TRAVEL_HINT);
        if (inputDay != 0) {
            String travelDateText = inputDay + "/" + (inputMonth + 1) + "/" + inputYear;
            travelDate.setText(travelDateText);
        }
        if (null != travelHint && !travelHint.isEmpty()) {
            ((EditText) findViewById(R.id.ab_event_title_input)).setText(travelHint);
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
            showBookingDateWhenUserSelectsTravelDate();
        }, year, month, day);

        Calendar userShowDateStart = Calendar.getInstance();
        userShowDateStart.add(Calendar.DAY_OF_YEAR, _121_DAYS);

        datePickerDialog.getDatePicker().setMinDate(userShowDateStart.getTimeInMillis() - 1000);
        datePickerDialog.setTitle("");
        datePickerDialog.show();
    }

    /**
     * This method will calculate & show the BOOKING_DATE when user selects TRAVEL_DATE in DatePickerDialog
     */
    public void showBookingDateWhenUserSelectsTravelDate() {
        if (inputDay > 0) {
            String fullText;

            // Calculate booking Date from User selected travel date
            Calendar bookingDate = Calendar.getInstance();
            bookingDate.set(inputYear, inputMonth, inputDay);
            bookingDate.add(Calendar.DAY_OF_YEAR, MINUS_120_DAYS);

            // Make up final text to be shown in screen
            String bookingDateText = MONTHS[bookingDate.get(Calendar.MONTH)] + " " + bookingDate.get(Calendar.DAY_OF_MONTH) + ", " + bookingDate.get(Calendar.YEAR) + " (" + DAYS[bookingDate.get(Calendar.DAY_OF_WEEK) - 1] + ") ";
            fullText = BOOKING_WILL_START + "\n" + bookingDateText + AT + BOOKING_OPENING_TIME + IST;

            // Format final text
            SpannableString finalResultText = new SpannableString(fullText);
            final int startIndex = fullText.indexOf(bookingDateText);
            final int endIndex = fullText.indexOf(bookingDateText) + bookingDateText.length();

            //finalResultText.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            finalResultText.setSpan(new ForegroundColorSpan(0xFF388C16), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            finalResultText.setSpan(new RelativeSizeSpan(1.2f), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            TextView result = findViewById(R.id.ab_result_textview);
            result.setBackgroundResource(R.drawable.result_textview);
            result.setText(finalResultText);
        }
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
            long eventId = CalendarUtil.createReminder(reminderTitle, reminderDateAndTime, Calendar.getInstance(), REMINDER_TYPE_120_DAY, this);

            // Schedule notification
            CommonUtil.buildAndScheduleNotification(REMINDER_TYPE_120_DAY, reminderTitle, inputDay, inputMonth, inputYear, _120_DAY_BOOKING_REMINDER_HOUR, this, reminderDateAndTime, eventId);

            // Show Success Screen
            Intent intent = CommonUtil.createIntentPostReminderCreation(this, reminderTitle, REMINDER_TYPE_120_DAY, inputDay, inputMonth, inputYear, reminderDateAndTime);
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
                    assert imm != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
