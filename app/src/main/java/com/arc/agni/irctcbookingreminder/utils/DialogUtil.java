package com.arc.agni.irctcbookingreminder.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arc.agni.irctcbookingreminder.R;
import com.arc.agni.irctcbookingreminder.activities.AdvanceBookingReminderActivity;
import com.arc.agni.irctcbookingreminder.activities.CustomReminderActivity;
import com.arc.agni.irctcbookingreminder.activities.HomeScreenActivity;
import com.arc.agni.irctcbookingreminder.activities.TatkalReminderActivity;
import com.arc.agni.irctcbookingreminder.activities.ViewRemindersActivity;

import java.util.Objects;

import static com.arc.agni.irctcbookingreminder.constants.Constants.CANCEL;
import static com.arc.agni.irctcbookingreminder.constants.Constants.DELETE_EVENT;
import static com.arc.agni.irctcbookingreminder.constants.Constants.DELETE_OPTION;
import static com.arc.agni.irctcbookingreminder.constants.Constants.DELETE_WARNING;
import static com.arc.agni.irctcbookingreminder.constants.Constants.IND_120_DAY_REMINDER;
import static com.arc.agni.irctcbookingreminder.constants.Constants.IND_CUSTOM_REMINDER;
import static com.arc.agni.irctcbookingreminder.constants.Constants.IND_TATKAL_REMINDER;
import static com.arc.agni.irctcbookingreminder.constants.Constants.IND_VIEW_REMINDERS;

public class DialogUtil {

    public void showDeleteEventDialog(final Context context, final String eventID) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(DELETE_EVENT);
        alertDialogBuilder
                .setMessage(DELETE_WARNING)
                .setCancelable(false)
                .setPositiveButton(DELETE_OPTION, (dialog, id) -> new ViewRemindersActivity().deleteEvent(eventID, context))
                .setNegativeButton(CANCEL, (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public static void showDialogPostEventCreation(final Context context, final int activityIndicator) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_success_description);

        // Create Another Reminder
        Button create_another = dialog.findViewById(R.id.create_another);
        create_another.setOnClickListener(v -> {
            Intent intent = null;
            CustomReminderActivity.isTravelDateSelected = false;
            dialog.dismiss();
            ((Activity) context).finish();
            ((Activity) context).overridePendingTransition(0, 0);

            if (activityIndicator == IND_120_DAY_REMINDER) {
                intent = new Intent(context, AdvanceBookingReminderActivity.class);
            } else if (activityIndicator == IND_TATKAL_REMINDER) {
                intent = new Intent(context, TatkalReminderActivity.class);
            } else if (activityIndicator == IND_CUSTOM_REMINDER) {
                intent = new Intent(context, CustomReminderActivity.class);
            }

            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(0, 0);
        });

        //Go to Home Page
        Button goto_home = dialog.findViewById(R.id.goto_home);
        goto_home.setOnClickListener(v -> {
            CustomReminderActivity.isTravelDateSelected = false;
            dialog.dismiss();
            ((Activity) context).finish();
            Intent intent = new Intent(context, HomeScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            context.startActivity(intent);
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void showDescriptionDialog(Context context, int indicator) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        switch (indicator) {
            case IND_120_DAY_REMINDER: {
                dialog.setContentView(R.layout.dialog_advancebooking_description);
                break;
            }
            case IND_TATKAL_REMINDER: {
                dialog.setContentView(R.layout.dialog_tatkalbooking_description);
                break;
            }
            case IND_CUSTOM_REMINDER: {
                dialog.setContentView(R.layout.dialog_custombooking_description);
                break;
            }
            case IND_VIEW_REMINDERS: {
                dialog.setContentView(R.layout.dialog_viewreminders_description);
                break;
            }
        }

        Button okaybutton = dialog.findViewById(R.id.ok_button);
        okaybutton.setOnClickListener(v -> dialog.dismiss());

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
