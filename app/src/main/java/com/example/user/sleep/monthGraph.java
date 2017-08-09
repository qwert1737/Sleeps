package com.example.user.sleep;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by user on 2017-07-31.
 */

public class monthGraph extends AppCompatActivity {

    BarChart barChart;
    int studentId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthgraph);

        Intent intent = getIntent();

        barChart = (BarChart) findViewById(R.id.month_bargraph);
        studentId = intent.getIntExtra("id", 1);

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select day, sleep_time from tb_sleep where student_id=? AND start_end=?",
                new String[]{String.valueOf(studentId), "End"});

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> theDates = new ArrayList<>();

        int i = 1;
        String date;
        int date_int;
        int year, month, dayOfMonth;
        theDates.add("0");

        while(cursor.moveToNext()){
            date = cursor.getString(0);
            date_int = Integer.parseInt(date);
            year = date_int/10000;
            month = (date_int%10000)/100;
            dayOfMonth = date_int%100;
            date = year+"/"+month+"/"+dayOfMonth;

            theDates.add(date);
            barEntries.add(new BarEntry(i,cursor.getInt(1)));

            i++;
        }
        db.close();

        BarDataSet barDataSet = new BarDataSet(barEntries,"Days");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextSize(10f);


        BarData theData = new BarData(barDataSet);
        barChart.setData(theData);
        barChart.invalidate();

        barChart.getBarData().setBarWidth(0.5f);
        barChart.setDescription(null);
        barChart.animateXY(2000,2000);
        barChart.setVisibleXRangeMaximum(30);
        barChart.setScrollX(2);
        barChart.moveViewToX(28);


        XAxis xAxis = barChart.getXAxis();
        //xAxis.setGranularity(0.5f);
        xAxis.setGranularityEnabled(true);
        // xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        //xAxis.setAxisMaximum(7);
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(theDates));


        barChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(10f);
        leftAxis.setAxisMinimum(0f);
    }







}
