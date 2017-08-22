package com.example.user.sleep;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 2017-07-21.
 */

//메인 항목

public class MainListAdapter extends ArrayAdapter<StudentVO> {
    Context context;
    ArrayList<StudentVO> datas;
    int resId;

    public  MainListAdapter(Context context, int resId, ArrayList<StudentVO> datas){
        super(context, resId);
        this.context = context;
        this.datas = datas;
        this.resId = resId;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resId, null);

            MainListWrapper wrapper = new MainListWrapper(convertView);
            convertView.setTag(wrapper);
        }

        MainListWrapper wrapper= (MainListWrapper)convertView.getTag();
        ImageView studentImageView = wrapper.studentImageView;
        TextView nameView = wrapper.nameView;
        final  ImageView sleepView=wrapper.sleepView;

        final  StudentVO vo= datas.get(position);
        nameView.setText(vo.name);

        sleepView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               // if(vo.photo != null && !vo.phone.equals("")){
                    // 데이터 보는 화면.
                    Intent intent = new Intent();
                    intent.setClass(context,addstudentactivity.class);
                    intent.putExtra("id",datas.get(position).id);
                    context.startActivity(intent);
            }
        });

        return convertView;
    }

}
