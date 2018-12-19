package com.example.awelijuh.classschedule;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ScheduleItemHolder extends RecyclerView.ViewHolder {

    TextView textView;
    TextView textView2;
    ImageView imageView;

    public ScheduleItemHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.textView);
        textView2 = itemView.findViewById(R.id.textView2);
        imageView = itemView.findViewById(R.id.imageView2);
    }

    public void bindCrime(Item item) {
        textView.setText(item.getDuration());
        textView2.setText(item.getSubject());
        if (item.getStar()) {
            imageView.setImageResource(R.drawable.ic_star_black_24dp);
        }
        else {
            imageView.setImageResource(R.drawable.ic_star_black2_24dp);
        }
    }


}
