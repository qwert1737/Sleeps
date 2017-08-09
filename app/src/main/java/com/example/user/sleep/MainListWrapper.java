package com.example.user.sleep;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by user on 2017-07-21.
 */

public class MainListWrapper {
    public ImageView studentImageView;
    public TextView nameView;
    public ImageView sleepView;

    public MainListWrapper(View root){
        studentImageView = (ImageView)root.findViewById(R.id.main_item_student_image);
        nameView = (TextView)root.findViewById(R.id.main_item_name);
        sleepView = (ImageView)root.findViewById(R.id.main_item_sleep_image);

    }

}
