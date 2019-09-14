package com.htbr.statistaa.Activities;

import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.htbr.statistaa.R;
import com.htbr.statistaa.Classes.Exercise;
import com.htbr.statistaa.Classes.FileWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ExerciseScrollingActivity extends AppCompatActivity {

    FirebaseUser user;
    private static final String TAG = "ExerciseScrollingAy";
    static JSONObject mySelectedExercisesJSON;

    Exercise exercise;

    int isAlreadyArchived = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout  = findViewById(R.id.collapsing_toolbar_layout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        user = FirebaseAuth.getInstance().getCurrentUser();

        exercise = (Exercise) getIntent().getSerializableExtra("Exercise");

        collapsingToolbarLayout.setTitle(exercise.getSubtitle());

        final String archive = getString(R.string.archive_JSON_ARRAY_key);


        TextView textView = findViewById(R.id.exercise_body_text);
        textView.setText(exercise.getContent());


        Log.d(TAG, "Exercise " + exercise.getName());


        try {
            //File file = new File(getString(R.string.mySelectedExerciseJSON));
            //if(file.exists()){
            mySelectedExercisesJSON = new JSONObject(FileWriter.readFile(this, user.getUid() + getString(R.string.mySelectedExerciseJSON)));


        } catch (JSONException e) {
            e.printStackTrace();
            mySelectedExercisesJSON = new JSONObject();

        }


        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_archieveFab);


        if (mySelectedExercisesJSON.has(archive)){
            try {
                JSONArray archived = (JSONArray) mySelectedExercisesJSON.getJSONArray(archive);
                for (int i = 0; i < archived.length(); i++) {
                    if (exercise.getId().equals(archived.getString(i))) {
                        isAlreadyArchived = 1;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isAlreadyArchived == 0) {

                    Toast.makeText(getApplicationContext(), getString(R.string.addToArchve), Toast.LENGTH_LONG).show();


                    Log.d(TAG, "Select exercise " + exercise.getId());
                    ;
                    try {


                        JSONArray jsonArray;
                        if (mySelectedExercisesJSON.has(archive)) {

                            jsonArray = mySelectedExercisesJSON.getJSONArray(archive);

                        } else {
                            jsonArray = new JSONArray();

                        }
                        jsonArray.put(exercise.getId());

                        mySelectedExercisesJSON.put(archive, jsonArray);
                        isAlreadyArchived = 1;


                    } catch (JSONException e) {
                        e.printStackTrace();


                    }
                    FileWriter.writeNewToFile(getApplicationContext(), user.getUid() + getString(R.string.mySelectedExerciseJSON), mySelectedExercisesJSON.toString());

                }
                else {
                    Toast.makeText(getApplicationContext(), getString(R.string.alreadyInArchive), Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}
