package com.arc.agni.irctcbookingreminder.constants;

import java.util.Calendar;

public class Constants {

    // Activity Titles
    public static final String TITLE_HOLIDAY_LIST = "Holiday List " + Calendar.getInstance().get(Calendar.YEAR);
    public static final String TITLE_BOOKING_DAY_CALCULATOR = "Booking Day Calculator";
    public static final String TITLE_120_DAY_REMINDER = "120 Day Reminder";
    public static final String TITLE_TATKAL_REMINDER = "Tatkal Reminder";
    public static final String TITLE_CUSTOM_REMINDER = "Custom Reminder";
    public static final String TITLE_VIEW_REMINDER = "View Reminders";
    public static final String TITLE_VIEW_SET_REMINDER = "Reminder";

    // Reminder Types
    public static final String REMINDER_TYPE_120_DAY = "120 Day Reminder";
    public static final String REMINDER_TYPE_TATKAL = "Tatkal Reminder";
    public static final String REMINDER_TYPE_TATKAL_AC = "Tatkal Reminder A.C.";
    public static final String REMINDER_TYPE_TATKAL_NON_AC = "Tatkal Reminder Non A.C.";
    public static final String REMINDER_TYPE_CUSTOM = "Custom Reminder";

    // Intent Constants
    public static final String EVENT_TITILE = "eventTitle";
    public static final String REMINDER_TYPE = "reminderType";
    public static final String TRAVEL_DATE = "travelDate";
    public static final String REMINDER_DATE = "reminderDate";
    public static final String REMINDER_TIME = "reminderTime";
    public static final String SCOPE = "scope";

    // Scopes
    public static final String SCOPE_NO_TOAST = "noToast";

    //Activity Indicator
    public static final int IND_120_DAY_REMINDER = 1;
    public static final int IND_TATKAL_REMINDER = 2;
    public static final int IND_CUSTOM_REMINDER = 3;
    public static final int IND_VIEW_REMINDERS = 4;

    // Sort Type Flags
    public static final int SORT_BY_REMINDER_DATE = 1;
    public static final int SORT_BY_TRAVEL_DATE = 2;

    // Reminder Time Constants
    public static final String LABEL_INPUT_DAY = "input_date";
    public static final String LABEL_INPUT_MONTH = "input_month";
    public static final String LABEL_INPUT_YEAR = "input_year";

    // Reminder Time Values
    public static final int _120_DAY_BOOKING_REMINDER_HOUR = 8;
    public static final int _120_DAY_BOOKING_REMINDER_MINUTE = 0;
    public static final int TATKAL_BOOKING_AC_REMINDER_HOUR = 10;
    public static final int TATKAL_BOOKING__AC_REMINDER_MINUTE = 0;
    public static final int TATKAL_BOOKING_NON_AC_REMINDER_HOUR = 11;
    public static final int TATKAL_BOOKING__NON_AC_REMINDER_MINUTE = 0;
    public static final int CUSTOM_BOOKING_REMINDER_HOUR = 8;
    public static final int CUSTOM_BOOKING_REMINDER_MINUTE = 0;
    public static final int REMINDER_DURATION = 30;

    // Warning Messages
    public static final String TITLE_AND_DATE_WARNING = "Please Enter Valid Title and Date";
    public static final String DATE_WARNING = "Please Select Valid Date";
    public static final String TRAVEL_DATE_WARNING = "Please Select Travel Date First";
    public static final String COACH_PREFERENCE_WARNING = "Please Select Coach Preference";
    public static final String CALENDAR_PERMISSION_WARNING = "Calendar Permission needed to proceed further";
    public static final String INTERNET_PERMISSION_WARNING = "Internet & Network Access is required to view the list of holidays";
    public static final String EXIT_WARNING = "Are you sure you want to exit?";
    public static final String DELETE_WARNING = "Are you sure you want to delete the reminder ?";
    public static final String NEED_INTERNET_CONNECTION = "Internet connection is required to view the list of holidays";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong, Please try again later!";

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
    public static final String AC_COACH = "A.C. Coach";
    public static final String TATKAL_OPENING_TIME_AC = "10.00 a.m.";
    public static final String NON_AC_COACH = "Non A.C. Coach";
    public static final String TATKAL_OPENING_TIME_NON_AC = "11.00 a.m.";
    public static final String BOOKING_WILL_START = "Booking will start on";
    public static final String BOOKING_STARTED = "Booking has already started on";
    public static final String DELETE_EVENT = "Delete Reminder";
    public static final String DELETE_OPTION = "Delete";
    public static final String CANCEL = "Cancel";
    public static final String HOLIDAY_LIST_TITLE = "Holiday List - " + Calendar.getInstance().get(Calendar.YEAR);

