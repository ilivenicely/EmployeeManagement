package com.example.employee.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.preference.PreferenceActivity;


import com.example.employee.R;
import com.example.employee.core.DbHelper;

public class SettingActivity extends PreferenceActivity {

    DbHelper dbHelper;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ListPreference listPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper=new DbHelper(SettingActivity.this);

        /*create sharedPreference*/
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        editor=sharedPreferences.edit();
        String dateformat = sharedPreferences.getString("dateformat","MMM dd, yyyy");

        /*manage listPreference*/
        addPreferencesFromResource(R.xml.pref_setting);
        listPreference=(ListPreference)findPreference("PREF");
        listPreference.setValue(dateformat);

        /*preference for dateformat*/
        findPreference("PREF").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                editor.putString("dateformat",newValue.toString());
                editor.apply();
                Intent intent=new Intent(SettingActivity.this,ListActivity.class);
                startActivity(intent);
                return false;
            }
        });

        /*preference for delete dialog*/
        findPreference("PREF_DELETE").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("Delete All");
                builder.setMessage("Are you sure you want to delete all stored employees from the device ?");
                builder.setPositiveButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(SettingActivity.this,ListActivity.class);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("Delete All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteAll();

                        Intent intent=new Intent(SettingActivity.this,ListActivity.class);
                        startActivity(intent);
                    }
                });

                AlertDialog dialog=builder.create();
                dialog.show();

                return false;
            }
        });
    }
}