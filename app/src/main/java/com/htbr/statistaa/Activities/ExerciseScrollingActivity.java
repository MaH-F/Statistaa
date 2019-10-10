package com.htbr.statistaa.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;

import android.view.MenuItem;
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
    int group = -1;
    int index = -1;


    long startTime;
    long stopTime;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout  = findViewById(R.id.collapsing_toolbar_layout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);



        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


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


        String callingActivity = (String) getIntent().getSerializableExtra("CallingActivity");







        String exerciseID = exercise.getId();

        for(int i = 0; i < mySelectedExercisesJSONArray.length(); i++){
            try {
                exerciseJSON = (JSONObject) mySelectedExercisesJSONArray.get(i);
                detailsJSON = (JSONObject) exerciseJSON.get("exercise");
                if(detailsJSON.get(getString(R.string.jsonparam_exercise_id)).equals(exerciseID)){

                    //isAlreadyArchived = 1;
                    group = (int) detailsJSON.get(getString(R.string.jsonparam_exercise_group));

                    Log.d(TAG, "Group of exercise " + exerciseID + " is " + group);

                    index = i;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        //if ((callingActivity.equals("ExerciseRecyclerActivity") && exercise.getBox() != -1) || exercise.getBox() == 2){
        if ((callingActivity.equals("ExerciseRecyclerActivity") && group != -1) || exercise.getBox() == 2){
            floatingActionButton.setVisibility(View.INVISIBLE);
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //JSONArray jsonArray= new JSONArray();
                JSONObject detais = new JSONObject();
                JSONObject exerciseJSON = new JSONObject();

                // to guarantee that a user can push an exercise only one time to another box/group
                if (isAlreadyArchived == 0) {
                    // in witch group is our archive
                    switch (group) {
                        // exercise is not archived, does not exist in JSON
                        case -1:
                            Log.d(TAG, "Exercise is not in a group");

                            Toast.makeText(getApplicationContext(), getString(R.string.addToArchve), Toast.LENGTH_LONG).show();


                            try {

                                if(index != -1) {

                                    mySelectedExercisesJSONArray.remove(index);

                                }

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


                            break;
                        // exercise is in group 0
                        case 0:
                            Log.d(TAG, "Exercise is in group 0 push it to group 1");

                            Toast.makeText(getApplicationContext(), getString(R.string.moveToBox2), Toast.LENGTH_LONG).show();


                            try {
                                if(index != -1) {

                                    mySelectedExercisesJSONArray.remove(index);

                                }
                                detais.put("id", exercise.getId());
                                detais.put("group", 1);


                                exerciseJSON.put("exercise", detais);
                                mySelectedExercisesJSONArray.put(exerciseJSON);

                                isAlreadyArchived = 1;


                            } catch (JSONException e) {
                                e.printStackTrace();


                            }
                            FileWriter.writeNewToFile(getApplicationContext(), user.getUid() + getString(R.string.mySelectedExerciseJSON), mySelectedExercisesJSONArray.toString());


                            //save exercise to file
                            exercise.setBox(1);
                            FileWriter.writeObjectToFile(getApplicationContext(), exercise);


                            break;
                        case 1:
                            Log.d(TAG, "Exercise is in group 0 push it to group 2");


                            Toast.makeText(getApplicationContext(), getString(R.string.moveToBox3), Toast.LENGTH_LONG).show();


                            try {
                                if(index != -1) {

                                    mySelectedExercisesJSONArray.remove(index);

                                }
                                detais.put("id", exercise.getId());
                                detais.put("group", 2);


                                exerciseJSON.put("exercise", detais);
                                mySelectedExercisesJSONArray.put(exerciseJSON);

                                isAlreadyArchived = 1;


                            } catch (JSONException e) {
                                e.printStackTrace();


                            }
                            FileWriter.writeNewToFile(getApplicationContext(), user.getUid() + getString(R.string.mySelectedExerciseJSON), mySelectedExercisesJSONArray.toString());


                            //save exercise to file
                            exercise.setBox(2);
                            FileWriter.writeObjectToFile(getApplicationContext(), exercise);


                            break;
                        case 2:
                            //What to do ??
                            Log.d(TAG, "Exercise is in group 2.");

                            Toast.makeText(getApplicationContext(), getString(R.string.alreadyInArchive), Toast.LENGTH_LONG).show();
                            // but if file does not exists save it
                            if (FileWriter.exists(getApplicationContext(), exercise.getId()) == 0) {
                                Log.d(TAG, "File does not exist but is in group 2");
                                exercise.setBox(0);
                                FileWriter.writeObjectToFile(getApplicationContext(), exercise);
                            }
                            break;
                    }

                }





            }




        });



    }



    @Override
    protected void onResume(){
        super.onResume();

        startTime = System.currentTimeMillis();


    }


    protected void onPause(){
        super.onPause();

        stopTime = System.currentTimeMillis() - startTime;

        SharedPreferences sharedPreferences = this.getSharedPreferences(user.getUid()+"_ExerciseStats", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        long totalTime = sharedPreferences.getLong(exercise.getId()+"_totalTime", 0);

        totalTime = totalTime + stopTime;


        editor.putLong(exercise.getId()+"_totalTime", totalTime);
        editor.apply();



        String statsJSONString = FileWriter.readFile(this, user.getUid()+"_ExerciseStats");

        JSONObject statsJSON= new JSONObject();;

        if (!statsJSONString.equals("{}")){
            try {
                statsJSON = new JSONObject(statsJSONString);

                if(statsJSON.has(exercise.getId()+"_totalTime")){
                    statsJSON.remove(exercise.getId()+"_totalTime");
                    statsJSON.put(exercise.getId()+"_totalTime", totalTime);
                }

                else{
                    statsJSON.put(exercise.getId()+"_totalTime", totalTime);
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {


            try {
                statsJSON.put(exercise.getId()+"_totalTime", totalTime);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        FileWriter.writeNewToFile(this, user.getUid()+"_ExerciseStats", statsJSON.toString());



    }



        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            if ( item.getItemId() == android.R.id.home){
                finish();
            }

            return super.onOptionsItemSelected(item);
        }
}
