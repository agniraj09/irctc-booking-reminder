package com.arc.agni.irctcbookingreminder.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.arc.agni.irctcbookingreminder.R;
import com.arc.agni.irctcbookingreminder.adapters.EventAdapter;
import com.arc.agni.irctcbookingreminder.bean.Event;
import com.arc.agni.irctcbookingreminder.notification.ReminderBroadcast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.arc.agni.irctcbookingreminder.constants.Constants.ALL;
import static com.arc.agni.irctcbookingreminder.constants.Constants.CALENDAR_ACCOUNT_NAME;
import static com.arc.agni.irctcbookingreminder.constants.Constants.CALENDAR_PERMISSION_WARNING;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE_120_DAY;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE_CUSTOM;
import static com.arc.agni.irctcbookingreminder.constants.Constants.REMINDER_TYPE_TATKAL;
import static com.arc.agni.irctcbookingreminder.constants.Constants.SORT_BY_TRAVEL_DATE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TITLE_VIEW_REMINDER;
import static com.arc.agni.irctcbookingreminder.constants.Constants._120_DAYS;
import static com.arc.agni.irctcbookingreminder.constants.Constants._1_DAY;

public class ViewRemindersActivity extends AppCompatActivity /*implements AdapterView.OnItemSelectedListener*/ {

