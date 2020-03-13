package com.arc.agni.irctcbookingreminder.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.arc.agni.irctcbookingreminder.R;
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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import androidx.appcompat.app.AppCompatActivity;

import static com.arc.agni.irctcbookingreminder.constants.Constants.CONNECTION_TIMEOUT;
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
    static Map<String, Map<String, String>> HOLIDAY_LIST = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE_HOLIDAY_LIST);
        setContentView(R.layout.loading_screen);

        // Request for ad
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        new HolidayListTask().execute();
    }

    /**
     * Async Class To Perform Network Operations
     */
    private class HolidayListTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... urls) {
            try {
                Map<String, String> holidays;
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
                    for (Items item : response.getItems()) {
                        if (null != item.getStart() && null != item.getStart().getDate()) {
                            if (item.getStart().getDate().length() >= 7) {
                                holidays = new LinkedHashMap<>();
                                holidays.put(item.getSummary(), item.getStart().getDate());
                                String month = MONTHS[(Integer.parseInt((item.getStart().getDate().substring(5, 7))) - 1)];
                                if (null != HOLIDAY_LIST.get(month)) {
                                    HOLIDAY_LIST.get(month).putAll(holidays);
                                } else {
                                    HOLIDAY_LIST.put(month, holidays);
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                Log.e("", SOMETHING_WENT_WRONG, e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setContentView(R.layout.activity_holiday_list);
            TextView showList = findViewById(R.id.holiday_list);
            AdView mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();

            if (HOLIDAY_LIST.values().size() > 0) {
                StringBuilder builder = new StringBuilder();
                for (Map.Entry<String, Map<String, String>> holiday : HOLIDAY_LIST.entrySet()) {
                    builder.append("--------------------------------");
                    builder.append(holiday.getKey()).append("\n");
                    for (Map.Entry<String, String> items : holiday.getValue().entrySet()) {
                        builder.append(items.getKey()).append(" - ").append(items.getValue()).append("\n");
                    }
                }

                showList.setText(builder.toString());
                mAdView.loadAd(adRequest);
            } else {
                showList.setText(SOMETHING_WENT_WRONG);
                mAdView.loadAd(adRequest);
            }

        }
    }

}
