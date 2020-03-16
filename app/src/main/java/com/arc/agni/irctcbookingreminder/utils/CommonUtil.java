package com.arc.agni.irctcbookingreminder.utils;

import android.content.Context;
import android.content.Intent;

import com.arc.agni.irctcbookingreminder.activities.ViewSetReminderActivity;

import java.util.Calendar;

import static com.arc.agni.irctcbookingreminder.constants.Constants.DAYS;
import static com.arc.agni.irctcbookingreminder.constants.Constants.EVENT_TITILE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.MONTHS;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_DATE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TIME;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE_TATKAL;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TRAVEL_DATE;

public class CommonUtil {

    //static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public static <T> Intent createIntentPostReminderCreation(Context context, String reminderTitle, String reminderType, int travelDay, int travelMonth, int travelYear, Calendar reminderDateAndTime) {
        Intent intent = new Intent(context, ViewSetReminderActivity.class);
        intent.putExtra(EVENT_TITILE, reminderTitle);
        intent.putExtra(REMINDER_TYPE, reminderType);
        intent.putExtra(TRAVEL_DATE, formatDateToFullText(travelDay, travelMonth, travelYear));
        intent.putExtra(REMINDER_DATE, formatCalendarDateToFullText(reminderDateAndTime));
        String reminderTime = REMINDER_TYPE_TATKAL.equalsIgnoreCase(reminderType) ? "7.30 a.m. & 10.30 a.m." : ((reminderDateAndTime.get(Calendar.HOUR_OF_DAY) - 1) + ".30 a.m.");
        intent.putExtra(REMINDER_TIME, reminderTime);

        return intent;
    }

    public static String formatDateToFullText(int day, int month, int year) {
        Calendar date = Calendar.getInstance();
        date.set(year, month, day);
        return formatCalendarDateToFullText(date);

    }

    public static String formatCalendarDateToFullText(Calendar date) {
        return MONTHS[date.get(Calendar.MONTH)] + " " + date.get(Calendar.DAY_OF_MONTH) + ", " + date.get(Calendar.YEAR) + " (" + DAYS[date.get(Calendar.DAY_OF_WEEK) - 1] + ") ";

    }
}
