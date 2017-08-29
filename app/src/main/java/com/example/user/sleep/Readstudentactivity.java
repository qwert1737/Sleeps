package com.example.user.sleep;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.lang.UCharacter;
import android.icu.text.SimpleDateFormat;
import android.icu.text.StringPrepParseException;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class Readstudentactivity extends AppCompatActivity {

    ImageView studentImage;
    TextView nameView;
    TextView phoneView;
    TextView emailView;

    int studentId;

    EditText DayGroup;
    EditText SleepGroup;
    EditText SleepPatternGroup;
    EditText PatternTimeGroup;
    EditText StartEndGroup;

    int sleep_time;
    int type_conversion;
    String start_end;
    String entire_time;

    int hour;
    int minute;


    ListView listView;
    ArrayList<HashMap<String, String>> sleepList;
    SimpleAdapter sa;


    ArrayList<StudentVO> datas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_readstudentactivity);

        Intent intent = getIntent();
        studentId = intent.getIntExtra("id", 1);

        studentImage = (ImageView) findViewById(R.id.read_student_image);
        nameView = (TextView) findViewById(R.id.read_name);
        phoneView = (TextView) findViewById(R.id.read_phone);
        emailView = (TextView) findViewById(R.id.read_email);

        listView = (ListView) findViewById(R.id.read_list);

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_student where _id=" + studentId, null);

        String photo = null;
        while (cursor.moveToNext()) {
            nameView.setText(cursor.getString(1));
            emailView.setText(cursor.getString(2));
            phoneView.setText(cursor.getString(3));
            photo = cursor.getString(4);
        }

        sleepList = new ArrayList<>();
        Cursor sleepCursor = db.rawQuery("select day, start_end, sleep_time from tb_sleep where student_id=?",
                new String[]{String.valueOf(studentId)});

        while (sleepCursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("day", sleepCursor.getString(0));
            start_end = sleepCursor.getString(1);
            map.put("start_end", start_end);
            entire_time = sleepCursor.getString(2);

            type_conversion = Integer.parseInt(entire_time);
            hour = type_conversion / 100;
            minute = type_conversion % 100;
            entire_time = Integer.toString(hour);
            map.put("sleep_time_hour", entire_time);

            entire_time = Integer.toString(minute);
            map.put("sleep_time_minute", entire_time);

            if(start_end.equals("End")) {
                sleepList.add(map);
            }
        }

        sa = new SimpleAdapter(this, sleepList, R.layout.read_list_item,
                new String[]{"day","sleep_time_hour", "sleep_time_minute"},
                new int[]{R.id.read_list_day,R.id.read_list_sleep_time_hour,R.id.read_list_sleep_time_minute});

        listView.setAdapter(sa);
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_read, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_read_sleep_add) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View root = inflater.inflate(R.layout.dialog_add_sleep, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Sleep ADD");

            DayGroup = (EditText)root.findViewById(R.id.dialog_day);
            SleepGroup = (EditText)root.findViewById(R.id.dialog_sleep);
            SleepPatternGroup = (EditText)root.findViewById(R.id.dialog_sleep_pattern);
            StartEndGroup = (EditText)root.findViewById(R.id.dialog_start_end);
            PatternTimeGroup = (EditText)root.findViewById(R.id.dialog_pattern_time);

            builder.setView(root);
            builder.setPositiveButton("ADD", dialogListener);
            builder.setNegativeButton("Cancel", null);

            builder.create().show();
        }

        return super.onOptionsItemSelected(item);
    }

    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String day = DayGroup.getText().toString();
            String sleep = SleepGroup.getText().toString();
            String sleep_pattern = SleepPatternGroup.getText().toString();
            String start_end = StartEndGroup.getText().toString();
            String pattern_time = PatternTimeGroup.getText().toString();

            int sleep_start = Integer.parseInt(pattern_time);

            if(start_end.equals("1")){
                start_end = "Start";
            }
            else if(start_end.equals("2")) {
                start_end = "End";
            }

            if (sleep_pattern.equals("1")){
                sleep_pattern = "얕은 수면";
            }
            else{
                sleep_pattern = "깊은 수면";
            }

            if(start_end.equals("Start")) {
                sleep_time = sleep_start;
            }
            else{
                if((sleep_time %100) + (sleep_start%100) > 59 ) {
                    sleep_time += sleep_start + 40;
                }
                else {
                    sleep_time += sleep_start;
                }
            }

            String sleeptime = Integer.toString(sleep_time);

            DBHelper helper = new DBHelper(Readstudentactivity.this);
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("insert into tb_sleep (student_id, day, sleep,  start_end, sleep_time) values (?, ?, ?, ?, ?)",
                   new String[]{String.valueOf(studentId), day, sleep, start_end, sleeptime});
            db.execSQL("insert into tb_daypattern (student_id, day, sleep, sleep_pattern, pattern_time) values (?, ?, ?, ?, ?)",
                    new String[]{String.valueOf(studentId),day, sleep, sleep_pattern, pattern_time});
            db.close();

            HashMap<String, String> map=new HashMap<>();
            map.put("day",day);
            map.put("sleep",sleep);
            map.put("start_end",start_end);
            map.put("sleep_time",sleeptime);
            sleepList.add(0,map);
            sa.notifyDataSetChanged();
        }
    };
}