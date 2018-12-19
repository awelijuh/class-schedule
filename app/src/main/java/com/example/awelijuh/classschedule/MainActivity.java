package com.example.awelijuh.classschedule;

import android.content.Intent;
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
import android.widget.TextView;
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
        if (id == R.id.item_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

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
                group,
                getCurrentDay()
        );
        viewPager.setAdapter(pagerAdapter);
        viewPager.clearOnPageChangeListeners();
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(getCurrentDay());
    }

    int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        if (day < 0) {
            day = 0;
        }
        return day;
    }

    private void initCourseSpinners() {
        Spinner spinner = findViewById(R.id.course_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String[] s = parser.getSheetNames();
        if (s == null) {
            Toast.makeText(this, "Ошибка. Попробуйте обновить.", Toast.LENGTH_SHORT).show();
            return;
        }

        adapter.addAll(s);
        spinner.setAdapter(adapter);

        spinner.setSelection(sharedPreferences.getInt("default_course", 0));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sharedPreferences.edit().putInt("default_course", position).apply();
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

        String[] s = parser.getGroupNames(course);
        if (s == null) {
            Toast.makeText(this, "Ошибка. Попробуйте обновить.", Toast.LENGTH_SHORT).show();
            return;
        }

        adapter.addAll(s);
        spinner.setAdapter(adapter);
        spinner.setSelection(sharedPreferences.getInt("default_group", 0));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sharedPreferences.edit().putInt("default_group", position).apply();
                initTab(course, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onDownloaded(Boolean ok) {

        if (!ok) {
            Toast.makeText(this, "Обновление не удалось.", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            Toast.makeText(this, "Загрузка удалась!", Toast.LENGTH_SHORT).show();
        }
        parser = new Parser(this);
        if (parser.isValid()) {
            initCourseSpinners();
        }
        else {
            Toast.makeText(this, "Ошибка. Попробуйте обновить.", Toast.LENGTH_SHORT).show();
        }
    }

    void update() {
        Toast.makeText(this, "Загрузка расписания.", Toast.LENGTH_SHORT).show();
        Downloader downloader = new Downloader(this, this);
        downloader.download();
    }


}
