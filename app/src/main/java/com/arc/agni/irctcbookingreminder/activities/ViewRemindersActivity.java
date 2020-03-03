package com.arc.agni.irctcbookingreminder.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;


import com.arc.agni.irctcbookingreminder.R;
import com.arc.agni.irctcbookingreminder.adapters.EventAdapter;
import com.arc.agni.irctcbookingreminder.bean.Event;
import com.arc.agni.irctcbookingreminder.utils.CalendarUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewRemindersActivity extends AppCompatActivity /*implements AdapterView.OnItemSelectedListener*/ {

    Context context;
    Event[] eventListArray;
    static ArrayList<Event> eventList;
    static ArrayList<Event> eventListBackup;
    public static EventAdapter eventAdapter;
    public RecyclerView recyclerView;
    public static int sortByParamIndicator;
    static boolean altClickTravel = true;
    static boolean altClickRem = true;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reminders);
        setTitle("View Reminders");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final Button showPopupMenu = findViewById(R.id.show);
        LinearLayout sortLayout = findViewById(R.id.sortMenu);
        View horizontalLine = findViewById(R.id.horiontalline);
        context = this;

        eventList = getEventList();
        eventListBackup = eventList;
        if (eventList != null && eventList.size() > 0) {

            showPopupMenu.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //Creating the instance of PopupMenu
                    PopupMenu popup = new PopupMenu(ViewRemindersActivity.this, showPopupMenu);
                    popup.getMenu().add("All");
                    popup.getMenu().add("120 Day Reminders");
                    popup.getMenu().add("Tatkal Reminders");
                    popup.getMenu().add("Custom Reminders");

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            ArrayList<Event> eventsList = new ArrayList<>();

                            String type = item.getTitle().toString();
                            switch (type) {
                                case "All": {
                                    eventList = eventListBackup;
                                    eventAdapter.refreshEventList(eventList);
                                    break;
                                }
                                case "120 Day Reminders": {
                                    eventList = eventListBackup;
                                    for (Event e : eventList) {
                                        if (e.getEventType().equalsIgnoreCase("120 Day Reminder")) {
                                            eventsList.add(e);
                                        }
                                    }
                                    eventList = eventsList;
                                    eventAdapter.refreshEventList(eventsList);
                                    break;
                                }
                                case "Tatkal Reminders": {
                                    eventList = eventListBackup;
                                    for (Event e : eventList) {
                                        if (e.getEventType().equalsIgnoreCase("Tatkal Reminder")) {
                                            eventsList.add(e);
                                        }
                                    }
                                    eventList = eventsList;
                                    eventAdapter.refreshEventList(eventsList);
                                    break;
                                }
                                case "Custom Reminders": {
                                    eventList = eventListBackup;
                                    for (Event e : eventList) {
                                        if (e.getEventType().equalsIgnoreCase("Custom Reminder")) {
                                            eventsList.add(e);
                                        }
                                    }
                                    eventList = eventsList;
                                    eventAdapter.refreshEventList(eventsList);
                                    break;
                                }
                            }


                            return true;
                        }
                    });

                    popup.show();//showing popup menu
                }
            });

            eventListArray = eventList.toArray(new Event[eventList.size()]);
            eventAdapter = new EventAdapter(this, eventListArray);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView = findViewById(R.id.recycle);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(eventAdapter);
        }else {
            horizontalLine.setVisibility(View.INVISIBLE);
            sortLayout.setVisibility(View.INVISIBLE);
            Toast toast = Toast.makeText(this, "No Events Created", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    public ArrayList<Event> getEventList() {
        ArrayList<Event> eventList = new ArrayList<Event>();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
        }
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
        String[] selectionArgs = new String[]{CalendarUtil.MY_ACCOUNT_NAME};

        Cursor cursor = getContentResolver().query(uri, mProjection, selection, selectionArgs, null);

        while (cursor.moveToNext()) {
            Event event = new Event();
            event.setEventID(cursor.getString(cursor.getColumnIndex(CalendarContract.Events._ID)));
            event.setEventTitle(cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE)));
            event.setEventType(cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION)));
            event.setReminderDate(cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DTSTART)));

            Calendar travelDate = Calendar.getInstance();
            travelDate.setTimeInMillis(Long.parseLong(event.getReminderDate()));

            switch (event.getEventType()) {
                case "120 Day Reminder": {
                    travelDate.add(Calendar.DAY_OF_YEAR, 120);
                    break;
                }

                case "Tatkal Reminder": {
                    travelDate.add(Calendar.DAY_OF_YEAR, 1);
                    break;
                }

                case "Custom Reminder": {
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
        if (eventsList != null) {
            sortByParamIndicator = 2;
            if (altClickTravel) {
                Collections.sort(eventsList, new Event());
            } else {
                Collections.sort(eventsList, new Event());
                Collections.reverse(eventsList);
            }
            altClickTravel = !altClickTravel;
            eventAdapter.refreshEventList(eventsList);
        }
    }

    public void sortByReminderDate(View view) {
        ArrayList<Event> eventsList = eventList;
        if (eventsList != null) {
            if (altClickRem) {
                Collections.sort(eventsList, new Event());
            } else {
                Collections.sort(eventsList, new Event());
                Collections.reverse(eventsList);
            }
            altClickRem = !altClickRem;
            eventAdapter.refreshEventList(eventsList);
        }
    }
}
