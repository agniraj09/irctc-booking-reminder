package com.arc.agni.irctcbookingreminder.activities;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
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

public class BookingDayCalculatorActivity extends AppCompatActivity {
    public static String ADMOB_APP_ID = "ca-app-pub-4587610802196055~4797049191";
    private AdView mAdView;

    EditText travel_date;
    static int input_date, input_month, input_year;
    int dateX, monthX, yearX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_calulator);
        setTitle("Booking Day Calculator");

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
        userShowDateStart.add(Calendar.DAY_OF_YEAR, 1);

        datePickerDialog.getDatePicker().setMinDate(userShowDateStart.getTimeInMillis() - 1000);
        datePickerDialog.setTitle("");
        datePickerDialog.show();
    }

    public void calculateBookingDate(View view) {
        if (input_date > 0) {
            boolean bookingStarted;
            String fullText;
            String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

            // Calculate booking Date from User selected travel date
            Calendar bookingDate = Calendar.getInstance();
            bookingDate.set(input_year, input_month, input_date);
            bookingDate.add(Calendar.DAY_OF_YEAR, -120);

            // Make up final text to be shown in screen
            String bookingDateText = months[bookingDate.get(Calendar.MONTH)] + " " + bookingDate.get(Calendar.DAY_OF_MONTH) + ", " + bookingDate.get(Calendar.YEAR) + " (" + days[bookingDate.get(Calendar.DAY_OF_WEEK) - 1] + ") ";
            String bookingTimeText = "8.00 a.m.";
            if (bookingDate.getTime().after(Calendar.getInstance().getTime())) {
                bookingStarted = false;
                fullText = "Booking will start on\n" + bookingDateText + "\nat " + bookingTimeText;
            } else {
                bookingStarted = true;
                fullText = "Booking has already started on\n" + bookingDateText + "\nat " + bookingTimeText + " IST";
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
            Toast.makeText(this, "Please Select Valid Date", Toast.LENGTH_SHORT).show();
        }
    }
}
