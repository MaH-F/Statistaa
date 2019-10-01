package com.htbr.statistaa.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.htbr.statistaa.Interfaces.OnExerciseSelectedListener;
import com.htbr.statistaa.R;
import com.htbr.statistaa.Classes.Exercise;
import com.htbr.statistaa.Adapters.ExerciseAdapter;
import com.htbr.statistaa.Classes.UserHandler;

import java.io.Serializable;

public class ExercisesRecyclerActivity extends AppCompatActivity implements View.OnClickListener, OnExerciseSelectedListener {




    private RecyclerView mExercisesRecycler;
    private ExerciseAdapter mAdapter;
    private ViewGroup emptyView;
    private static final String TAG = "ExercisesRecycler";

    private FirebaseFirestore mFirestore;

    private FirebaseUser user;

    private Query mQuery;
    private int LIMIT = 50;

    private UserHandler userHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercisesrecycler);



        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        mExercisesRecycler = findViewById(R.id.my_recycler_view);
        //TODO do we need this??
        //emptyView = findViewById(R.id.view_empty);

        userHandler = new UserHandler(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }


    @Override
    public void onStart() {
        super.onStart();
        initFirestore();
        initRecyclerView();





        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }


    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();




        //get user group id
        long group = userHandler.getUsergroup( user );


        String collection = "";

        if(group == 1){
            collection = "ExercisesA";
            Log.w(TAG, "Group is "+ group + "collection is "+ collection);
        } else if (group == 2){
            collection = "ExercisesB";
            Log.w(TAG, "Group is "+ group + "collection is "+ collection);
        } else{
            Toast.makeText(this, getString(R.string.no_user_group), Toast.LENGTH_LONG).show();
        }


        if(collection == ""){
            Log.e(TAG,"collection is empty string");
        }

        // Get the 50 highest rated restaurants
        //TODO generate GroupID
        mQuery = mFirestore.collection(collection)
                .orderBy("number", Query.Direction.DESCENDING)
                .limit(LIMIT);



        mAdapter = new ExerciseAdapter(mQuery, this);



        mExercisesRecycler.setAdapter(mAdapter);
    }



    private void initRecyclerView() {
        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new ExerciseAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    mExercisesRecycler.setVisibility(View.GONE);
                    //emptyView.setVisibility(View.VISIBLE);
                } else {
                    mExercisesRecycler.setVisibility(View.VISIBLE);
                    //emptyView.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        mExercisesRecycler.setLayoutManager(new LinearLayoutManager(this));
        mExercisesRecycler.setAdapter(mAdapter);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onExerciseSelected(DocumentSnapshot exerciseSnapshot) {
        Exercise exercise = exerciseSnapshot.toObject(Exercise.class);
        exercise.setId(exerciseSnapshot.getId());
        Log.d(TAG, "Exercise "+ exercise.getName() + " id: " + exercise.getId() + " selected");



        Intent intent = new Intent(this, ExerciseScrollingActivity.class);
        intent.putExtra("Exercise" , (Serializable) exercise);
        intent.putExtra("CallingActivity", "ExerciseRecyclerActivity");
        startActivity(intent);


    }


    //do nothing
    @Override
    public void onExerciseSelected(Exercise exercise) {

    }


/*    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if ( item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
