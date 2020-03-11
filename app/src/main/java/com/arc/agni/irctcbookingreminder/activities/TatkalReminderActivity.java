package com.arc.agni.irctcbookingreminder.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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

import static com.arc.agni.irctcbookingreminder.constants.Constants.AC_COACH;
import static com.arc.agni.irctcbookingreminder.constants.Constants.BOOKING_WILL_START;
import static com.arc.agni.irctcbookingreminder.constants.Constants.COACH_PREFERENCE_WARNING;
import static com.arc.agni.irctcbookingreminder.constants.Constants.DAYS;
import static com.arc.agni.irctcbookingreminder.constants.Constants.IND_TATKAL_REMINDER;
import static com.arc.agni.irctcbookingreminder.constants.Constants.IST;
import static com.arc.agni.irctcbookingreminder.constants.Constants.MINUS_1_DAY;
import static com.arc.agni.irctcbookingreminder.constants.Constants.MONTHS;
import static com.arc.agni.irctcbookingreminder.constants.Constants.NON_AC_COACH;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE_TATKAL;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TATKAL_BOOKING_AC_REMINDER_HOUR;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TATKAL_BOOKING_NON_AC_REMINDER_HOUR;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TATKAL_BOOKING__AC_REMINDER_MINUTE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TATKAL_BOOKING__NON_AC_REMINDER_MINUTE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TATKAL_OPENING_TIME_AC;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TATKAL_OPENING_TIME_NON_AC;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TITLE_AND_DATE_WARNING;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TITLE_TATKAL_REMINDER;
import static com.arc.agni.irctcbookingreminder.constants.Constants._1_DAY;
import static com.arc.agni.irctcbookingreminder.constants.Constants._2_DAYS;

public class TatkalReminderActivity extends AppCompatActivity {

    static int inputDay, inputMonth, inputYear;
    EditText travelDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tatkal_reminder);
        setTitle(TITLE_TATKAL_REMINDER);

        // 'travelDate' TextView is accessed at class level.
        travelDate = findViewById(R.id.tr_traveldate);

        // Request for ad
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

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
        Calendar bookingTime = Calendar.getInstance();
        bookingTime.set(bookingTime.get(Calendar.YEAR), bookingTime.get(Calendar.MONTH), bookingTime.get(Calendar.DAY_OF_MONTH), TATKAL_BOOKING_NON_AC_REMINDER_HOUR, TATKAL_BOOKING__NON_AC_REMINDER_MINUTE);
        if (userShowDateStart.getTime().after(bookingTime.getTime())) {
            userShowDateStart.add(Calendar.DAY_OF_YEAR, _2_DAYS);
        } else {
            userShowDateStart.add(Calendar.DAY_OF_YEAR, _1_DAY);
        }

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
            bookingDate.add(Calendar.DAY_OF_YEAR, MINUS_1_DAY);

            // Make up final text to be shown in screen
            String bookingDateText = MONTHS[bookingDate.get(Calendar.MONTH)] + " " + bookingDate.get(Calendar.DAY_OF_MONTH) + ", " + bookingDate.get(Calendar.YEAR) + " (" + DAYS[bookingDate.get(Calendar.DAY_OF_WEEK) - 1] + ") ";
            fullText = BOOKING_WILL_START + "\n" + bookingDateText + "\n" + AC_COACH + " - " + TATKAL_OPENING_TIME_AC + IST + ", " + NON_AC_COACH + " - " + TATKAL_OPENING_TIME_NON_AC + IST;

            // Format final text
            SpannableString finalResultText = new SpannableString(fullText);
            final int startIndex = fullText.indexOf(bookingDateText);
            final int endIndex = fullText.indexOf(bookingDateText) + bookingDateText.length();

            //finalResultText.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            finalResultText.setSpan(new ForegroundColorSpan(0xFF388C16), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            finalResultText.setSpan(new RelativeSizeSpan(1.2f), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            TextView result = findViewById(R.id.tb_result_textview);
            result.setBackgroundResource(R.drawable.result_textview);
            result.setText(finalResultText);
        }
    }

    /**
     * This method is used to create TATKAL_REMINDER
     */
    public void createEvent(View view) {
        // Check if CALENDAR_WRITE PERMISSION is provided
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Retrieve title
        String reminderTitle = ((EditText) findViewById(R.id.tr_event_title_input)).getText().toString();
        // Retrieve Coach Preference
        boolean isACChecked = ((CheckBox) findViewById(R.id.acCoach)).isChecked();
        boolean isNonACChecked = ((CheckBox) findViewById(R.id.nonAcCoach)).isChecked();

        // Create Tatkal reminder
        if (ValidationUtil.titleAndDateValidation(reminderTitle, inputDay)) {
            if (isACChecked | isNonACChecked) {
                // Build reminderDateAndTime
                Calendar reminderDateAndTime = Calendar.getInstance();
                // For AC Coach, Reminder Time is TATKAL_BOOKING_AC_REMINDER_HOUR : TATKAL_BOOKING__AC_REMINDER_MINUTE
                if (isACChecked) {
                    reminderDateAndTime.set(inputYear, inputMonth, inputDay, TATKAL_BOOKING_AC_REMINDER_HOUR, TATKAL_BOOKING__AC_REMINDER_MINUTE);
                    reminderDateAndTime.add(Calendar.DAY_OF_YEAR, MINUS_1_DAY);
                    CalendarUtil.createReminder(reminderTitle, reminderDateAndTime, Calendar.getInstance(), REMINDER_TYPE_TATKAL, this);
                    // Schedule notification
                    String notificationText = ReminderBroadcast.buildNotificationContent(REMINDER_TYPE_TATKAL, reminderTitle, inputDay, inputMonth, inputYear, TATKAL_BOOKING_AC_REMINDER_HOUR);
                    ReminderBroadcast.scheduleNotification(notificationText, reminderDateAndTime, this);
                }

                // For AC Coach, Reminder Time is TATKAL_BOOKING_NON_AC_REMINDER_HOUR : TATKAL_BOOKING__NON_AC_REMINDER_MINUTE
                if (isNonACChecked) {
                    reminderDateAndTime.set(inputYear, inputMonth, inputDay, TATKAL_BOOKING_NON_AC_REMINDER_HOUR, TATKAL_BOOKING__NON_AC_REMINDER_MINUTE);
                    reminderDateAndTime.add(Calendar.DAY_OF_YEAR, MINUS_1_DAY);
                    CalendarUtil.createReminder(reminderTitle, reminderDateAndTime, Calendar.getInstance(), REMINDER_TYPE_TATKAL, this);
                    // Schedule notification
                    String notificationText = ReminderBroadcast.buildNotificationContent(REMINDER_TYPE_TATKAL, reminderTitle, inputDay, inputMonth, inputYear, TATKAL_BOOKING_NON_AC_REMINDER_HOUR);
                    ReminderBroadcast.scheduleNotification(notificationText, reminderDateAndTime, this);
                }

                // Show Success Pop-up
                DialogUtil.showDialogPostEventCreation(TatkalReminderActivity.this, IND_TATKAL_REMINDER);

            } else {
                Toast.makeText(this, COACH_PREFERENCE_WARNING, Toast.LENGTH_SHORT).show();
            }
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
