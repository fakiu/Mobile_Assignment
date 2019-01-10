package com.example.chanys.assignment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SportAdapter extends ArrayAdapter<Sport> {

    public SportAdapter(Activity context, int resource, List<Sport> list) {
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Sport sport = getItem(position);

        LayoutInflater inflater  = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //parent = content_main
        View rowView = inflater.inflate(R.layout.sport_record, parent, false);

        TextView textViewSport;

        textViewSport = (TextView)rowView.findViewById(R.id.textViewSport);

        textViewSport.setText(String.format("%s", sport.getSportName()));
        return rowView;
    }
}
