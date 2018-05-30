package com.wolf.apps.expensemanager.UIX;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.CalendarView;

import com.wolf.apps.expensemanager.MainActivity;
import com.wolf.apps.expensemanager.R;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = findViewById(R.id.calendar_main);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar temp = Calendar.getInstance();
                temp.set(year, month, dayOfMonth);
                fragment_transactions.setCurrentDate(temp);
                finish();
            }
        });
    }
}
