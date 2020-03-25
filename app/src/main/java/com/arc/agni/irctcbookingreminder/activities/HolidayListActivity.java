package com.arc.agni.irctcbookingreminder.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.arc.agni.irctcbookingreminder.R;
import com.arc.agni.irctcbookingreminder.adapters.HolidayListAdapter;
import com.arc.agni.irctcbookingreminder.bean.CalendarResponse;
import com.arc.agni.irctcbookingreminder.bean.Items;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.arc.agni.irctcbookingreminder.constants.Constants.CONNECTION_TIMEOUT;
import static com.arc.agni.irctcbookingreminder.constants.Constants.DAYS;
import static com.arc.agni.irctcbookingreminder.constants.Constants.GOOGLE_CALENDAR_ID_KEY;
import static com.arc.agni.irctcbookingreminder.constants.Constants.GOOGLE_CALENDAR_ID_VALUE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.GOOGLE_CALENDAR_NAME_KEY;
import static com.arc.agni.irctcbookingreminder.constants.Constants.GOOGLE_CALENDAR_NAME_VALUE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.HOLIDAY_LIST_URL;
import static com.arc.agni.irctcbookingreminder.constants.Constants.MONTHS;
import static com.arc.agni.irctcbookingreminder.constants.Constants.SOMETHING_WENT_WRONG;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TIMEZONE_KEY;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TIMEZONE_VALUE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TIME_MAX_KEY;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TIME_MAX_VALUE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TIME_MIN_KEY;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TIME_MIN_VALUE;
import static com.arc.agni.irctcbookingreminder.constants.Constants.TITLE_HOLIDAY_LIST;

public class HolidayListActivity extends AppCompatActivity {
    static List<Items> holidaysList = new ArrayList<>();
    public static HolidayListAdapter holidayListAdapter;
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE_HOLIDAY_LIST);
        setContentView(R.layout.loading_screen);

        // Request for ad
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if (holidaysList.size() == 0) {
            new HolidayListTask().execute();
        } else {
            renderHolidayList();
        }
    }

    /**
     * Async Class To Perform Network Operations
     */
    private class HolidayListTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... urls) {
            try {
                Map<String, List<Items>> holidays = new LinkedHashMap<>();
                CalendarResponse response = new CalendarResponse();
                StringBuilder builder = new StringBuilder();
                String jsonResponse = null;
                String URL = HOLIDAY_LIST_URL.replace(GOOGLE_CALENDAR_NAME_KEY, GOOGLE_CALENDAR_NAME_VALUE).replace(GOOGLE_CALENDAR_ID_KEY, GOOGLE_CALENDAR_ID_VALUE).replace(TIMEZONE_KEY, TIMEZONE_VALUE).replace(TIME_MIN_KEY, TIME_MIN_VALUE).replace(TIME_MAX_KEY, TIME_MAX_VALUE);

                HttpURLConnection client = (HttpURLConnection)
                        (new URL(URL)
                                .openConnection());
                client.setConnectTimeout(CONNECTION_TIMEOUT);
                client.connect();

                if (client.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String line;
                    while ((line = br.readLine()) != null) {
                        builder.append(line).append("\n");
                    }
                    br.close();
                    jsonResponse = builder.toString();
                }

                if (null != jsonResponse) {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    response = mapper.readValue(jsonResponse, CalendarResponse.class);
                }


                if (null != response && null != response.getItems()) {
                    String day;
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    for (Items item : response.getItems()) {
                        if (null != item.getStart() && null != item.getStart().getDate()) {
                            if (item.getStart().getDate().length() >= 10) {
                                String month = MONTHS[(Integer.parseInt((item.getStart().getDate().substring(5, 7))) - 1)];
                                calendar.setTime(format.parse(item.getStart().getDate()));
                                day = DAYS[calendar.get(Calendar.DAY_OF_WEEK) - 1];
                                item.setDay(day);
                                if (null != holidays.get(month)) {
                                    Objects.requireNonNull(holidays.get(month)).add(item);
                                } else {
                                    holidays.put(month, new ArrayList<>(Collections.singletonList(item)));
                                }
                            }
                        }
                    }
                }

                Comparator<Items> dateComparator = (itemOne, itemTwo) -> itemOne.getStart().getDate().compareTo(itemTwo.getStart().getDate());

                for (Map.Entry<String, List<Items>> entry : holidays.entrySet()) {
                    Items itemWithMonth = new Items();
                    itemWithMonth.setMonthLabel(entry.getKey());
                    holidaysList.add(itemWithMonth);

                    List<Items> itemsToBeAdded = holidays.get(entry.getKey());
                    assert itemsToBeAdded != null;
                    Collections.sort(itemsToBeAdded, dateComparator);
                    holidaysList.addAll(itemsToBeAdded);
                }

            } catch (Exception e) {
                Log.e("", SOMETHING_WENT_WRONG, e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            renderHolidayList();
        }
    }

    public void renderHolidayList() {
        setContentView(R.layout.activity_holiday_list);
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        if (holidaysList.size() > 0) {
            holidayListAdapter = new HolidayListAdapter(holidaysList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HolidayListActivity.this);
            recyclerView = findViewById(R.id.holidaylistrecycleview);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(holidayListAdapter);
            mAdView.loadAd(adRequest);
        } else {
            Toast.makeText(HolidayListActivity.this, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
            mAdView.loadAd(adRequest);
        }
    }

}
