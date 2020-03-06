package com.arc.agni.irctcbookingreminder.utils;

public class ValidationUtil {

    public boolean advanceBookingValidation(String event_title, int date) {

        if (event_title != null && !event_title.isEmpty() && date > 0) {
            return true;
        }
        return false;
    }
}
