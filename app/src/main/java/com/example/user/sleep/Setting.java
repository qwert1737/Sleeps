package com.example.user.sleep;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by user on 2017-08-14.
 */

public class Setting extends PreferenceActivity{

    String Name = "null";
    String Age = "null";
    public static String imformation = "수면분석 슬립타이트 V1.0.0";
    String Alram = "null";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,new MyPreferenceFragment()).commit();
    }

    public class MyPreferenceFragment extends PreferenceFragment{
        @Override
        public void onCreate(final Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.setting);


            DBHelper helper = new DBHelper(Setting.this);
            final SQLiteDatabase db = helper.getWritableDatabase();

            final EditTextPreference User_name = (EditTextPreference)findPreference("name");
            final EditTextPreference User_age = (EditTextPreference)findPreference("age");
            final Preference imfor = (Preference)findPreference("imformation");

            Name = User_name.getText();
            Age = User_age.getText();

            db.execSQL("delete from tb_student");
            db.execSQL("insert into tb_student (name, email) values (?,?)", new String[]{Name,Age});
            db.close();

            imfor.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Toast.makeText(Setting.this, imformation,
                            Toast.LENGTH_SHORT).show();


                    return true;
                }
            });


            final SwitchPreference User_Alram = (SwitchPreference)findPreference("alram");

            final String onAlram = null;
            Alram = (String)User_Alram.getSwitchTextOn();
            User_Alram.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if(String.valueOf(newValue) == "true"){
                        Alram = (String) User_Alram.getSwitchTextOn();
                    }
                    else{
                        Alram = (String) User_Alram.getSwitchTextOff();
                    }

                    Toast.makeText(Setting.this, Alram, Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }
    }


}
