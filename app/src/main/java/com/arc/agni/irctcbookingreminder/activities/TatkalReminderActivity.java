package com.arc.agni.irctcbookingreminder.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
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

import static com.arc.agni.irctcbookingreminder.constants.Constants.COACH_PREFERENCE_WARNING;
import static com.arc.agni.irctcbookingreminder.constants.Constants.IND_TATKAL_REMINDER;
import static com.arc.agni.irctcbookingreminder.constants.Constants.MINUS_1_DAY;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE_TATKAL;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TATKAL_BOOKING_AC_REMINDER_HOUR;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TATKAL_BOOKING_NON_AC_REMINDER_HOUR;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TATKAL_BOOKING__AC_REMINDER_MINUTE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TATKAL_BOOKING__NON_AC_REMINDER_MINUTE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TITLE_AND_DATE_WARNING;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TITLE_TATKAL_REMINDER;
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
        }, year, month, day);

        Calendar userShowDateStart = Calendar.getInstance();
        userShowDateStart.add(Calendar.DAY_OF_YEAR, _2_DAYS);

        datePickerDialog.getDatePicker().setMinDate(userShowDateStart.getTimeInMillis() - 1000);
        datePickerDialog.setTitle("");
        datePickerDialog.show();
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
                }

                // For AC Coach, Reminder Time is TATKAL_BOOKING_NON_AC_REMINDER_HOUR : TATKAL_BOOKING__NON_AC_REMINDER_MINUTE
                if (isNonACChecked) {
                    reminderDateAndTime.set(inputYear, inputMonth, inputDay, TATKAL_BOOKING_NON_AC_REMINDER_HOUR, TATKAL_BOOKING__NON_AC_REMINDER_MINUTE);
                    reminderDateAndTime.add(Calendar.DAY_OF_YEAR, MINUS_1_DAY);
                    CalendarUtil.createReminder(reminderTitle, reminderDateAndTime, Calendar.getInstance(), REMINDER_TYPE_TATKAL, this);
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
