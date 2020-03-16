package com.arc.agni.irctcbookingreminder.adapters;

import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arc.agni.irctcbookingreminder.R;
import com.arc.agni.irctcbookingreminder.bean.Items;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.arc.agni.irctcbookingreminder.constants.Constants.MONTHS;

public class HolidayListAdapter extends RecyclerView.Adapter<HolidayListAdapter.MyViewHolder> {

    private List<Items> items;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView holidayName;
        TextView holidayDay;
        RelativeLayout layout;

        MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.holiday_date);
            holidayName = view.findViewById(R.id.holiday_name);
            holidayDay = view.findViewById(R.id.holiday_day);
            layout = view.findViewById(R.id.holiday_layout);
        }
    }

    public HolidayListAdapter(List<Items> items) {
        this.items = items;
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
                holder.holidayName.setText(month);
                holder.holidayName.setTextSize(20);
                holder.holidayName.setTextColor(0xFFFFFFFF);
                holder.holidayName.setTypeface(holder.holidayName.getTypeface(), Typeface.BOLD);
                holder.holidayName.setGravity(Gravity.CENTER_HORIZONTAL);
                holder.date.setVisibility(View.GONE);
                holder.holidayDay.setVisibility(View.GONE);
                holder.layout.setBackgroundColor(0xFF03A9F4);
            } else {
                String date = currentItem.getStart() != null ? currentItem.getStart().getDate() : null;
                if (null != date && date.length() >= 10) {
                    date = date.substring(8, 10);
                    holder.holidayName.setText(holidayName);
                    holder.holidayName.setTextSize(16);
                    holder.holidayName.setTextColor(0xFF000000);
                    holder.holidayName.setTypeface(null, Typeface.NORMAL);
                    holder.holidayName.setGravity(Gravity.START);
                    holder.date.setText(date);
                    holder.date.setVisibility(View.VISIBLE);
                    holder.holidayDay.setText(currentItem.getDay().substring(0, 3));
                    holder.holidayDay.setVisibility(View.VISIBLE);
                    holder.layout.setBackgroundColor(0xFFFFFFFF);
                }
            }

        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
