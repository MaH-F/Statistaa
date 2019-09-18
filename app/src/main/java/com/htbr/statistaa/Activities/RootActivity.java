package com.htbr.statistaa.Activities;

import android.content.Intent;
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
import com.htbr.statistaa.Classes.Exercise;
import com.htbr.statistaa.R;
import com.htbr.statistaa.Classes.FileWriter;
import com.htbr.statistaa.Classes.UserHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RootActivity extends AppCompatActivity {

    private static final String TAG = "RootActivity";
    // user is not "static" because after logging out, it still will be the "old" user
    FirebaseUser user;

    long group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);













/*        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

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
        user = FirebaseAuth.getInstance().getCurrentUser();
        UserHandler.setUsergroup(this, user);




        String fileContent = FileWriter.readFile(this, user.getUid()+getString(R.string.mySelectedExerciseJSON));
        //String fileContent = FileWriter.readFile(this, "questionnaire_1");

        //if we have no json,
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://statistaafrbs.appspot.com/");
        StorageReference storageRef = storage.getReference();


        if (fileContent.equals("{}")){
            //download file if exists

            StorageReference riversRef = storageRef.child(user.getEmail()+"/selectedExercises.txt");
            final long ONE_MEGABYTE = 1024 * 1024;
            riversRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Data for "images/island.jpg" is returns, use this as needed
                    FileWriter.writeBytesToFile(getApplicationContext(), user.getUid()+getString(R.string.mySelectedExerciseJSON), bytes);

                    //downloadSavedExercises();

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

        else {
            //load data up


            StorageReference riversRef = storageRef.child(user.getEmail()+"/selectedExercises.txt");
            UploadTask uploadTask = riversRef.putBytes(fileContent.getBytes());

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






    }


    public void downloadSavedExercises(){
        group = UserHandler.getUsergroup(this, user);


        while (group == 0){
            try {
                Thread.sleep(2000);
                Log.d(TAG, "Usergroup is still default");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


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
                    Log.d(TAG, "File " + exerciseFileName + " does not exist so try to load it form database!");


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
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