    // Admob Constants
    public static final String ADMOB_APP_ID = "ca-app-pub-3940256099942544~3347511713";
    public static final String TEST_DEVICE_ID = "0EC56B91253E874AAF286CEDC3945F6A";

    // Calendar Constants
    public static final int PERMISSIONS_REQUEST_READ_CALENDAR = 1;
    public static final int PERMISSIONS_REQUEST_WRITE_CALENDAR = 2;
    public static final int PERMISSIONS_REQUEST_INTERNET = 3;
    public static final int PERMISSIONS_REQUEST_NETWORK = 4;
    public static final String CALENDAR_ACCOUNT_NAME = "IRCTC Booking Reminder";
    public static final String CALENDAR_NAME = "IRCTC Reminder Calendar";
    public static final int CALENDAR_COLOR_LOCAL = 0xffff0000;
    public static final String OWNER_ACCOUNT_ID = "";

    // Push Notification Constants
    public static final String CHANNEL_ID = "IRCTCBookingReminder";
    public static final String CHANNEL_NAME = "IRCTC Booking Reminder Channel";
    public static final String CHANNEL_DESCRIPTION = "This is a channel to shoot notifications for IRCTC Booking Reminder App";
    public static final String NOTIFICATION_TITLE = "IRCTC Booking Reminder";
    public static final String NOTIFICATION_TEXT = "It's a booking day, Gear Up !";
    public static final String INTENT_EXTRA_NOTIFICATION = "notification";
    public static final String INTENT_EXTRA_NOTIFICATION_ID = "notification_id";

    // Network Constants
    public static final String NETWORK_TEST_URL = "http://clients3.google.com/generate_204";
    public static final String USER_AGENT_KEY = "User-Agent";
    public static final String USER_AGENT_VALUE = "Android";
    public static final String CONNECTION_KEY = "Connection";
    public static final String CONNECTION_VALUE = "close";
    public static final int CONNECTION_TIMEOUT = 2000;
    public static final int STATUS_CODE = 204;
    public static final int CONTENT_LENGTH = 0;

    // Holiday List Constants
    public static final String HOLIDAY_LIST_URL = "https://clients6.google.com/calendar/v3/calendars/{calendar}/events?calendarId={calendarId}&&singleEvents=true&timeZone={timeZone}&&maxAttendees=1&maxResults=250&sanitizeHtml=true&timeMin={timeMin}&timeMax={timeMax}&key=AIzaSyBNlYH01_9Hc5S1J9vuFmu2nUqBZJNAXxs";
    public static final String GOOGLE_CALENDAR_NAME_KEY = "{calendar}";
    public static final String GOOGLE_CALENDAR_NAME_VALUE = "en.indian%23holiday@group.v.calendar.google.com";
    public static final String GOOGLE_CALENDAR_ID_KEY = "{calendarId}";
    public static final String GOOGLE_CALENDAR_ID_VALUE = "en.indian%23holiday%40group.v.calendar.google.com";
    public static final String TIMEZONE_KEY = "{timeZone}";
    public static final String TIMEZONE_VALUE = "Asia%2FKolkata";
    public static final String TIME_MIN_KEY = "{timeMin}";
    public static final String TIME_MIN_VALUE = Calendar.getInstance().get(Calendar.YEAR) + "-01-01T00%3A00%3A00%2B05%3A30";
    public static final String TIME_MAX_KEY = "{timeMax}";
    public static final String TIME_MAX_VALUE = Calendar.getInstance().get(Calendar.YEAR) + "-12-31T00%3A00%3A00%2B05%3A30";
    public static final String MONTH_LABEL = "monthname";

}
