package com.example.user.sleep;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;

/**
 * Created by user on 2017-08-09.
 */

public class DayCalendar extends AppCompatActivity {

    int studentId;
    CalendarView calendarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_view);

        Intent intent1 = getIntent();
        studentId = intent1.getIntExtra("id", 1);

        calendarView = (CalendarView) findViewById(R.id.calendar_View);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = year+"년 "+month+"월 "+dayOfMonth+"일";
                Intent intent = new Intent(DayCalendar.this, dayGraph.class);
                intent.putExtra("id",studentId);
                intent.putExtra("date",date);
                intent.putExtra("year",year);
                intent.putExtra("month",month);
                intent.putExtra("dayOfMonth",dayOfMonth);
                startActivity(intent);
            }
        });
    }
}
