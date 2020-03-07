package com.arc.agni.irctcbookingreminder.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arc.agni.irctcbookingreminder.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import static com.arc.agni.irctcbookingreminder.constants.Constants.AT;
import static com.arc.agni.irctcbookingreminder.constants.Constants.BOOKING_OPENING_TIME;
import static com.arc.agni.irctcbookingreminder.constants.Constants.BOOKING_STARTED;
import static com.arc.agni.irctcbookingreminder.constants.Constants.BOOKING_WILL_START;
import static com.arc.agni.irctcbookingreminder.constants.Constants.DATE_WARNING;
import static com.arc.agni.irctcbookingreminder.constants.Constants.DAYS;
import static com.arc.agni.irctcbookingreminder.constants.Constants.IST;
import static com.arc.agni.irctcbookingreminder.constants.Constants.LABEL_INPUT_DAY;
import static com.arc.agni.irctcbookingreminder.constants.Constants.LABEL_INPUT_MONTH;
import static com.arc.agni.irctcbookingreminder.constants.Constants.LABEL_INPUT_YEAR;
import static com.arc.agni.irctcbookingreminder.constants.Constants.MINUS_120_DAYS;
import static com.arc.agni.irctcbookingreminder.constants.Constants.MONTHS;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TITLE_BOOKING_DAY_CALCULATOR;
import static com.arc.agni.irctcbookingreminder.constants.Constants._1_DAY;

public class BookingDayCalculatorActivity extends AppCompatActivity {

    EditText travelDate;
    static int inputDay, inputMonth, inputYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_calulator);
        setTitle(TITLE_BOOKING_DAY_CALCULATOR);

        // 'travelDate' TextView is accessed at class level.
        travelDate = findViewById(R.id.bc_traveldate);

        // Request for ad
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    /**
     * This method is used to display the Date Picker. The minimum start date is TODAY + 1 DAY.
     */
    public void showDatePickerDialogOnButtonClick(View view) {
        int day = 0, month = 0, year = 0;
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view1, datePickerYear, datePickerMonth, datePickerDay) -> {
            inputYear = datePickerYear;
            inputMonth = datePickerMonth;
            inputDay = datePickerDay;
            String travelDateText = inputDay + "/" + (inputMonth + 1) + "/" + inputYear;
            travelDate.setText(travelDateText);
            calculateBookingDate(view1);
        }, year, month, day);

        Calendar userShowDateStart = Calendar.getInstance();
        userShowDateStart.add(Calendar.DAY_OF_YEAR, _1_DAY);

        datePickerDialog.getDatePicker().setMinDate(userShowDateStart.getTimeInMillis() - 1000);
        datePickerDialog.setTitle("");
        datePickerDialog.show();
    }

    /**
     * This method is used to calculate the BOOKING OPENING DATE based on TRAVEL DATE chosen by user. The final result is formatted with RED|GREEN & BOLD text format and shown to user. If eligible, Create reminder option also will be shown to user.
     */
    public void calculateBookingDate(View view) {
        if (inputDay > 0) {
            boolean bookingStarted;
            String fullText;

            // Calculate booking Date from User selected travel date
            Calendar bookingDate = Calendar.getInstance();
            bookingDate.set(inputYear, inputMonth, inputDay);
            bookingDate.add(Calendar.DAY_OF_YEAR, MINUS_120_DAYS);

            // Make up final text to be shown in screen
            String bookingDateText = MONTHS[bookingDate.get(Calendar.MONTH)] + " " + bookingDate.get(Calendar.DAY_OF_MONTH) + ", " + bookingDate.get(Calendar.YEAR) + " (" + DAYS[bookingDate.get(Calendar.DAY_OF_WEEK) - 1] + ") ";
            if (bookingDate.getTime().after(Calendar.getInstance().getTime())) {
                bookingStarted = false;
                fullText = BOOKING_WILL_START + "\n" + bookingDateText + "\n" + AT + BOOKING_OPENING_TIME + IST;
                // Set Visibility of Create Event Button True
                Button createEvent = findViewById(R.id.bc_create120DayReminderEvent);
                createEvent.setVisibility(View.VISIBLE);
            } else {
                bookingStarted = true;
                fullText = BOOKING_STARTED + "\n" + bookingDateText + "\n" + AT + BOOKING_OPENING_TIME + IST;
            }

            // Format final text
            SpannableString finalResultText = new SpannableString(fullText);
            final int startIndex = fullText.indexOf(bookingDateText);
            final int endIndex = fullText.indexOf(bookingDateText) + bookingDateText.length();

            finalResultText.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (bookingStarted) {
                finalResultText.setSpan(new ForegroundColorSpan(Color.RED), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                finalResultText.setSpan(new RelativeSizeSpan(1.4f), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                finalResultText.setSpan(new ForegroundColorSpan(0xFF388C16), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                finalResultText.setSpan(new RelativeSizeSpan(1.4f), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            TextView result = findViewById(R.id.bc_result_textview);
            result.setBackgroundResource(R.drawable.result_textview);
            result.setText(finalResultText);
        } else {
            Toast.makeText(this, DATE_WARNING, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method is used to create a reminder by redirecting the user to 120_DAY_REMINDER page.
     */
    public void createEvent(View view) {
        Intent intent = new Intent(BookingDayCalculatorActivity.this, AdvanceBookingReminderActivity.class);
        intent.putExtra(LABEL_INPUT_YEAR, inputYear);
        intent.putExtra(LABEL_INPUT_MONTH, inputMonth);
        intent.putExtra(LABEL_INPUT_DAY, inputDay);
        startActivity(intent);
    }
}
