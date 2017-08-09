package com.example.user.sleep;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.zip.Inflater;

//import static com.example.sleep.R.id.radio;
//import static com.example.sleep.R.id.read_sleep_day;


public class dayGraph extends AppCompatActivity{

    int studentId;

    int hour;
    int minute;
    int type_conversion;
    int year_int, month_int,dayOfMonth_int;
    String date_str;
    int date_int;
    String string_time;

    ListView listView;
    ArrayList<HashMap<String, String>> sleepList;
    SimpleAdapter sa;

    TextView textView;

    ArrayList<HashMap<String, String>> pattern_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_daygraph);

        Intent intent = getIntent();
        studentId = intent.getIntExtra("id", 1);
        date_str = intent.getStringExtra("date");
        year_int = intent.getIntExtra("year",1);
        month_int = intent.getIntExtra("month",1);
        dayOfMonth_int = intent.getIntExtra("dayOfMonth",1);

        date_int = (year_int*10000)+(month_int*100)+dayOfMonth_int;

        textView = (TextView)findViewById(R.id.date);
        textView.setText(date_str);

        listView = (ListView) findViewById(R.id.pattern);

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        sleepList = new ArrayList<>();
        Cursor sleepCursor = db.rawQuery("select sleep, sleep_pattern, pattern_time from tb_daypattern where student_id=? AND day=?",
                new String[]{String.valueOf(studentId), String.valueOf(date_int)});

        while (sleepCursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            type_conversion = Integer.parseInt(sleepCursor.getString(0));
            hour = type_conversion / 100;
            minute = type_conversion % 100;
            string_time = Integer.toString(hour);
            map.put("sleep_hour", string_time);
            string_time = Integer.toString(minute);
            map.put("sleep_minute", string_time);

            map.put("sleep_pattern", sleepCursor.getString(1));

            type_conversion = Integer.parseInt(sleepCursor.getString(2));
            hour = type_conversion / 100;
            minute = type_conversion % 100;
            string_time = Integer.toString(hour);
            map.put("pattern_time_hour", string_time);
            string_time = Integer.toString(minute);
            map.put("pattern_time_minute", string_time);
            sleepList.add(map);
        }



        sa = new SimpleAdapter(this, sleepList, R.layout.day_list_item,
                new String[]{"sleep_hour","sleep_minute","sleep_pattern","pattern_time_hour","pattern_time_minute"},
                new int[]{R.id.day_sleep_hour,R.id.day_sleep_minute, R.id.patterntype,R.id.pattern_time_hour,R.id.patten_time_minute});

        listView.setAdapter(sa);
        db.close();

        setupPieChart();


    }

    private void setupPieChart(){

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        pattern_time = new ArrayList<>();
        Cursor patternCursor = db.rawQuery("select sleep_pattern, pattern_time from tb_daypattern where student_id=? AND day=?",
                new String[]{String.valueOf(studentId), String.valueOf(date_int)});

        String time_str;
        int hour_int;
        int minute_int;
        int time_int;

        List<PieEntry> pieEntries = new ArrayList<>();
        while(patternCursor.moveToNext()){
            time_str = patternCursor.getString(1);
            time_int = Integer.parseInt(time_str);
            if(time_int > 60) {
                hour_int = time_int / 100;
                minute_int = time_int % 100;
                time_int = (hour_int*60)+minute_int;
            }

            pieEntries.add(new PieEntry(time_int,patternCursor.getString(0)));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "단위 : 분");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        PieChart chart = (PieChart) findViewById(R.id.piegraph);
        chart.setData(data);
        chart.animateY(1000);
        chart.setCenterTextSize(20f);
        chart.invalidate();
    }


}