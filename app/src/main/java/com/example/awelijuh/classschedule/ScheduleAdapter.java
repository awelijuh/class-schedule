package com.example.awelijuh.classschedule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScheduleAdapter extends ArrayAdapter<Pair<String, String>> {
    public ScheduleAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item, parent, false);
        }
        Pair<String, String> item = getItem(position);
        TextView textView = convertView.findViewById(R.id.textView);
        TextView textView2 = convertView.findViewById(R.id.textView2);

        textView.setText(item.second);
        textView2.setText(item.first);
        LinearLayout layout = convertView.findViewById(R.id.constraint_layout);
        if (position % 2 == 0) {
            layout.setBackgroundColor(convertView.getResources().getColor(R.color.color2));
        }
        else {
            layout.setBackgroundColor(convertView.getResources().getColor(R.color.color1));
        }

        return convertView;
    }
}
