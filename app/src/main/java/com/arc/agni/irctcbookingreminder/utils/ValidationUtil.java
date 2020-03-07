package com.arc.agni.irctcbookingreminder.utils;

public class ValidationUtil {

    /**
     * This method validates the ReminderTitle & TravelDate inputted by user.
     */
    public static boolean titleAndDateValidation(String event_title, int date) {
        return event_title != null && !event_title.isEmpty() && date > 0;
    }
}
