package com.example.user.sleep;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 2017-07-20.
 */

public class DBHelper extends SQLiteOpenHelper{
    public static int DATABASE_VERSION = 15;

    public DBHelper(Context context){
        super(context, "studentdb",null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String studentSql = "create table tb_student ("+
                "_id integer primary key autoincrement, "+
                "name not null, " +
                "email, "+
                "phone, "+
                "photo)";



        String sleepSql = "create table tb_sleep (" +
                "_id integer primary key autoincrement, "+
                "student_id not null, "+
                "day, "+
                "sleep, "+
                "start_end, "+
                "sleep_time,"+
                "sleep_time_hour,"+
                "sleep_time_minute)";

        String timeSql = "create table tb_daypattern (" +
                "_id integer primary key autoincrement, "+
                "student_id not null, "+
                "day, "+
                "sleep, "+
                "sleep_hour, "+
                "sleep_minute, "+
                "sleep_pattern, "+
                "pattern_time,"+
                "pattern_hour, "+
                "pattern_minute)";


        db.execSQL(studentSql);
        db.execSQL(sleepSql);
        db.execSQL(timeSql);
    }

    @Override  // 디비 업그레이드
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if(newVersion == DATABASE_VERSION){
            db.execSQL("drop table tb_student");
            db.execSQL("drop table tb_sleep");
            db.execSQL("drop table tb_daypattern");

            onCreate(db);
        }
    }


}
