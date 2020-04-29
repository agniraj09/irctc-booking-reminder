package com.arc.agni.irctcbookingreminder.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
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

import static com.arc.agni.irctcbookingreminder.constants.Constants.ALERT_TYPE_ALARM;
import static com.arc.agni.irctcbookingreminder.constants.Constants.ALERT_TYPE_NOTIFICATION;
import static com.arc.agni.irctcbookingreminder.constants.Constants.AT;
import static com.arc.agni.irctcbookingreminder.constants.Constants.BOOKING_OPENING_TIME;
import static com.arc.agni.irctcbookingreminder.constants.Constants.BOOKING_STARTED;
import static com.arc.agni.irctcbookingreminder.constants.Constants.BOOKING_WILL_START;
import static com.arc.agni.irctcbookingreminder.constants.Constants.CUSTOM_BOOKING_REMINDER_HOUR;
import static com.arc.agni.irctcbookingreminder.constants.Constants.CUSTOM_BOOKING_REMINDER_MINUTE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.IST;
import static com.arc.agni.irctcbookingreminder.constants.Constants.MINUS_120_DAYS;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE_CUSTOM;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TITLE_AND_DATE_WARNING;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TITLE_CUSTOM_REMINDER;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TRAVEL_DATE_WARNING;
import static com.arc.agni.irctcbookingreminder.constants.Constants._1_DAY;

public class CustomReminderActivity extends AppCompatActivity {

    static int inputDay, inputMonth, inputYear;
    static int travelDay, travelMonth, travelYear;
    int dateX, monthX, yearX;
    EditText travelDate, reminderDate;

    static Calendar selectedTravelDate = Calendar.getInstance();
    public static boolean isTravelDateSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_reminder);
        setTitle(TITLE_CUSTOM_REMINDER);

        // 'travelDate' & 'reminderDate' TextView is accessed at class level.
        travelDate = findViewById(R.id.cr_traveldate);
        reminderDate = findViewById(R.id.cr_reminderdate);
        isTravelDateSelected = false;

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
            clearReminderDateInput();
            showBookingDateWhenUserSelectsTravelDate();
        }, yearX, monthX, dateX);

        Calendar userShowDateStart = Calendar.getInstance();
        userShowDateStart.add(Calendar.DAY_OF_YEAR, _1_DAY);
        datePickerDialog.getDatePicker().setMinDate(userShowDateStart.getTimeInMillis() - 1000);
        datePickerDialog.setTitle("");
        datePickerDialog.show();
    }

    public void clearReminderDateInput() {
        inputYear = 0;
        inputMonth = 0;
        inputDay = 0;
        reminderDate.setText(null);
    }

    /**
     * This method is used to calculate the BOOKING OPENING DATE based on TRAVEL DATE chosen by user. The final result is formatted with RED|GREEN & BOLD text format and shown to user. If eligible, Create reminder option also will be shown to user.
     */
    public void showBookingDateWhenUserSelectsTravelDate() {
        if (travelDay > 0) {
            boolean bookingStarted;
            String fullText;

            // Calculate booking Date from User selected travel date
            Calendar bookingDate = Calendar.getInstance();
            bookingDate.set(travelYear, travelMonth, travelDay);
            bookingDate.add(Calendar.DAY_OF_YEAR, MINUS_120_DAYS);

            // Make up final text to be shown in screen
            String bookingDateText = CommonUtil.formatCalendarDateToFullText(bookingDate);
            if (bookingDate.getTime().after(Calendar.getInstance().getTime())) {
                bookingStarted = false;
                fullText = BOOKING_WILL_START + "\n" + bookingDateText + AT + BOOKING_OPENING_TIME + IST;
            } else {
                bookingStarted = true;
                fullText = BOOKING_STARTED + "\n" + bookingDateText + AT + BOOKING_OPENING_TIME + IST;
            }

            // Format final text
            SpannableString finalResultText = new SpannableString(fullText);
            final int startIndex = fullText.indexOf(bookingDateText);
            final int endIndex = fullText.indexOf(bookingDateText) + bookingDateText.length();

            if (bookingStarted) {
                finalResultText.setSpan(new ForegroundColorSpan(Color.RED), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                finalResultText.setSpan(new RelativeSizeSpan(1.2f), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                finalResultText.setSpan(new ForegroundColorSpan(0xFF388C16), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                finalResultText.setSpan(new RelativeSizeSpan(1.2f), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            TextView result = findViewById(R.id.cr_result_textview);
            result.setBackgroundResource(R.drawable.result_textview);
            result.setText(finalResultText);
        }
    }

    /**
     * This method is used to create CUSTOM REMINDER
     */
    public void createEvent(View view) {
        // Check if CALENDAR_WRITE PERMISSION is provided
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Retrieve title & alert type
        String reminderTitle = ((EditText) findViewById(R.id.cr_event_title_input)).getText().toString();
        String alertType = ((RadioButton) findViewById(R.id.alarm)).isChecked() ? ALERT_TYPE_ALARM : ALERT_TYPE_NOTIFICATION;

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
            CommonUtil.buildAndScheduleNotification(REMINDER_TYPE_CUSTOM, reminderTitle, travelDay, travelMonth, travelYear, CUSTOM_BOOKING_REMINDER_HOUR, this, reminderDateAndTime, eventId, alertType);

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
                    assert imm != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
