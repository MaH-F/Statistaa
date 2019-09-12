package com.htbr.statistaa.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.NonNull;
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
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    FirebaseStorage storage = FirebaseStorage.getInstance("gs://statistaafrbs.appspot.com/");
                    StorageReference storageRef = storage.getReference();


                    String fileContent = FileWriter.readFile(getApplicationContext(), questionnaire.getId());

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

    private void showRecycler() {

        Log.d(TAG, "reload recycler");

        questionnaire = (Questionnaire) getIntent().getSerializableExtra("Questionnaire");
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


}
