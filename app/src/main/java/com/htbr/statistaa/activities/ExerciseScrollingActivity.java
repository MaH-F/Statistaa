package com.htbr.statistaa.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.htbr.statistaa.R;
import com.htbr.statistaa.classes.Exercise;
import com.htbr.statistaa.classes.FileWriter;

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



        try {


            mySelectedExercisesJSONArray = new JSONArray(FileWriter.readFile(this, user.getUid() + getString(R.string.mySelectedExerciseJSON)));


        } catch (JSONException e) {
            e.printStackTrace();


            mySelectedExercisesJSONArray = new JSONArray();

        }



        String html = "";

//        TextView textView = findViewById(R.id.exercise_body_text);
//        textView.setText(exercise.getContent());



        if(exercise.getHtml() != null){
            html = exercise.getHtml();

            WebView webView = (WebView) findViewById(R.id.exercise_html);
            webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);

        }


        if(exercise.getSolution() != null){
            final EditText solutionEditText = (EditText) findViewById(R.id.exercise_editText_solution);
            solutionEditText.setVisibility(View.VISIBLE);

            final Button sendSolutionButton = (Button) findViewById(R.id.exercise_sendSolutionButton);
            sendSolutionButton.setVisibility(View.VISIBLE);

            sendSolutionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                   if(solutionEditText.getText().toString().equals(exercise.getSolution())){
                       Toast.makeText(ExerciseScrollingActivity.this, getString(R.string.solutionRight), Toast.LENGTH_SHORT).show();


                       writeToJson(1);


                   }

                   else {
                       Toast.makeText(ExerciseScrollingActivity.this, getString(R.string.solutionFalse), Toast.LENGTH_SHORT).show();

                       writeToJson(0);
                   }

                }
            });
        }


        Log.d(TAG, "Exercise " + exercise.getName());




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
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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



    public void writeToJson(int inputInteger){

        JSONArray solutionsArray;
        JSONObject exJson = new JSONObject();
        JSONObject detJson = new JSONObject();


        int numberOfTrueSolutions;
        int numberOfFalseSolutions;


        try {


            String readData = FileWriter.readFile(this, user.getUid() + getString(R.string.mySolutions));

            if (readData.equals("{}")){
                solutionsArray = new JSONArray();
            }

            else {

                solutionsArray = new JSONArray(readData);

            }



        } catch (JSONException e) {
            e.printStackTrace();


            solutionsArray = new JSONArray();

        }



        //read from JSON

        String exerciseID = exercise.getId();

        for(int i = 0; i < solutionsArray.length(); i++){
            try {
                exJson = (JSONObject) solutionsArray.get(i);

                detJson = (JSONObject) exJson.get("exercise");
                if(detJson.get(getString(R.string.jsonparam_exercise_id)).equals(exerciseID)){

                   solutionsArray.remove(i);

                    if (inputInteger == 1){

                        if (detJson.has(getString(R.string.jsonparam_exercise_numberOfTrueSolutions))){
                            numberOfTrueSolutions = (int) detJson.get(getString(R.string.jsonparam_exercise_numberOfTrueSolutions));

                            detJson.remove(getString(R.string.jsonparam_exercise_numberOfTrueSolutions));
                            detJson.put(getString(R.string.jsonparam_exercise_numberOfTrueSolutions), numberOfTrueSolutions + 1);

                        }
                        else {
                            detJson.put(getString(R.string.jsonparam_exercise_numberOfTrueSolutions), 1);
                        }

                    }


                    else{
                        if (detJson.has(getString(R.string.jsonparam_exercise_numberOfFalseSolutions))){
                            numberOfFalseSolutions = (int) detJson.get(getString(R.string.jsonparam_exercise_numberOfFalseSolutions));

                            detJson.remove(getString(R.string.jsonparam_exercise_numberOfFalseSolutions));
                            detJson.put(getString(R.string.jsonparam_exercise_numberOfFalseSolutions), numberOfFalseSolutions + 1);

                        }
                        else {
                            detJson.put(getString(R.string.jsonparam_exercise_numberOfFalseSolutions), 1);
                        }
                    }

                    detJson.put(getString(R.string.jsonparam_exercise_id), exerciseID);
                    exJson.put("exercise", detJson);
                    solutionsArray.put(exJson);


                    FileWriter.writeNewToFile(this, user.getUid() + getString(R.string.mySolutions) ,solutionsArray.toString() );

                    return;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        // if there is no json...

        try {

        Log.d(TAG, "write a new json");

        detJson = new JSONObject();
        exJson = new JSONObject();

        if(inputInteger == 1){
            detJson.put(getString(R.string.jsonparam_exercise_numberOfTrueSolutions), 1);
        }
        else {
            detJson.put(getString(R.string.jsonparam_exercise_numberOfFalseSolutions), 1);
        }

        detJson.put(getString(R.string.jsonparam_exercise_id), exerciseID);

        exJson.put("exercise", detJson);
        solutionsArray.put(exJson);

        FileWriter.writeNewToFile(this, user.getUid() + getString(R.string.mySolutions) ,solutionsArray.toString() );

        } catch (JSONException e) {
                e.printStackTrace();
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
