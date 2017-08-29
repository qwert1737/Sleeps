package com.example.user.sleep;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.NumberFormat;
import android.icu.util.Calendar;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by user on 2017-08-14.
 */

@RequiresApi(api = Build.VERSION_CODES.N)
public class Setting extends PreferenceActivity{

    String Name = "null";
    String Age = "null";
    public static String information = "수면분석 슬립타이트 V1.0.0";
    String Alram = "null";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,new MyPreferenceFragment()).commit();
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                if (TextUtils.isEmpty(stringValue)) {

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        preference.setSummary(null);
                    } else {
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else if (preference instanceof EditTextPreference) {
                preference.setSummary(stringValue);
            }else {
                preference.setSummary(String.valueOf(value));
            }
            return true;
        }
    };


    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    public class MyPreferenceFragment extends PreferenceFragment{
        @Override
        public void onCreate(final Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.setting);

            final EditTextPreference User_name = (EditTextPreference)findPreference("name");
            final EditTextPreference User_age = (EditTextPreference)findPreference("age");
            final Preference imfor = (Preference)findPreference("information");

            Name = User_name.getText();
            Age = User_age.getText();

            bindPreferenceSummaryToValue(findPreference("name"));
            bindPreferenceSummaryToValue(findPreference("age"));
            bindPreferenceSummaryToValue(findPreference("sex"));


            imfor.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Toast.makeText(Setting.this, information,
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
