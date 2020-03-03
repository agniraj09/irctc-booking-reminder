package com.arc.agni.irctcbookingreminder.utils;

public class ValidationUtil {

    public boolean advanceBookingValidation(String event_title, int date) {

        System.out.println(date);
        if (event_title != null && date > 0) {
            event_title = event_title.replaceAll(" ", "");
            if (event_title.length() != 0) {
                return true;
            }
        }
        return false;
    }
}
