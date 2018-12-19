package com.example.awelijuh.classschedule;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.Math.min;


public class ScheduleFragment extends Fragment {



    public static Bundle getBundle(Parser parser, int numSheet, int numGroup, int numDay, boolean current) {
        Bundle bundle = new Bundle();
        String[] sch = parser.getSchedule(numSheet, numGroup, numDay);
        String[] per = parser.getTimePeriod(numSheet, numDay);
        if (sch == null) {
            return null;
        }

        Parcelable[] parcelables = new Item[min(sch.length, per.length)];
        for (int i = 0; i < min(sch.length, per.length); i++) {
            parcelables[i] = new Item(per[i], sch[i]);
        }
        bundle.putParcelableArray("schedule", parcelables);
        bundle.putBoolean("current", current);
        return bundle;
    }


    public static ScheduleFragment newFragment(Parser parser, int numSheet, int numGroup, int numDay, boolean current) {
        ScheduleFragment fragment = new ScheduleFragment();
        fragment.setArguments(getBundle(parser, numSheet, numGroup, numDay, current));
        return fragment;
    }

    Item[] schedule;
    RecyclerView recyclerView;
    boolean current;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);



        Bundle bundle = getArguments();
        if (bundle != null) {
            schedule = (Item[]) bundle.getParcelableArray("schedule");
            current = bundle.getBoolean("current");
        }
        if (schedule == null && savedInstanceState != null) {
            schedule = (Item[]) savedInstanceState.getParcelableArray("schedule");
            current = savedInstanceState.getBoolean("current");
        }

        recyclerView = rootView.findViewById(R.id.recyclerView);
        initRecyclerView();

        return rootView;
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new Decoration(getContext()));
        ScheduleRecyclerAdapter adapter = new ScheduleRecyclerAdapter(Arrays.asList(schedule));
        recyclerView.setAdapter(adapter);
        markCurrent();
    }


    public void markCurrent() {
        if (!current) return;
        for (int i = 0; i < schedule.length; i++) {
            if (schedule[i].insideDuration()) {
                Log.d("time_", "true");
                ((ScheduleRecyclerAdapter) recyclerView.getAdapter()).setStar(i, true);
            }
            else {
                ((ScheduleRecyclerAdapter) recyclerView.getAdapter()).setStar(i, false);
            }
        }
        recyclerView.getLayoutManager().onItemsUpdated(recyclerView, 0, recyclerView.getChildCount());
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArray("schedule", schedule);
        outState.putBoolean("current", current);
        super.onSaveInstanceState(outState);
    }



}