package com.htbr.statistaa.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.htbr.statistaa.R;

import java.io.Serializable;

public class ExercisesRecyclerActivity extends AppCompatActivity implements View.OnClickListener, ExerciseAdapter.OnExerciseSelectedListener  {




    private RecyclerView mExercisesRecycler;
    private ExerciseAdapter mAdapter;
    private ViewGroup emptyView;
    private static final String TAG = "ExercisesRecycler";

    private FirebaseFirestore mFirestore;


    private Query mQuery;
    private int LIMIT = 50;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercisesrecycler);

        mExercisesRecycler = findViewById(R.id.my_recycler_view);
        //TODO do we need this??
        //emptyView = findViewById(R.id.view_empty);



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
        long group = UserHandler.getUsergroup(this, FirebaseAuth.getInstance().getCurrentUser());;


        String collection = "";

        if(group == 1){
            collection = "ExercisesA";
            Log.w(TAG, "Group is "+ group + "collection is "+ collection);
        } else if (group == 2){
            collection = "ExercisesB";
            Log.w(TAG, "Group is "+ group + "collection is "+ collection);
        } else{
            Toast.makeText(this, "Error, you are not part of a user group, please call help", Toast.LENGTH_LONG).show();
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
        startActivity(intent);


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

}
