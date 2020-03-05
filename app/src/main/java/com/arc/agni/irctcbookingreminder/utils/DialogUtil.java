package com.arc.agni.irctcbookingreminder.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.arc.agni.irctcbookingreminder.R;
import com.arc.agni.irctcbookingreminder.activities.AdvanceBookingReminderActivity;
import com.arc.agni.irctcbookingreminder.activities.BookingDayCalculatorActivity;
import com.arc.agni.irctcbookingreminder.activities.CustomReminderActivity;
import com.arc.agni.irctcbookingreminder.activities.HomeScreenActivity;
import com.arc.agni.irctcbookingreminder.activities.TatkalReminderActivity;
import com.arc.agni.irctcbookingreminder.activities.ViewRemindersActivity;

public class DialogUtil {

    Context context;

    public DialogUtil(Context context) {
        this.context = context;
    }

    ViewRemindersActivity viewRemindersActivity = new ViewRemindersActivity();

    public void showDeleteEventDialog(final Context context, final String eventID) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Delete Event");
        alertDialogBuilder
                .setMessage("Are you sure you want to delete the event ?")
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        viewRemindersActivity.deleteEvent(eventID, context);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public static void showDialogPostEventCreation(final Context context, final int activityIndicator) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_success_description);

        // Create Another Reminder
        Button create_another = dialog.findViewById(R.id.create_another);
        create_another.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                CustomReminderActivity.isTravelDateSelected = false;
                dialog.dismiss();
                ((Activity) context).finish();
                ((Activity) context).overridePendingTransition(0, 0);

                if (activityIndicator == 1) {
                    intent = new Intent(context, AdvanceBookingReminderActivity.class);
                } else if (activityIndicator == 2) {
                    intent = new Intent(context, TatkalReminderActivity.class);
                } else if (activityIndicator == 3) {
                    intent = new Intent(context, CustomReminderActivity.class);
                }

                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(0, 0);
            }
        });

        //Go to Home Page
        Button goto_home = dialog.findViewById(R.id.goto_home);
        goto_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomReminderActivity.isTravelDateSelected = false;
                dialog.dismiss();
                ((Activity) context).finish();
                Intent intent = new Intent(context, HomeScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(intent);
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void showDescriptionDialog(Context context, int indicator) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        switch (indicator) {
            case 1: {
                dialog.setContentView(R.layout.dialog_advancebooking_description);
                break;
            }
            case 2: {
                dialog.setContentView(R.layout.dialog_tatkalbooking_description);
                break;
            }
            case 3: {
                dialog.setContentView(R.layout.dialog_custombooking_description);
                break;
            }
            case 4:
            {
                dialog.setContentView(R.layout.dialog_viewreminders_description);
                break;
            }
        }

        Button okaybutton = dialog.findViewById(R.id.ok_button);
        okaybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
