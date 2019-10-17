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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.htbr.statistaa.Classes.FileWriter;
import com.htbr.statistaa.R;

import org.json.JSONException;
import org.json.JSONObject;


public class SettingsActivity extends AppCompatActivity {
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();

        user = FirebaseAuth.getInstance().getCurrentUser();



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

            sharedPreferences =  getActivity().getSharedPreferences(FirebaseAuth.getInstance().getCurrentUser().getUid() + getString(R.string.user_properties_JSON), Context.MODE_PRIVATE);
            String gender = sharedPreferences.getString(getString(R.string.genderID), "no");






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

            final ListPreference listPreferenceTextSize = (ListPreference) findPreference("textSizeOnePreference");

            listPreferenceTextSize.setValue(String.valueOf(textSize));

            listPreferenceTextSize.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {


                    SharedPreferences sharedPreferences =  getActivity().getSharedPreferences(getString(R.string.text_size_prefs), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(getString(R.string.exercise_subtitle_textSize), Integer.parseInt(String.valueOf((newValue))));
                    editor.apply();

                    listPreferenceTextSize.setValue(String.valueOf(newValue));


                    return false;
                }
            });


            final ListPreference listPreferenceGender = (ListPreference) findPreference("GenderPreference");


            listPreferenceGender.setValue(gender);

            listPreferenceGender.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    listPreferenceGender.setValue((String) newValue);


                    SharedPreferences sharedPreferences =  getActivity().getSharedPreferences(user.getUid() + getString(R.string.user_properties_JSON), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getString(R.string.genderID), newValue.toString());
                    editor.apply();


                    String statsJSONString = FileWriter.readFile(getContext(), user.getUid()+"_UserProps");

                    JSONObject jsonObject= new JSONObject();;

                    String key = "gender";

                    if (!statsJSONString.equals("{}")){
                        try {
                            jsonObject = new JSONObject(statsJSONString);



                            if(jsonObject.has(key)){
                                jsonObject.remove(key);
                                jsonObject.put(key, newValue.toString());
                            }

                            else{
                                jsonObject.put(key, newValue.toString());
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {


                        try {
                            jsonObject.put(key, newValue.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    FileWriter.writeNewToFile(getContext(), user.getUid()+"_UserProps", jsonObject.toString());


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