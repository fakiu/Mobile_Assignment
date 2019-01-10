package com.example.chanys.assignment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CourtAdapter extends ArrayAdapter<Court> {

    public CourtAdapter(Activity context, int resource, List<Court> list) {
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Court court = getItem(position);

        LayoutInflater inflater  = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //parent = content_main
        View rowView = inflater.inflate(R.layout.court_record, parent, false);

        TextView textViewCourtName, textViewCourtAddress;

        textViewCourtName = (TextView)rowView.findViewById(R.id.textViewCourtName);
        textViewCourtAddress = (TextView)rowView.findViewById(R.id.textViewCourtAddress);

        textViewCourtName.setText(String.format("%s", court.getCourtName()));
        textViewCourtAddress.setText(String.format("%s : %s", getContext().getString(R.string.CourtAddress), court.getCourtAddress()));
        return rowView;
    }
}
