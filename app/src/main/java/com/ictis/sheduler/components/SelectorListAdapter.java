package com.ictis.sheduler.components;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ictis.sheduler.MainActivity;
import com.ictis.sheduler.R;
import com.ictis.sheduler.model.Choice;

public class SelectorListAdapter extends ArrayAdapter<Choice> {

    private final Activity context;

    public SelectorListAdapter(Activity context, int resource, @NonNull Choice[] objects) {
        super(context, R.layout.selector_row, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        Choice row = this.getItem(position);
        View rowView = inflater.inflate(R.layout.selector_row, null, true);
        if (row == null) {
            return rowView;
        }

        TextView editText = rowView.findViewById(R.id.personName);
        editText.setText(row.getName());
        rowView.setOnClickListener(v1 -> {
            MainActivity.fillList("id=" + row.getId(), context);
           /* TextView listTitle = context.findViewById(R.id.listTitle);
            listTitle.setText(row.getName());*/
        });
        return rowView;
    }
}