    Context context;
    Event[] eventListArray;
    static ArrayList<Event> eventList = new ArrayList<>();
    static ArrayList<Event> eventListBackup;
    public static EventAdapter eventAdapter;
    public RecyclerView recyclerView;
    public static int sortByParamIndicator;
    static boolean altClickTravel = true;
    static boolean altClickRem = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reminders);
        setTitle(TITLE_VIEW_REMINDER);

        // Request for ad
        AdView mAdView1 = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest);

        final Button showPopupMenu = findViewById(R.id.show);
        context = this;

        eventList = getEventList();
        eventListBackup = eventList;
        if (eventList != null && eventList.size() > 0) {

            showPopupMenu.setOnClickListener(v -> {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(ViewRemindersActivity.this, showPopupMenu);
                popup.getMenu().add(ALL);
                popup.getMenu().add(REMINDER_TYPE_120_DAY);
                popup.getMenu().add(REMINDER_TYPE_TATKAL);
                popup.getMenu().add(REMINDER_TYPE_CUSTOM);

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(item -> {
                    ArrayList<Event> eventsList = new ArrayList<>();

                    String type = item.getTitle().toString();
                    switch (type) {
                        case ALL: {
                            eventList = eventListBackup;
                            eventAdapter.refreshEventList(eventList);
                            break;
                        }
                        case REMINDER_TYPE_120_DAY: {
                            eventList = eventListBackup;
                            for (Event e : eventList) {
                                if (e.getEventType().equalsIgnoreCase(REMINDER_TYPE_120_DAY)) {
                                    eventsList.add(e);
                                }
                            }
                            eventList = eventsList;
                            eventAdapter.refreshEventList(eventsList);
                            break;
                        }
                        case REMINDER_TYPE_TATKAL: {
                            eventList = eventListBackup;
                            for (Event e : eventList) {
                                if (e.getEventType().equalsIgnoreCase(REMINDER_TYPE_TATKAL)) {
                                    eventsList.add(e);
                                }
                            }
                            eventList = eventsList;
                            eventAdapter.refreshEventList(eventsList);
                            break;
                        }
                        case REMINDER_TYPE_CUSTOM: {
                            eventList = eventListBackup;
                            for (Event e : eventList) {
                                if (e.getEventType().equalsIgnoreCase(REMINDER_TYPE_CUSTOM)) {
                                    eventsList.add(e);
                                }
                            }
                            eventList = eventsList;
                            eventAdapter.refreshEventList(eventsList);
                            break;
                        }
                    }


                    return true;
                });

                popup.show();//showing popup menu
            });

            eventListArray = eventList.toArray(new Event[eventList.size()]);
            eventAdapter = new EventAdapter(this, eventListArray);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView = findViewById(R.id.recycle);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(eventAdapter);
        } else {
            // If the reminder list is empty, set the content view to "NO_REMINDERS_SET.xml"
            setContentView(R.layout.no_reminders_set);
            AdView mAdView2 = findViewById(R.id.adView2);
            mAdView2.loadAd(adRequest);
        }

    }

    public ArrayList<Event> getEventList() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            Toast toast = Toast.makeText(this, CALENDAR_PERMISSION_WARNING, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        ArrayList<Event> eventList = new ArrayList<>();
        String[] mProjection =
                {
                        CalendarContract.Events._ID,
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.DESCRIPTION,
                        CalendarContract.Events.DTSTART,
                        CalendarContract.Events.DTEND,
                        CalendarContract.Events.EXDATE,
                };

        Uri uri = CalendarContract.Events.CONTENT_URI;
        String selection = CalendarContract.Calendars.ACCOUNT_NAME + " = ? ";
        String[] selectionArgs = new String[]{CALENDAR_ACCOUNT_NAME};

        Cursor cursor = getContentResolver().query(uri, mProjection, selection, selectionArgs, null);

        while (Objects.requireNonNull(cursor).moveToNext()) {
            Event event = new Event();
            event.setEventID(cursor.getString(cursor.getColumnIndex(CalendarContract.Events._ID)));
            event.setEventTitle(cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE)));
            event.setEventType(cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION)));
            event.setReminderDate(cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DTSTART)));

            Calendar travelDate = Calendar.getInstance();
            travelDate.setTimeInMillis(Long.parseLong(event.getReminderDate()));

            switch (event.getEventType()) {
                case REMINDER_TYPE_120_DAY: {
                    travelDate.add(Calendar.DAY_OF_YEAR, _120_DAYS);
                    break;
                }

                case REMINDER_TYPE_TATKAL: {
                    travelDate.add(Calendar.DAY_OF_YEAR, _1_DAY);
                    break;
                }

                case REMINDER_TYPE_CUSTOM: {
                    String customTravelDate = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.EXDATE));
                    travelDate.setTimeInMillis(Long.parseLong(customTravelDate));
                    break;
                }
            }
            event.setTravelDate(String.valueOf(travelDate.getTimeInMillis()));
            eventList.add(event);
        }
        return eventList;
    }

    public void deleteEvent(String eventID, Context context) {
        String[] selArgs = new String[]{eventID};
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        context.getContentResolver().
                delete(
                        CalendarContract.Events.CONTENT_URI,
                        CalendarContract.Events._ID + " =? ",
                        selArgs);

        // Delete the corresponding notification set
        ReminderBroadcast.cancelNotification(Integer.parseInt(eventID), context);

        ArrayList<Event> events = ViewRemindersActivity.eventList;
        for (Event event : events) {
            if (event.getEventID().equalsIgnoreCase(eventID)) {
                eventList.remove(event);
                eventListBackup.remove(event);
                eventAdapter.refreshEventList(eventList);
                break;
            }
        }

    }

    public void sortByTravelDate(View view) {
        ArrayList<Event> eventsList = eventList;
        Comparator<Event> travelDateComparator = (eventOne, eventTwo) -> eventOne.getTravelDate().compareTo(eventTwo.getTravelDate());
        if (eventsList != null) {
            sortByParamIndicator = SORT_BY_TRAVEL_DATE;
            if (altClickTravel) {
                Collections.sort(eventsList, travelDateComparator);
            } else {
                Collections.sort(eventsList, travelDateComparator);
                Collections.reverse(eventsList);
            }
            altClickTravel = !altClickTravel;
            eventAdapter.refreshEventList(eventsList);
        }
    }

    public void sortByReminderDate(View view) {
        ArrayList<Event> eventsList = eventList;
        Comparator<Event> reminderDateComparator = (eventOne, eventTwo) -> eventOne.getReminderDate().compareTo(eventTwo.getReminderDate());
        if (eventsList != null) {
            if (altClickRem) {
                Collections.sort(eventsList, reminderDateComparator);
            } else {
                Collections.sort(eventsList, reminderDateComparator);
                Collections.reverse(eventsList);
            }
            altClickRem = !altClickRem;
            eventAdapter.refreshEventList(eventsList);
        }
    }
}
