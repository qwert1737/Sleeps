package com.example.user.sleep;

import android.app.job.JobService;
import android.content.ClipData;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 2017-07-22.
 */


// 서버에서 데이터를 전송 받을 때
public class Receivejson {

    RecyclerView recyclerView;
    LinearLayout objectResultLo;

    ArrayList<ClipData.Item> mItems = new ArrayList<>();
    RecyclerView.Adapter adapter;

    TextView mReceiveTV;
    TextView mReceivedayTV;
    TextView mReceivesleep_timeTv;
    TextView mReceivesleep_patternTv;
    TextView mReceivestart_end;
    TextView mReceicepattern_time;




    private void receiveObject(JSONObject data){
        recyclerView.setVisibility(View.GONE);
        objectResultLo.setVisibility(View.VISIBLE);

        try{
            mReceiveTV.setText(data.toString());
            mReceivedayTV.setText("day : "+data.getString("day"));
            mReceivesleep_timeTv.setText("sleep : "+data.getString("sleep"));
            mReceivesleep_patternTv.setText("sleep_pattern : "+data.getString("sleep_pattern"));
            mReceivestart_end.setText("start_end : "+data.getString("start_end"));
            mReceicepattern_time.setText("pattern_time : "+data.getString("pattern_time"));
        } catch (JSONException e){
            e.printStackTrace();
        }
    }



}
