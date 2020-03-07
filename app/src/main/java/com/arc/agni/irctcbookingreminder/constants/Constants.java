package com.arc.agni.irctcbookingreminder.constants;

public class Constants {

    // Activity Titles
    public static final String TITLE_BOOKING_DAY_CALCULATOR = "Booking Day Calculator";
    public static final String TITLE_120_DAY_REMINDER = "120 Day Reminder";
    public static final String TITLE_TATKAL_REMINDER = "Tatkal Reminder";
    public static final String TITLE_CUSTOM_REMINDER = "Custom Reminder";
    public static final String TITLE_VIEW_REMINDER = "View Reminders";

    // Reminder Types
    public static final String REMINDER_TYPE_120_DAY = "120 Day Reminder";
    public static final String REMINDER_TYPE_TATKAL = "Tatkal Reminder";
    public static final String REMINDER_TYPE_CUSTOM = "Custom Reminder";

    //Activity Indicator
    public static final int IND_120_DAY_REMINDER = 1;
    public static final int IND_TATKAL_REMINDER = 2;
    public static final int IND_CUSTOM_REMINDER = 3;
    public static final int IND_VIEW_REMINDERS = 4;

    // Sort Type Flags
    public static final int SORT_BY_REMINDER_DATE = 1;
    public static final int SORT_BY_TRAVEL_DATE = 2;

    // Reminder Time Constants
    public static final String LABEL_INPUT_DATE = "input_date";
    public static final String LABEL_INPUT_MONTH = "input_month";
    public static final String LABEL_INPUT_YEAR = "input_year";

    // Reminder Time Values
    public static final int _120_DAY_BOOKING_REMINDER_HOUR = 7;
    public static final int _120_DAY_BOOKING_REMINDER_MINUTE = 30;
    public static final int TATKAL_BOOKING_AC_REMINDER_HOUR = 9;
    public static final int TATKAL_BOOKING__AC_REMINDER_MINUTE = 30;
    public static final int TATKAL_BOOKING_NON_AC_REMINDER_HOUR = 10;
    public static final int TATKAL_BOOKING__NON_AC_REMINDER_MINUTE = 30;
    public static final int CUSTOM_BOOKING_REMINDER_HOUR = 7;
    public static final int CUSTOM_BOOKING_REMINDER_MINUTE = 30;
    public static final int REMINDER_DURATION = 25;

    // Warning Messages
    public static final String TITLE_AND_DATE_WARNING = "Please Enter Valid Title and Date";
    public static final String DATE_WARNING = "Please Select Valid Date";
    public static final String TRAVEL_DATE_WARNING = "Please Select Travel Date First";
    public static final String COACH_PREFERENCE_WARNING = "Please Select Coach Preference";
    public static final String NO_EVENTS = "No reminders are created yet";
    public static final String CALENDAR_PERMISSION_WARNING = "Calendar Permission needed to proceed further";
    public static final String EXIT_WARNING = "Are you sure you want to exit?";
    public static final String DELETE_WARNING = "Are you sure you want to delete the reminder ?";

    // Day Calculation Constants
    public static final int _1_DAY = 1;
    public static final int MINUS_1_DAY = -1;
    public static final int _2_DAYS = 2;
    public static final int _121_DAYS = 121;
    public static final int _120_DAYS = 120;
    public static final int MINUS_120_DAYS = -120;

    // Universal Constants
    public static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public static final String[] DAYS = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    // Text Constants
    public static final String ALL = "All";
    public static final String AT = "at ";
    public static final String IST = " IST";
    public static final String BOOKING_OPENING_TIME = "8.00 a.m.";
    public static final String BOOKING_WILL_START = "Booking will start on";
    public static final String BOOKING_STARTED = "Booking has already started on";
    public static final String DELETE_EVENT = "Delete Reminder";
    public static final String DELETE_OPTION = "Delete";
    public static final String CANCEL = "Cancel";

    // Admob Constants
    public static String ADMOB_APP_ID = "ca-app-pub-3940256099942544~3347511713";
    public static String TEST_DEVICE_ID = "0EC56B91253E874AAF286CEDC3945F6A";

    // Calendar Constants
    public static final int PERMISSIONS_REQUEST_READ_CALENDAR = 1;
    public static final int PERMISSIONS_REQUEST_WRITE_CALENDAR = 2;
    public static final String CALENDAR_ACCOUNT_NAME = "IRCTC Booking Reminder";
    public static final String CALENDAR_NAME = "IRCTC Reminder Calendar";
    public static final int CALENDAR_COLOR_LOCAL = 0xffff0000;
    public static final String OWNER_ACCOUNT_ID = "";

    // Push Notification Constants
    public static final String CHANNEL_ID = "IRCTCBookingReminder";
    public static final String CHANNEL_NAME = "IRCTC Booking Reminder Channel";
    public static final String CHANNEL_DESCRIPTION = "This is a channel to shoot notifications for IRCTC Booking Reminder App";
    public static final int NOTIFICATION_ID = 978;
}
