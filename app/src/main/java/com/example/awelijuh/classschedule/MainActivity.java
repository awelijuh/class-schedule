package com.example.awelijuh.classschedule;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements Downloaded {

    Parser parser;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getPreferences(MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        String path = getCacheDir() + "/" + "schedule";
        File file = new File(path);
        if (file.exists()) {
            parser = new Parser(this);
            if (parser.isValid()) {
                initCourseSpinners();
            } else {
                update();
            }
        } else {
            update();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.item_update) {
            update();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initTab(int course, int group) {
        Log.d("deb_", "course=" + course + "\ngroup=" + group);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);
        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager(),
                new Parser(getApplicationContext()),
                course,
                group
        );
        viewPager.setAdapter(pagerAdapter);
        viewPager.clearOnPageChangeListeners();
        tabLayout.setupWithViewPager(viewPager);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        Log.d("deb_", "day=" + day);
        day = (day + 6) % 6;
        viewPager.setCurrentItem(day);
    }

    private void initCourseSpinners() {
        Spinner spinner = findViewById(R.id.course_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(parser.getSheetNames());
        spinner.setAdapter(adapter);

        spinner.setSelection(sharedPreferences.getInt("default_course", 0));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sharedPreferences.edit().putInt("default_course", position).commit();
                initGroupSpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initGroupSpinner(final int course) {
        Spinner spinner = findViewById(R.id.group_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(parser.getGroupNames(course));
        spinner.setAdapter(adapter);
        spinner.setSelection(sharedPreferences.getInt("default_group", 0));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sharedPreferences.edit().putInt("default_group", position).commit();
                initTab(course, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    boolean lockUpdate = false;

    @Override
    public void onDownloaded() {
        lockUpdate = false;
        parser = new Parser(this);
        if (!parser.isValid()) {
            Toast.makeText(this, "Не удалось загрузить расписание.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Расписание успешно загружено.", Toast.LENGTH_SHORT).show();
            initCourseSpinners();
        }
    }

    void update() {
        if (lockUpdate) {
            return;
        }
        lockUpdate = true;
        Downloader downloader = new Downloader(this, this);
        downloader.download();
    }


}
