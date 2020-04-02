package com.arc.agni.irctcbookingreminder.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arc.agni.irctcbookingreminder.R;
import com.arc.agni.irctcbookingreminder.activities.AdvanceBookingReminderActivity;
import com.arc.agni.irctcbookingreminder.activities.HomeScreenActivity;
import com.arc.agni.irctcbookingreminder.bean.Items;
import com.arc.agni.irctcbookingreminder.utils.CommonUtil;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.arc.agni.irctcbookingreminder.constants.Constants.EXIT_WARNING;
import static com.arc.agni.irctcbookingreminder.constants.Constants.LABEL_INPUT_DAY;
import static com.arc.agni.irctcbookingreminder.constants.Constants.LABEL_INPUT_MONTH;
import static com.arc.agni.irctcbookingreminder.constants.Constants.LABEL_INPUT_YEAR;
import static com.arc.agni.irctcbookingreminder.constants.Constants.LABEL_TRAVEL_HINT;
import static com.arc.agni.irctcbookingreminder.constants.Constants.MINUS_120_DAYS;
import static com.arc.agni.irctcbookingreminder.constants.Constants.MONTHS;

public class HolidayListAdapter extends RecyclerView.Adapter<HolidayListAdapter.MyViewHolder> {

    private List<Items> items;
    private Context context;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView holidayName;
        TextView bookingStatus;
        RelativeLayout layout;

        MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.holiday_date);
            holidayName = view.findViewById(R.id.holiday_name);
            layout = view.findViewById(R.id.holiday_layout);
            bookingStatus = view.findViewById(R.id.booking_status);
        }
    }

    public HolidayListAdapter(List<Items> items, Context context) {
        this.items = items;
        this.context = context;
    }

/*    public void refreshEventList(ArrayList<Items> items) {
        this.items = items;
        notifyDataSetChanged();
    }*/

    @NonNull
    @Override
    public HolidayListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holiday_list, parent, false);
        return new HolidayListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Items currentItem = items.get(position);

        if (null != currentItem) {
            String holidayName = currentItem.getSummary();
            String month = currentItem.getMonthLabel();

            // If month name is passed, it should be formatted like a title
            if (null != month && Arrays.asList(MONTHS).contains(month)) {
                holder.date.setVisibility(View.GONE);
                holder.bookingStatus.setVisibility(View.GONE);
                holder.layout.setBackgroundColor(0xFF03A9F4);
                holder.layout.setOnClickListener(null);

                holder.holidayName.setText(month);
                holder.holidayName.setTextSize(21);
                holder.holidayName.setTextColor(0xFFFFFFFF);
                holder.holidayName.setTypeface(holder.holidayName.getTypeface(), Typeface.BOLD);
                holder.holidayName.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            } else {
                boolean bookingAllowed = false;
                SpannableString dateAndDay;
                String date = currentItem.getStart() != null ? currentItem.getStart().getDate() : null;
                String day = currentItem.getDay().substring(0, 3);
                if (null != date && date.length() >= 10) {
                    date = date.substring(8, 10);
                    holder.holidayName.setText(holidayName);
                    holder.holidayName.setTextSize(16);
                    holder.holidayName.setTextColor(0xFF000000);
                    holder.holidayName.setTypeface(null, Typeface.NORMAL);
                    holder.holidayName.setGravity(Gravity.START);

                    dateAndDay = new SpannableString(date + "\n" + day);
                    dateAndDay.setSpan(new RelativeSizeSpan(1.8f), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    dateAndDay.setSpan(new StyleSpan(Typeface.BOLD), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    holder.date.setText(dateAndDay);
                    holder.date.setVisibility(View.VISIBLE);
                    holder.layout.setBackgroundColor(0xFFFFFFFF);

                    // Booking status logic
                    final String travelDate = currentItem.getStart().getDate();
                    Calendar holidayDate = Calendar.getInstance();
                    holidayDate.set(Integer.parseInt(travelDate.substring(0, 4)), (Integer.parseInt(travelDate.substring(5, 7)) - 1), Integer.parseInt(travelDate.substring(8)));
                    holidayDate.add(Calendar.DAY_OF_YEAR, MINUS_120_DAYS);
                    String bookingStatusText;
                    if (holidayDate.getTime().after(Calendar.getInstance().getTime())) {
                        bookingStatusText = "Booking starts on " + CommonUtil.formatCalendarDateToFullText(holidayDate);
                        bookingAllowed = true;
                    } else {
                        bookingStatusText = "Booking has already started";
                    }
                    holder.bookingStatus.setText(bookingStatusText);
                    holder.bookingStatus.setVisibility(View.VISIBLE);

                    // Enable onclick listener to advance booking if booking not yet started
                    if (bookingAllowed) {
                        int travelYear = Integer.parseInt(travelDate.substring(0, 4));
                        int travelMonth = (Integer.parseInt(travelDate.substring(5, 7)) - 1);
                        int travelDay = Integer.parseInt(travelDate.substring(8));

                        holder.layout.setOnClickListener(v -> {

                            Calendar travelDateActual = Calendar.getInstance();
                            travelDateActual.set(travelYear, travelMonth, travelDay);
                            if (travelDateActual.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                                new AlertDialog.Builder(context)
                                        .setTitle("")
                                        .setMessage("The selected holiday falls on Saturday. Did you plan to travel by Friday ?")
                                        .setNegativeButton("No, Proceed with selected date", (arg0, arg1) -> createReminderForHoliday(holidayName, travelDateActual))
                                        .setPositiveButton("Yes, Remind me for Friday booking", (arg0, arg1) -> {
                                            travelDateActual.add(Calendar.DAY_OF_YEAR, -1);
                                            createReminderForHoliday(holidayName, travelDateActual);
                                        }).create().show();
                            } else if (travelDateActual.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                                new AlertDialog.Builder(context)
                                        .setTitle("")
                                        .setMessage("The selected holiday falls on Sunday. Did you plan to travel by Friday ?")
                                        .setNegativeButton("No, Proceed with selected date", (arg0, arg1) -> createReminderForHoliday(holidayName, travelDateActual))
                                        .setPositiveButton("Yes, Remind me for Friday Booking", (arg0, arg1) -> {
                                            travelDateActual.add(Calendar.DAY_OF_YEAR, -2);
                                            createReminderForHoliday(holidayName, travelDateActual);
                                        }).create().show();
                            } else {
                                createReminderForHoliday(holidayName, travelDateActual);
                            }
                        });
                    }
                }
            }

        }

    }

    public void createReminderForHoliday(String holidayName, Calendar travelDate) {
        Intent intent = new Intent(context, AdvanceBookingReminderActivity.class);
        intent.putExtra(LABEL_INPUT_YEAR, travelDate.get(Calendar.YEAR));
        intent.putExtra(LABEL_INPUT_MONTH, travelDate.get(Calendar.MONTH));
        intent.putExtra(LABEL_INPUT_DAY, travelDate.get(Calendar.DAY_OF_MONTH));
        intent.putExtra(LABEL_TRAVEL_HINT, holidayName);
        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}
