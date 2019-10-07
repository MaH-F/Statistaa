package com.htbr.statistaa.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.htbr.statistaa.Adapters.QuestionAdapter;
import com.htbr.statistaa.Classes.FileWriter;
import com.htbr.statistaa.Classes.Questionnaire;
import com.htbr.statistaa.R;



public class QuestionnaireScrollingActivity extends AppCompatActivity {

    private static final String TAG = "QuestionnaireScrollingA";

    Questionnaire questionnaire;

    FirebaseUser user;


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    int mode;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(TAG + " onCreate ");
        setContentView(R.layout.activity_questionnaire_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout  = findViewById(R.id.collapsing_toolbar_layout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);



        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        user = FirebaseAuth.getInstance().getCurrentUser();

        // get JSON File

        questionnaire = (Questionnaire) getIntent().getSerializableExtra("Questionnaire");
        String jsonFileName = questionnaire.getId();


        //download file from storage or write new empty JSON to file or do nothing if JSON exists already
        initJSONFile();

        //now we should have a (JSON) file userUID_questtinnaireID for the current questionnaire


        mode = 0; //0 means edit-sign  > no edit mode  , 1 is edit mode (fab shows a save sign)



        //edit FAB
        final FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_EditSave);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == 0){
                    //change to edit-mode
                    mode = 1;
                    //floatingActionButton.setRippleColor(getResources().getColor(R.color.fab_save_background));
                    floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_save_icon));
                    System.out.println(mode);
                    showRecycler();

                    Toast.makeText(getApplicationContext(), getString(R.string.edit_mode_toast), Toast.LENGTH_LONG).show();

                } else if (mode == 1){
                    //save and change to non-edit mode
                    mode = 0;
                    //floatingActionButton.setRippleColor(getResources().getColor(R.color.fab_edit_background));
                    floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_icon));
                    System.out.println(mode);
                    showRecycler();

                    Toast.makeText(getApplicationContext(), getString(R.string.non_edit_mode), Toast.LENGTH_LONG).show();



                    // upload json
                   // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    FirebaseStorage storage = FirebaseStorage.getInstance("gs://statistaafrbs.appspot.com/");
                    StorageReference storageRef = storage.getReference();


                    String fileContent = FileWriter.readFile(getApplicationContext(), user.getUid() + "_" + questionnaire.getId());

                    //TODO:
                    //every user can write in every directory
                    //StorageReference riversRef = storageRef.child("carlos@mueslimann.de" + "/" + questionnaire.getId());


                    StorageReference riversRef = storageRef.child(user.getEmail()+ "/" + questionnaire.getId());
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
        });


        showRecycler();


    }

    private void initJSONFile() {

        final String localFilename = user.getUid() + "_" + questionnaire.getId();
        String storageFilename = user.getEmail() + "/" + questionnaire.getId();

       // String localFileContent = "";
        //String localFileContent = FileWriter.readFile(this, localFilename);
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://statistaafrbs.appspot.com/");
        StorageReference storageRef = storage.getReference();


        //if (localFileContent.equals("{}")){
            //download file if exists

        StorageReference riversRef = storageRef.child(storageFilename);
        final long ONE_MEGABYTE = 1024 * 1024;
        riversRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                FileWriter.writeBytesToNEWFile(getApplicationContext(), localFilename, bytes);
                Log.d(TAG, "wrote QuestionnaireJSON to local file");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                String exceptionMessage =  exception.getMessage();
                exception.printStackTrace();

                if (exceptionMessage.contains("Object does not exist at location.")){
                    //go on, this is the first time
                    Log.d(TAG, "Object does not exist, first Log In?");

                    //write empty JSON to localFile
                    //try to read local file

                    String localFileContent = FileWriter.readFile(getApplicationContext(), localFilename);
                    if (localFileContent.equals("{}")){
                        //this means that file didnt exist before, look into FrileWriter.readFile();

                        //so write a new empty JSON
                        FileWriter.writeNewToFile(getApplicationContext(), localFilename, "{}");
                    }
                    else {
                        // in this case a local file exists, but no file on the cloud storage
                        Log.d(TAG, "no JSON found on cloud storage but local file is here");
                    }





                }
                else{
                    exception.printStackTrace();
                }


            }
        });

//        }
//        else {
//            //local file exists
//            //TODO is file a JSON??
//            Log.d(TAG, "Local file is no emty JSON");
//        }







    }

    private void showRecycler() {

        Log.d(TAG, "reload recycler");


        CollapsingToolbarLayout collapsingToolbarLayout  = findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle(questionnaire.getName());





        Log.d(TAG, questionnaire.getQuestions().toString());








        recyclerView = (RecyclerView) findViewById(R.id.question_recycler);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new QuestionAdapter(questionnaire.getQuestions().toArray(new String[0]), questionnaire.getId(), this, mode);


        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if ( item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
