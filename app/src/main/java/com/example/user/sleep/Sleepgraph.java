package com.example.user.sleep;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ColorFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;


public class Sleepgraph extends AppCompatActivity implements View.OnClickListener{


    ImageView day_graphs;
    ImageView week_graphs;
    ImageView month_graph;

    ArrayList<SleepVO> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleepgraph);

        day_graphs = (ImageView) findViewById(R.id.read_sleep_day);
        week_graphs = (ImageView) findViewById(R.id.read_sleep_week);
        month_graph = (ImageView) findViewById(R.id.read_sleep_month);

        day_graphs.setOnClickListener(this);
        week_graphs.setOnClickListener(this);
        month_graph.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent1 = getIntent();
        int studentId = intent1.getIntExtra("id", 1);

        if(v == day_graphs) {
            Intent intent = new Intent(this, DayCalendar.class);
            intent.putExtra("id",studentId);
            startActivity(intent);
        }
        else if(v == week_graphs){
            Intent intent = new Intent(this, weekGraph.class);
            intent.putExtra("id",studentId);
            startActivity(intent);
        }
        else if(v == month_graph){
            Intent intent = new Intent(this, monthGraph.class);
            intent.putExtra("id",studentId);
            startActivity(intent);
        }

    }
}
