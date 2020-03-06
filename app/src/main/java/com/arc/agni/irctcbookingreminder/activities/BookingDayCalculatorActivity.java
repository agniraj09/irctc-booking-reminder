package com.arc.agni.irctcbookingreminder.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arc.agni.irctcbookingreminder.R;
import com.arc.agni.irctcbookingreminder.utils.DialogUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

import static com.arc.agni.irctcbookingreminder.constants.Constants.*;

public class BookingDayCalculatorActivity extends AppCompatActivity {

    private AdView mAdView;
    EditText travel_date;
    static int input_date, input_month, input_year;
    int dateX, monthX, yearX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_calulator);
        setTitle(TITLE_BOOKING_DAY_CALCULATOR);

        travel_date = findViewById(R.id.bc_traveldate);

        MobileAds.initialize(this, ADMOB_APP_ID);
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
                calculateBookingDate(view);
            }
        }, yearX, monthX, dateX);

        Calendar userShowDateStart = Calendar.getInstance();
        userShowDateStart.add(Calendar.DAY_OF_YEAR, _1_DAY);

        datePickerDialog.getDatePicker().setMinDate(userShowDateStart.getTimeInMillis() - 1000);
        datePickerDialog.setTitle("");
        datePickerDialog.show();
    }

    public void calculateBookingDate(View view) {
        if (input_date > 0) {
            boolean bookingStarted;
            String fullText;

            // Calculate booking Date from User selected travel date
            Calendar bookingDate = Calendar.getInstance();
            bookingDate.set(input_year, input_month, input_date);
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
            ForegroundColorSpan red = new ForegroundColorSpan(Color.RED);
            ForegroundColorSpan green = new ForegroundColorSpan(0xFF388C16);
            RelativeSizeSpan textSize = new RelativeSizeSpan(1.4f);
            SpannableString finalResultText = new SpannableString(fullText);
            finalResultText.setSpan(new StyleSpan(Typeface.BOLD), fullText.indexOf(bookingDateText), fullText.indexOf(bookingDateText) + bookingDateText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (bookingStarted) {
                finalResultText.setSpan(red, fullText.indexOf(bookingDateText), fullText.indexOf(bookingDateText) + bookingDateText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                finalResultText.setSpan(textSize, fullText.indexOf(bookingDateText), fullText.indexOf(bookingDateText) + bookingDateText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                finalResultText.setSpan(green, fullText.indexOf(bookingDateText), fullText.indexOf(bookingDateText) + bookingDateText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                finalResultText.setSpan(textSize, fullText.indexOf(bookingDateText), fullText.indexOf(bookingDateText) + bookingDateText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            TextView result = findViewById(R.id.bc_result_textview);
            result.setBackgroundResource(R.drawable.result_textview);
            result.setText(finalResultText);
        } else {
            Toast.makeText(this, DATE_WARNING, Toast.LENGTH_SHORT).show();
        }
    }

    public void createEvent(View view) {
        Intent intent = new Intent(BookingDayCalculatorActivity.this, AdvanceBookingReminderActivity.class);
        intent.putExtra(LABEL_INPUT_YEAR, input_year);
        intent.putExtra(LABEL_INPUT_MONTH, input_month);
        intent.putExtra(LABEL_INPUT_DATE, input_date);
        startActivity(intent);
    }
}
