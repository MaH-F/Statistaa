package com.htbr.statistaa.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.htbr.statistaa.classes.Exercise;
import com.htbr.statistaa.R;
import com.htbr.statistaa.classes.FileWriter;
import com.htbr.statistaa.classes.UserHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RootActivity extends AppCompatActivity {

    private static final String TAG = "RootActivity";
    // user is not "static" because after logging out, it still will be the "old" user
    FirebaseUser user;
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference riversRef_SelectedExercises;
    StorageReference riversRef_UserStats;
    StorageReference riverseRef_UserProps;
    StorageReference riversRef_Solutions;

    UserHandler userHandler;

    long group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check if user has accepted the Terms
        user = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(user.getUid()+"_terms", Context.MODE_PRIVATE);
        boolean accepted = sharedPreferences.getBoolean("accepted", false);


        if(!accepted){
            FirebaseAuth.getInstance().signOut();
            if (FirebaseAuth.getInstance().getCurrentUser() == null){
                Intent intent = new Intent(this, LoginActivity.class);
                //this (hopefully destroys all other activities)
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            else {
                Log.e("Error","RootActivityLogoutError");
            }
        }


        // check if this is the first start. if yes: call Tutorial Activity


        SharedPreferences firstStartPrefs = getApplicationContext().getSharedPreferences(user.getUid()+"_firstStart", Context.MODE_PRIVATE);
        boolean isFirstStart = firstStartPrefs.getBoolean("isFirstStart", true);

        if (isFirstStart){

            SharedPreferences.Editor isFirstStartEditor = firstStartPrefs.edit();
            isFirstStartEditor.putBoolean("isFirstStart", false);
            isFirstStartEditor.apply();


            startActivity(new Intent(this, TutorialActivity.class));
        }



        setContentView(R.layout.activity_root);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ImageButton learnButton = findViewById(R.id.learnbutton);
        learnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RootActivity.this, ExercisesRecyclerActivity.class));
            }
        });

        ImageButton myProfileButton = findViewById(R.id.profilebutton);
        myProfileButton .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RootActivity.this, MyProfileActivity.class));
            }
        });

        ImageButton feedbackButton = findViewById(R.id.feedbackbutton);
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RootActivity.this, QuestionnairesRecyclerActivity.class));
            }
        });

        ImageButton archiveButton = findViewById(R.id.archivebutton);
        archiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RootActivity.this, ArchiveActivity.class));
            }
        });


    }


    @Override
    protected void onStart(){
        super.onStart();


        storage = FirebaseStorage.getInstance("gs://statistaafrbs.appspot.com/");
        storageRef = storage.getReference();
        riversRef_SelectedExercises = storageRef.child(user.getEmail()+"/selectedExercises.txt");
        riversRef_UserStats = storageRef.child(user.getEmail()+"/StorageStats.txt");
        riverseRef_UserProps = storageRef.child(user.getEmail()+"/UserProps.txt");
        riversRef_Solutions = storageRef.child(user.getEmail()+"/Solutions.json");


        userHandler = new UserHandler(this);
        userHandler.setGroupIDListener(new UserHandler.GroupIDListener() {
            @Override
            public void onGotGroup() {
                // check if stored exercise-filies exist, if not download them.
                Log.d(TAG, "group is " + userHandler.getUserGroup(user));


                //TODO: download every time?? Maybe work with Listeners (OnDataChanged -> upload) and download every time...?
                String fileContent = FileWriter.readFile(getApplicationContext(), user.getUid() + getString(R.string.mySelectedExerciseJSON));


                // if no file exists...
                 if (fileContent.equals("{}")) {
                     Log.d(TAG, "file " + user.getUid() + getString(R.string.mySelectedExerciseJSON) + " is not in storage. Download it. ");
                     downloadSelectedExercisesJSONFile();
                 }
                 else {
                     //load data up
                     uploadSelectedExercises(fileContent, riversRef_SelectedExercises);
                 }




            }
        });

        userHandler.setUserGroup(this, user);


        // upload USER STATS JSON


        String stats = FileWriter.readFile(this, user.getUid()+"_ExerciseStats");
         if (!stats.equals("{}")){
            uploadSelectedExercises(stats, riversRef_UserStats);
        }






        String props = FileWriter.readFile(this, user.getUid()+"_UserProps");
        if (!props.equals("{}")) {
            uploadSelectedExercises(props, riverseRef_UserProps);
        }




        String solutions = FileWriter.readFile(this, user.getUid() + getString(R.string.mySolutions));
        if (!solutions.equals("{}")) {
            uploadSelectedExercises(solutions, riversRef_Solutions);
        }
    }

    private void downloadSelectedExercisesJSONFile() {

        //if we have no json,


        final long ONE_MEGABYTE = 1024 * 1024;
        riversRef_SelectedExercises.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                FileWriter.writeBytesToFile(getApplicationContext(), user.getUid()+getString(R.string.mySelectedExerciseJSON), bytes);

               // then download our saved exercise-Files (if they not exist)
                downloadSavedExercises();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                String exceptionMessage =  exception.getMessage();

                if (exceptionMessage.contains("Object does not exist at location.")){
                    //go on, this is the first time
                    Log.d(TAG, "Object does not exist, first Log In?");
                }
                else{
                    exception.printStackTrace();
                }


            }
        });
    }



    private void uploadSelectedExercises(String fileContent, StorageReference storageRef){
        //if we have no json,


        UploadTask uploadTask = storageRef.putBytes(fileContent.getBytes());

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
    }


    public void downloadSavedExercises(){
        group = userHandler.getUserGroup(user);





        try {
            JSONArray mySelectetExerciseJSONArray = new JSONArray(FileWriter.readFile(this, user.getUid()+  getString(R.string.mySelectedExerciseJSON)));



            JSONObject exerciseJSON;
            JSONObject detailsJSON;
            for (int i = 0; i < mySelectetExerciseJSONArray.length(); i++){
                final Exercise exercise;
                exerciseJSON = (JSONObject) mySelectetExerciseJSONArray.get(i);
                detailsJSON = (JSONObject) exerciseJSON.get("exercise");
                //TODO: if a string is in the json that is not a file, what to do?
                String exerciseFileName = detailsJSON.getString(getString(R.string.jsonparam_exercise_id));




                //TODO thread or service?
                //
                // if file does not exists
                if  ( FileWriter.exists(this,exerciseFileName) == 0 ){
                    Log.d(TAG, "File " + exerciseFileName + " does not exist so try to load it from database!");


                    final FirebaseFirestore db;
                    db = FirebaseFirestore.getInstance();




                    String collection = "";

                    if(group == 1){
                        collection = "ExercisesA";
                        Log.w(TAG, "Group is "+ group + "collection is "+ collection);
                    } else if (group == 2){
                        collection = "ExercisesB";
                        Log.w(TAG, "Group is "+ group + "collection is "+ collection);
                    } else {
                        Log.d(TAG, "user has no group");
                    }
                    final DocumentReference docRef = db.collection(collection).document(detailsJSON.getString("id"));
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Exercise downloadedExercise = document.toObject(Exercise.class);

                                    downloadedExercise.setId(document.getId());




                                    FileWriter.writeObjectToFile(getApplicationContext(), downloadedExercise);



                                    //editor.apply();
                                    Log.d(TAG," was successfull for File " + downloadedExercise.getId());

                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }

                    });




                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.root_menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                if (FirebaseAuth.getInstance().getCurrentUser() == null){
                    Intent intent = new Intent(this, LoginActivity.class);
                    //this (hopefully destroys all other activities)
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else {
                    Log.e("Error","RootActivityLogoutError");
                }
                return true;


            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;

            case R.id.settingsActivity:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            case R.id.tutorialActivity:
                startActivity(new Intent(this, TutorialActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
