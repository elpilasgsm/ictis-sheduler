package com.example.myapplication.components;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myapplication.R;
import com.example.myapplication.model.task.FormattedItemRow;

public class TimelineListAdapter extends ArrayAdapter<FormattedItemRow> {

    private final Activity context;

    public TimelineListAdapter(Activity context, int resource, @NonNull FormattedItemRow[] objects) {
        super(context, R.layout.timeline_row, objects);
        this.context = context;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        FormattedItemRow row = this.getItem(position);

        if (row == null) {
            return null;
        }
        View rowView = null;
        if (row.getDate() != null) {
            rowView = inflater.inflate(R.layout.date_row, null, true);

            TextView titleText = (TextView) rowView.findViewById(R.id.date);
            titleText.setText(row.getDate());
        } else {
            rowView = inflater.inflate(R.layout.timeline_row, null, true);

            TextView timeText = (TextView) rowView.findViewById(R.id.time);
            timeText.setText(row.getTime());

            TextView dataText = (TextView) rowView.findViewById(R.id.data);
            dataText.setText(row.getData());
        }
        return rowView;
    }
}
