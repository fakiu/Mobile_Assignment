package com.example.chanys.assignment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BookingHistoryAdapter extends ArrayAdapter<BookingInformation> {

    public BookingHistoryAdapter(Activity context, int resource, List<BookingInformation> list) {
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BookingInformation booking = getItem(position);

        LayoutInflater inflater  = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //parent = content_main
        View rowView = inflater.inflate(R.layout.booking_history_record, parent, false);

        TextView textViewBookCourt, textViewBookDate, textViewBookTime;

        textViewBookCourt = (TextView)rowView.findViewById(R.id.textViewCourtName);
        textViewBookDate = (TextView)rowView.findViewById(R.id.textViewBookDate);
        textViewBookTime = (TextView)rowView.findViewById(R.id.textViewBookTime);

        textViewBookCourt.setText(String.format("%s", booking.getCourtName()));
        textViewBookDate.setText(String.format("%s : %s / %s / %s", "Date :", booking.getBookingDay(), booking.getBookingMonth(), booking.getBookingYear()));
        textViewBookTime.setText(String.format("%s : %s - %s", "Time :", booking.getStartTime(), booking.getEndTime()));
        return rowView;
    }
}
