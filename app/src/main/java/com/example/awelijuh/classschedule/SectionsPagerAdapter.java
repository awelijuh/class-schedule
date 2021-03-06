package com.example.awelijuh.classschedule;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    String[] titles;
    Parser parser;
    int numSheet;
    int numGroup;
    int currentDay;



    public SectionsPagerAdapter(FragmentManager fm, Parser parser, int numSheet, int numGroup, int currentDay) {
        super(fm);
        this.parser = parser;
        titles = parser.getDateNames(numSheet);
        setParams(numSheet, numGroup, currentDay);
    }

    public void setParams(int numSheet, int numGroup, int currentDay) {
        this.numSheet = numSheet;
        this.numGroup = numGroup;
        this.currentDay = currentDay;
    }

    public void replaceParser(Parser parser) {
        this.parser = parser;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return ScheduleFragment.newFragment(parser, numSheet, numGroup, position, position == currentDay);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public long getItemId(int position) {
        return numSheet * 100 + numGroup * 10 + position;
    }
}