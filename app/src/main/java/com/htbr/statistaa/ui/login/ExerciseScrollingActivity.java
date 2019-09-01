package com.htbr.statistaa.ui.login;

import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.htbr.statistaa.R;

public class ExerciseScrollingActivity extends AppCompatActivity {


    private static final String TAG = "ExerciseScrollingAc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout  = findViewById(R.id.collapsing_toolbar_layout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);



        Exercise exercise = (Exercise) getIntent().getSerializableExtra("Exercise");

        collapsingToolbarLayout.setTitle(exercise.getSubtitle());


        TextView textView = findViewById(R.id.exercise_body_text);
        textView.setText(exercise.getContent());


        Log.d(TAG, "Exercise " + exercise.getName());


        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_archieveFab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getString(R.string.addToArchve), Toast.LENGTH_LONG).show();
                //TODO
            }
        });



    }
}
