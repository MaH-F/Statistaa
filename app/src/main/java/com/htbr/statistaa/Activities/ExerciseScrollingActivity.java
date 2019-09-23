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


    static JSONArray mySelectedExercisesJSONArray;
    JSONObject exerciseJSON;
    JSONObject detailsJSON;

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




        TextView textView = findViewById(R.id.exercise_body_text);
        textView.setText(exercise.getContent());


        Log.d(TAG, "Exercise " + exercise.getName());


        try {


            mySelectedExercisesJSONArray = new JSONArray(FileWriter.readFile(this, user.getUid() + getString(R.string.mySelectedExerciseJSON)));


        } catch (JSONException e) {
            e.printStackTrace();


            mySelectedExercisesJSONArray = new JSONArray();

        }


        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_archieveFab);


        String exerciseID = exercise.getId();
        int group = -1;
        for(int i = 0; i < mySelectedExercisesJSONArray.length(); i++){
            try {
                exerciseJSON = (JSONObject) mySelectedExercisesJSONArray.get(i);
                detailsJSON = (JSONObject) exerciseJSON.get("exercise");
                if(detailsJSON.get(getString(R.string.jsonparam_exercise_id)).equals(exerciseID)){

                    isAlreadyArchived = 1;
                    group = (int) detailsJSON.get(getString(R.string.jsonparam_exercise_group));

                    Log.d(TAG, "Group of exercise " + exerciseID + " is " + group);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //JSONArray jsonArray= new JSONArray();
                JSONObject detais = new JSONObject();
                JSONObject exerciseJSON = new JSONObject();

                // Exercise is not in archive
                if (isAlreadyArchived == 0) {

                    Toast.makeText(getApplicationContext(), getString(R.string.addToArchve), Toast.LENGTH_LONG).show();


                    Log.d(TAG, "Select exercise " + exercise.getId());

                    try {




                        detais.put("id", exercise.getId());
                        detais.put("group", 0);



                        exerciseJSON.put("exercise", detais);
                        mySelectedExercisesJSONArray.put(exerciseJSON);






                        isAlreadyArchived = 1;




                    } catch (JSONException e) {
                        e.printStackTrace();


                    }
                    FileWriter.writeNewToFile(getApplicationContext(), user.getUid() + getString(R.string.mySelectedExerciseJSON), mySelectedExercisesJSONArray.toString());


                    //save exercise to file
                    exercise.setBox(0);
                    FileWriter.writeObjectToFile(getApplicationContext(), exercise);




                }
                else {
                    Toast.makeText(getApplicationContext(), getString(R.string.alreadyInArchive), Toast.LENGTH_LONG).show();


                    // but if file does not exists save it
                    if(FileWriter.exists(getApplicationContext(), exercise.getId()) == 0) {
                        exercise.setBox(0);
                        FileWriter.writeObjectToFile(getApplicationContext(), exercise);
                    }
                }
            }
        });



    }
}
