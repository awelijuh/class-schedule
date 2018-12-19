package com.example.awelijuh.classschedule;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Item implements Parcelable{
    private String duration;
    private String subject;
    private boolean star;

    public Item(String duration, String subject) {
        this.duration = duration;
        this.subject = subject;
    }

    protected Item(Parcel in) {
        duration = in.readString();
        subject = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getDuration() {
        return duration;
    }

    public String getSubject() {
        return subject;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(duration);
        dest.writeString(subject);
    }

    public boolean insideDuration() {
        Log.d("time_", "duration:" + duration);
        try {
            Log.d("time_", "ok");
            Pattern pattern = Pattern.compile("\\d{2}.\\d{2}-\\d{2}.\\d{2}");
            Matcher matcher = pattern.matcher(duration);
            Log.d("time_", "ok2");
            matcher.find();
            String s = duration.substring(matcher.start(), matcher.end());
            Log.d("time_", "s:" + s);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH.mm");
            Date date1 = simpleDateFormat.parse(s.substring(0, 5));
            Date date2 = simpleDateFormat.parse(s.substring(6, 11));
            Date date = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            Log.d("time_", "dur1:" + date1.getTime());
            Log.d("time_", "dur2:" + date2.getTime());
            Log.d("time_", "time:" + date.getTime());
            return date1.getTime() <= date.getTime() && date.getTime() <= date2.getTime();
        } catch (Exception e) {
            return false;
        }
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public boolean getStar() {
        return star;
    }

}
