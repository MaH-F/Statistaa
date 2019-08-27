package com.htbr.statistaa.ui.login;

import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;

import android.widget.TextView;

import com.htbr.statistaa.R;

public class ExerciseScrollingActivity extends AppCompatActivity {


    private static final String TAG = "ExerciseScrollingAc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Exercise exercise = (Exercise) getIntent().getSerializableExtra("Exercise");

        TextView textView = findViewById(R.id.exercise_body_text);
        textView.setText(exercise.getContent());

        Log.d(TAG, "Exercise " + exercise.getName());


    }
}
