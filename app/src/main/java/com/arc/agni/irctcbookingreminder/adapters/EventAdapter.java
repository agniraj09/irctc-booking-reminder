package com.arc.agni.irctcbookingreminder.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arc.agni.irctcbookingreminder.R;
import com.arc.agni.irctcbookingreminder.activities.CustomReminderActivity;
import com.arc.agni.irctcbookingreminder.activities.ViewRemindersActivity;
import com.arc.agni.irctcbookingreminder.bean.Event;
import com.arc.agni.irctcbookingreminder.utils.DialogUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.arc.agni.irctcbookingreminder.constants.Constants.DAYS;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private Event[] events;
    private Context context;
    DialogUtil dialogUtil;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView eventTitle;
        private TextView travelDateLabel;
        private TextView travelDate;
        private TextView reminderDate;
        private TextView reminderType;
        private ImageView delete;

        public MyViewHolder(View view) {
            super(view);
            eventTitle = view.findViewById(R.id.event_title);
            travelDateLabel = view.findViewById(R.id.travel_date_label);
            travelDate = view.findViewById(R.id.travel_date);
            reminderDate = view.findViewById(R.id.reminder_date);
            reminderType = view.findViewById(R.id.reminder_type);
            delete = view.findViewById(R.id.delete_event);
        }
    }

    public EventAdapter(ViewRemindersActivity viewRemindersActivity, Event[] events) {
        context = viewRemindersActivity;
        dialogUtil = new DialogUtil(context);
        this.events = events;
    }

    public void refreshEventList(ArrayList<Event> events) {
        this.events = events.toArray(new Event[events.size()]);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        String remDate = "";
        String traDate = "";
        long reminderdate = Long.parseLong(events[position].getReminderDate());
        long traveldate = Long.parseLong(events[position].getTravelDate());

        Calendar reminderDatetoUI = Calendar.getInstance();
        Calendar travelDatetoUI = Calendar.getInstance();
        reminderDatetoUI.setTimeInMillis(reminderdate);
        travelDatetoUI.setTimeInMillis(traveldate);

        remDate = ("  :  " + DAYS[reminderDatetoUI.get(Calendar.DAY_OF_WEEK)-1] + ", " +  reminderDatetoUI.get(Calendar.DATE) + "/" + (reminderDatetoUI.get(Calendar.MONTH) + 1) + "/" + reminderDatetoUI.get(Calendar.YEAR));
        traDate = ("  :  " + DAYS[travelDatetoUI.get(Calendar.DAY_OF_WEEK)-1] + ", " + travelDatetoUI.get(Calendar.DATE) + "/" + (travelDatetoUI.get(Calendar.MONTH) + 1) + "/" + travelDatetoUI.get(Calendar.YEAR));
        String reminderType = "  :  " + events[position].getEventType();

        holder.eventTitle.setText(events[position].getEventTitle());
        holder.travelDate.setText(traDate);
        holder.reminderDate.setText(remDate);
        holder.reminderType.setText(reminderType);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUtil.showDeleteEventDialog(context, events[position].getEventID());
            }
        });

    }

    @Override
    public int getItemCount() {
        return events.length;
    }

}
