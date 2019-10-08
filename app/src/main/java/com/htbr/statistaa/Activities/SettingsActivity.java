package com.htbr.statistaa.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.ListPreference;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.htbr.statistaa.R;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            //final EditTextPreference textSizePreference = findPreference("textSizePreference");


            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.text_size_prefs), Context.MODE_PRIVATE);
            int textSize = sharedPreferences.getInt(getString(R.string.exercise_subtitle_textSize), 36);





//            textSizePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//                @Override
//                public boolean onPreferenceChange(Preference preference, Object newValue) {
//
//
//                    SharedPreferences sharedPreferences =  getActivity().getSharedPreferences(getString(R.string.text_size_prefs), Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    //System.out.println("asdlkfj........"+newValue);
//
//
//
//                    editor.putInt(getString(R.string.exercise_subtitle_textSize), Integer.parseInt(String.valueOf((newValue))));
//                    editor.commit();
//
//
//                    return false;
//                }
//            });

            final ListPreference listPreference = (ListPreference) findPreference("textSizeOnePreference");

            listPreference.setValue(String.valueOf(textSize));

            listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {


                    SharedPreferences sharedPreferences =  getActivity().getSharedPreferences(getString(R.string.text_size_prefs), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(getString(R.string.exercise_subtitle_textSize), Integer.parseInt(String.valueOf((newValue))));
                    editor.commit();

                    listPreference.setValue(String.valueOf(newValue));


                    return false;
                }
            });

        }




    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if ( item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}