package com.example.chanys.assignment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AdminBookingHistoryAdapter extends ArrayAdapter<BookingInformation> {

    public AdminBookingHistoryAdapter(Activity context, int resource, List<BookingInformation> list) {
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BookingInformation booking = getItem(position);

        LayoutInflater inflater  = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //parent = content_main
        View rowView = inflater.inflate(R.layout.admin_booking_record, parent, false);

        TextView textViewUsername, textViewDate, textViewTime;

        textViewUsername = (TextView)rowView.findViewById(R.id.textViewUsername);
        textViewDate = (TextView)rowView.findViewById(R.id.textViewDate);
        textViewTime = (TextView)rowView.findViewById(R.id.textViewTime);

        textViewUsername.setText(String.format("%s", booking.getUsername()));
        textViewDate.setText(String.format("%s : %s / %s / %s", "Date :", booking.getBookingDay(), booking.getBookingMonth(), booking.getBookingYear()));
        textViewTime.setText(String.format("%s : %s - %s", "Time :", booking.getStartTime(), booking.getEndTime()));
        return rowView;
    }
}
