package com.example.awelijuh.classschedule;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ScheduleFragment extends Fragment {



    public static Bundle getBundle(Parser parser, int numSheet, int numGroup, int numDay) {
        Bundle bundle = new Bundle();
        bundle.putStringArray("schedule", parser.getSchedule(numSheet, numGroup, numDay));
        bundle.putStringArray("periods", parser.getTimePeriod(numSheet, numDay));
        return bundle;
    }


    public static ScheduleFragment newFragment(Parser parser, int numSheet, int numGroup, int numDay) {
        ScheduleFragment fragment = new ScheduleFragment();
        fragment.setArguments(getBundle(parser, numSheet, numGroup, numDay));
        return fragment;
    }

    String[] schedule;
    String[] periods;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);



        Bundle bundle = getArguments();
        schedule = bundle.getStringArray("schedule");
        periods = bundle.getStringArray("periods");
        if (schedule == null && savedInstanceState != null) {
            schedule = savedInstanceState.getStringArray("schedule");
            periods = savedInstanceState.getStringArray("periods");
        }

        listView = rootView.findViewById(R.id.listView);
        initListView();

        return rootView;
    }

    void initListView() {
        ScheduleAdapter adapter = new ScheduleAdapter(getActivity(), R.id.listView);
        addToAdapter(adapter);
        listView.setAdapter(adapter);
    }

    void addToAdapter(ScheduleAdapter adapter) {
        for (int i = 0; i < schedule.length; i++) {
            adapter.add(new Pair<String, String>(schedule[i], periods[i]));
        }
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putStringArray("schedule", schedule);
        outState.putStringArray("periods", periods);
        super.onSaveInstanceState(outState);
    }
}