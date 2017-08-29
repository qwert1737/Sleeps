package com.example.user.sleep;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by user on 2017-08-14.
 */

public class Click_menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Integer i = item.getItemId();
        if(i==R.id.sleeping){
            Toast.makeText(this,"sleeping",Toast.LENGTH_SHORT).show();
            return true;
        }
        if(i==R.id.CCTV){
            Toast.makeText(this,"CCTV",Toast.LENGTH_SHORT).show();
            return true;
        }
        if(i==R.id.setting){
            Toast.makeText(this,"setting",Toast.LENGTH_SHORT).show();
            return true;
        }
        if(i==R.id.power){
            Toast.makeText(this,"power off",Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }


        return false;
    }

}
